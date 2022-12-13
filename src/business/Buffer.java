package business;

public class Buffer {
    private int number;
    private Task currentTask;

    public Buffer(int number) {
        this.number = number;
        currentTask = null;
    }

    public boolean isFree() {
        return currentTask == null;
    }

    public void receiveTask(Task task) {
        this.currentTask = task;
    }

    public Task sendTask() {
        Task taskToSend = this.currentTask;
        this.currentTask = null;
        return taskToSend;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public int getSourceNumber() {
        return this.currentTask.getSourceNumber();
    }

    public String getCurrentTaskId() { //для вывода в пошаговом режиме
        return this.currentTask.getTaskId();
    }

    public double getReceiveTime() { //для вывода в пошаговом режиме
        return this.currentTask.getCreationTime();
    }

    public int getNumber() {
        return number;
    }
}
