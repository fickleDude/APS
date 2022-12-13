package business;

public class Device {

    private int number;
    private double lambda;
    private double prevProcessTime;
    private Task currentTask;

    public Device(int number, double lambda) {
        this.number = number;
        this.lambda = lambda;
        this.prevProcessTime = 0;
    }

    private double distributionLaw() {
        return (-1.0 / this.lambda) * Math.log(Math.random());
    }

    public void receiveTask(Task task) {
        double deltaTime = distributionLaw();
        System.out.println("DEVICE: device number - " + this.number
                + " task number - " + task.getTaskId() + " received");
        System.out.println("set to: " + (deltaTime + prevProcessTime));
        if (this.prevProcessTime > task.getCreationTime()) {
            //прибор не был свободен, заявка находилась в буфере
            task.setFinishedTime(prevProcessTime + deltaTime);
            task.setReceivedTime(prevProcessTime);
        } else {
            //прибор был свободен и заявка пришла из буфера моментально
            task.setFinishedTime(task.getCreationTime() + deltaTime);
            task.setReceivedTime(task.getCreationTime());
        }
        this.currentTask = task;
    }

    public Task finishTask() {
        Task taskToFinish = currentTask;
        System.out.println("DEVICE: device number - " + this.number +
                " task number - " + currentTask.getTaskId() + "finished");
        //уже установили время окончания в момент получения заявки
        this.prevProcessTime = this.currentTask.getFinishedTime();
        this.currentTask = null;
        return taskToFinish;
    }

    public boolean isFree() {
        return currentTask == null;
    }

    public Task getCurrentTask() {
        return currentTask;
    }//нужно в контроллере для определения приоритета

    public int getNumber() {
        return number;
    }//нужно в контроллере для определения приоритета
}
