package business;

public class SourceStatistic {
    private int numberOfGeneratedTasks;//количество сгенерированных заявок одного источника
    private int numberOfFailedTasks;//количество непринятых заявок одного источника
    private int numberOfSucceedTasks;//количество обработанных заявок одного источника

    private double timeInSystem;//время жизни заявки одного источника
    private double timeWaiting;//время между созданием и получением прибора
    private double timeProcessing;//время обработки прибором заявки одного источника
    private double squaredTimeProcessing;
    private double squaredTimeWaiting;


    public SourceStatistic() {
        this.numberOfGeneratedTasks = 0;
        this.numberOfFailedTasks = 0;
        this.numberOfSucceedTasks = 0;

        this.timeInSystem = 0;
        this.timeWaiting = 0;
        this.timeProcessing = 0;
        this.squaredTimeProcessing= 0;
        this.squaredTimeWaiting= 0;
    }

    public int getNumberOfGeneratedTasks() {
        return numberOfGeneratedTasks;
    }

    public int getNumberOfFailedTasks() {
        return numberOfFailedTasks;
    }

    public int getNumberOfSucceedTasks() {
        return numberOfSucceedTasks;
    }

    public double getTimeInSystem() {
        return timeInSystem;
    }

    public double getTimeWaiting() {
        return timeWaiting;
    }

    public double getTimeProcessing() {
        return timeProcessing;
    }

    public double getSquaredTimeProcessing() {
        return squaredTimeProcessing;
    }

    public double getSquaredTimeWaiting() {
        return squaredTimeWaiting;
    }

    void taskSucceed(Task task) {
        this.numberOfSucceedTasks++;

        this.timeProcessing += task.getFinishedTime() - task.getReceivedTime();
        this.squaredTimeProcessing += Math.pow(task.getFinishedTime() - task.getReceivedTime(), 2) ;
        this.timeInSystem += task.getFinishedTime() - task.getCreationTime();
    }

    void taskFailed(Task task, double currentTime) {
        this.numberOfFailedTasks++;

        this.timeInSystem += currentTime - task.getCreationTime();
    }

    void taskGenerated(Task task) {
        this.numberOfGeneratedTasks++;
    }

    void taskReceived(Task task) {
        this.timeWaiting += task.getReceivedTime() - task.getCreationTime();
        this.squaredTimeWaiting += Math.pow(task.getReceivedTime() - task.getCreationTime(), 2);
    }
}
