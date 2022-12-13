package business;

public class Task {
    private String taskId;
    private int sourceNumber;
    private double creationTime;
    private double receivedTime;//время попадание заявки на прибор
    private double finishedTime;

    public Task(int sourceNumber, int numberOfSourceTask, double creationTime) {
        this.sourceNumber = sourceNumber;
        this.creationTime = creationTime;
        this.receivedTime = -1;
        this.finishedTime = -1;
        this.taskId = sourceNumber + "." + numberOfSourceTask;
    }

    public void setReceivedTime(double receivedTime) {
        this.receivedTime = receivedTime;
    }

    public void setFinishedTime(double finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public double getCreationTime() {
        return creationTime;
    }

    public double getReceivedTime() {
        return receivedTime;
    }

    public double getFinishedTime() {
        return finishedTime;
    }

    public double getProcessedTime() {
        return finishedTime - receivedTime;
    }
}
