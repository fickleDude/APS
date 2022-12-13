package business;

import java.util.LinkedList;
import java.util.List;

public class StatisticController {
    private List<SourceStatistic> sourceStatistics;//index + 1 - номер источника
    private List<DeviceStatistic> deviceStatistics;//index + 1 - номер прибора

    StatisticController(int numberOfSources, int numberOfDevices) {
        this.sourceStatistics = new LinkedList<>();
        this.deviceStatistics = new LinkedList<>();
        for (int i = 0; i < numberOfSources; i++) {
            sourceStatistics.add(new SourceStatistic());
        }
        for (int i = 0; i < numberOfDevices; i++) {
            deviceStatistics.add(new DeviceStatistic());
        }
    }

    void getGenerated(Task task) {
        this.sourceStatistics.get(task.getSourceNumber() - 1).taskGenerated(task);
    }

    void getReceived(Task task) {
        this.sourceStatistics.get(task.getSourceNumber() - 1).taskReceived(task);
    }

    void getSucceed(Task task, int deviceNum) {
        this.sourceStatistics.get(task.getSourceNumber() - 1).taskSucceed(task);
        double newDeviceTimeWork = task.getFinishedTime() - task.getReceivedTime();
        this.deviceStatistics.get(deviceNum - 1).worked(newDeviceTimeWork);
    }

    void getFailed(Task task, double time) {
        System.out.println("FAILED: source number - " + task.getSourceNumber() + " task id - " + task.getTaskId());
        this.sourceStatistics.get(task.getSourceNumber() - 1).taskFailed(task, time);
    }

    public long getNumberGeneratedTasks(int sourceIndex) {
        return sourceStatistics.get(sourceIndex).getNumberOfGeneratedTasks();
    }

    public double getAverageProcessingTime(int sourceIndex) {
        double timeProcessing = sourceStatistics.get(sourceIndex).getTimeProcessing();
        int numberOfSucceedTasks = sourceStatistics.get(sourceIndex).getNumberOfSucceedTasks();
        return timeProcessing / numberOfSucceedTasks;
    }

    public double getDispersionProcessingTime(int sourceIndex) {
        double timeProcessing = sourceStatistics.get(sourceIndex).getSquaredTimeProcessing();
        int numberOfSucceedTasks = sourceStatistics.get(sourceIndex).getNumberOfSucceedTasks();
        return (timeProcessing / numberOfSucceedTasks) - Math.pow(this.getAverageProcessingTime(sourceIndex), 2);
    }

    public double getAverageWaitingTime(int sourceIndex) {
        double timeWaiting = sourceStatistics.get(sourceIndex).getTimeWaiting();
        int numberOfSucceedTasks = sourceStatistics.get(sourceIndex).getNumberOfSucceedTasks();
        return timeWaiting / numberOfSucceedTasks;
    }

    public double getDispersionWaitingTime(int sourceIndex) {
        double timeWaiting = Math.pow(sourceStatistics.get(sourceIndex).getSquaredTimeWaiting(), 2);
        int numberOfSucceedTasks = sourceStatistics.get(sourceIndex).getNumberOfSucceedTasks();
        return (timeWaiting / numberOfSucceedTasks) - Math.pow(this.getAverageWaitingTime(sourceIndex), 2);
    }

    public double getAverageTimeInSystem(int sourceIndex) {
        double timeInSystem = sourceStatistics.get(sourceIndex).getTimeInSystem();
        int numberOfSucceedTasks = sourceStatistics.get(sourceIndex).getNumberOfSucceedTasks();
        return timeInSystem / numberOfSucceedTasks;
    }

    public double getFailureProbability(int sourceIndex) {
        return (double) (sourceStatistics.get(sourceIndex).getNumberOfFailedTasks()
                / (double) sourceStatistics.get(sourceIndex).getNumberOfGeneratedTasks());
    }

    public double getUsageRate(int deviceIndex, double systemTime) {
        return deviceStatistics.get(deviceIndex).getWorkTime() / systemTime;
    }

    public long getAllNumberGeneratedTasks() {
        long result = 0;
        for (SourceStatistic statistic : sourceStatistics) {
            result += statistic.getNumberOfGeneratedTasks();
        }
        return result;
    }

    public long getAllNumberFailedTasks() {
        long result = 0;
        for (SourceStatistic statistic : sourceStatistics) {
            result += statistic.getNumberOfFailedTasks();
        }
        return result;
    }
}
