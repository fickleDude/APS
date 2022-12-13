package business;

public class Source {
    private int number;
    private double a;
    private double b;
    private Task currentTask;
    private int numberOfGeneratedTasks;//нуже для формирования task id

    public Source(int number, double a, double b) {
        this.number = number;
        this.a = a;
        this.b = b;
        this.currentTask = null;
        this.numberOfGeneratedTasks = 0;
        this.generateTask();
    }

    private double distributionLaw() {
        return a + (b - a) * (Math.random());
    }

    public void generateTask() {
        double deltaTime = distributionLaw();
        double prevTime = this.currentTask == null ? 0 : this.currentTask.getCreationTime();
        System.out.println("SOURCE: source number - " + number + " generating to "
                + (prevTime + deltaTime));
        numberOfGeneratedTasks++;
        this.currentTask = new Task(this.number, this.numberOfGeneratedTasks, prevTime + deltaTime);
    }

    public Task sendTask() {
        Task taskToSend = this.currentTask;
        System.out.println("SOURCE: source number - " + taskToSend.getSourceNumber() + " task id - "
                + taskToSend.getTaskId() + " sent");
        this.generateTask();
        return taskToSend;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public int getNumber() {
        return number;
    }
}
