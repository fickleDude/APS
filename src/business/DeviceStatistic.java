package business;

public class DeviceStatistic {
    private double workTime;
    private long numberOfTasksProcessed;

    public DeviceStatistic() {
        this.numberOfTasksProcessed = 0;
        this.workTime = 0;
    }

    public double getWorkTime() {
        return workTime;
    }

    public void worked(double workTime) {
        this.workTime += workTime;
        this.numberOfTasksProcessed++;
    }
}
