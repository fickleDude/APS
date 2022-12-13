package business;

import java.text.DecimalFormat;

import static business.Action.*;

public class Engine {
    private double sourceEventTime;
    private double deviceEventTime;
    private int amountOfTasks;
    private StatisticController statisticController;
    private DeviceController deviceController;
    private SourceController sourceController;
    private BufferController bufferController;

    private Object[][] calendar;
    private Object[][] buffer;
    private int currentCalendarAction;
    private int currentBufferAction;

    public Engine(int amountOfTasks, int sourcesNumber, int devicesNumber, int bufferNumber,
                  double a, double b, double lambda) {
        this.amountOfTasks = amountOfTasks;
        this.statisticController = new StatisticController(sourcesNumber, devicesNumber);
        this.deviceController = new DeviceController(this.statisticController, devicesNumber, lambda);
        this.sourceController = new SourceController(this.statisticController, sourcesNumber, a, b);
        this.bufferController = new BufferController(this.statisticController, bufferNumber);
        this.sourceEventTime = 0;
        this.deviceEventTime = 0;

        this.calendar = new Object[(int) (amountOfTasks * amountOfTasks)][5];
        this.buffer = new Object[(int) (amountOfTasks * amountOfTasks)][3];
        currentCalendarAction = 0;
        currentBufferAction = 0;
    }

    public void autoMode() {
        while (statisticController.getAllNumberGeneratedTasks() < this.amountOfTasks) {
            stepByStepMode();
        }
    }

    public void stepByStepMode() {
        this.sourceEventTime = this.sourceController.getEventTime();
        this.deviceEventTime = this.deviceController.getEventTime();

        if (this.deviceEventTime <= this.sourceEventTime) {

            Task finishedTask = deviceController.finishTask();
            this.setCalendarStatus(FINISH, finishedTask);

            if (!bufferController.isEmpty()) {
                Task receivedTask = bufferController.sendTask();
                deviceController.receiveTask(receivedTask);
                this.setCalendarStatus(RECEIVE, receivedTask);
            }
        } else {
            this.setCalendarStatus(GENERATE, null);

            Task sentTask = sourceController.sendTask();
            Task failedTask = bufferController.receiveTask(sentTask);
            if (failedTask != null) {
                this.setCalendarStatus(FAIL, failedTask);
            }
            this.setCalendarStatus(STORE, sentTask);

            if (!deviceController.isFull()) {
                //this.setCalendarStatus(SENT, null);

                Task receivedTask = bufferController.sendTask();
                this.setCalendarStatus(SENT, receivedTask);
                deviceController.receiveTask(receivedTask);

                this.setCalendarStatus(RECEIVE, receivedTask);
            }
        }
    }

    public Object[][] getSourceStatus() { //для отображения в autoMode
        int amountOfSources = sourceController.getNumberOfSources();
        Object[][] result = new Object[amountOfSources][8];
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < amountOfSources; i++) {
            result[i] = new Object[]{i + 1, format.format(statisticController.getNumberGeneratedTasks(i)),
                    format.format(statisticController.getFailureProbability(i)), format.format(statisticController.getAverageTimeInSystem(i)),
                    format.format(statisticController.getAverageWaitingTime(i)), format.format(statisticController.getAverageProcessingTime(i)),
                    format.format(statisticController.getDispersionWaitingTime(i)), format.format(statisticController.getDispersionProcessingTime(i))};
        }
        return result;
    }

    public Object[] getCalendarStatus(int index) {
        return calendar[index];
    }//для отображения в stepByStepMode

    private void setCalendarStatus(Action action, Task task) {
        DecimalFormat format = new DecimalFormat("0.00");
        switch (action) {
            case FINISH ->
                    calendar[currentCalendarAction] = new Object[]{"Device" + deviceController.getFinishDevice().getNumber(),
                            format.format(task.getFinishedTime()), "finished task",
                            statisticController.getAllNumberGeneratedTasks(), statisticController.getAllNumberFailedTasks()};
            case RECEIVE ->
                    calendar[currentCalendarAction] = new Object[]{"Device" + deviceController.getReceiveDevice().getNumber(),
                            format.format(task.getReceivedTime()), "received task",
                            statisticController.getAllNumberGeneratedTasks(), statisticController.getAllNumberFailedTasks()};
            case GENERATE ->
                    calendar[currentCalendarAction] = new Object[]{"Source" + sourceController.getSentSource().getNumber(),
                            format.format(sourceEventTime), "generated task",
                            statisticController.getAllNumberGeneratedTasks() + 1, statisticController.getAllNumberFailedTasks()};
            case STORE ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getStoreBuffer().getNumber(),"-",
                            "store task " + task.getTaskId()};
            case SENT ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getSentBuffer().getNumber(),"-",
                            "sent task " + task.getTaskId()};
            case FAIL ->
                    calendar[currentCalendarAction] = new Object[]{"Buffer" + bufferController.getStoreBuffer().getNumber(), "-",
                            "failed task " + task.getTaskId()};
        }
        setBufferStatus();
        this.currentCalendarAction++;

    }

    public Object[][] getDeviceStatus() { //для отображения в autoMode
        int amountOfDevices = deviceController.getNumberOfDevices();
        Object[][] result = new Object[amountOfDevices][2];
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < amountOfDevices; i++) {
            result[i] = new Object[]{i + 1, format.format(statisticController.getUsageRate(i, this.getSystemTime()))};
        }
        return result;
    }

    public Object[][] getBufferStatus(int currentStep) { //для отображения в stepByStepMode
        int numberOfBuffers = bufferController.getNumberOfBuffers();
        Object[][] result = new Object[numberOfBuffers][4];
        for (int i = 0; i < numberOfBuffers; i++) {
            result[i] = buffer[currentStep * numberOfBuffers + i];
        }
        return result;
    }

    public void setBufferStatus() {
        int numberOfBuffers = bufferController.getNumberOfBuffers();
        DecimalFormat format = new DecimalFormat("0.00");
        for (int i = 0; i < numberOfBuffers; i++) {
            if (!bufferController.isEmpty(i)) {
                buffer[currentBufferAction] = new Object[]{i + 1, format.format(bufferController.getReceiveTime(i)),
                        bufferController.getCurrentTaskId(i)};
            } else {
                buffer[currentBufferAction] = new Object[]{i + 1, 0.0, 0};
            }
            currentBufferAction += 1;
        }
    }

    public long getAllNumberOfGeneratedTasks() {//для отображения в stepByStepMode
        return statisticController.getAllNumberGeneratedTasks();
    }

    public long getAmountOfTasks() {
        return this.amountOfTasks;
    }//для отображения в stepByStepMode

    public double getSystemTime() {
        return Double.max(sourceEventTime, deviceEventTime);
    }
}
