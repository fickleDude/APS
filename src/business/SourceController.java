package business;

import java.util.LinkedList;
import java.util.List;

public class SourceController {
    private List<Source> sources;
    private Source toSentSource;
    private double eventTime;
    private StatisticController statisticController;

    public SourceController(StatisticController statisticsController, int sourcesNumber, double a, double b) {
        this.statisticController = statisticsController;
        this.sources = new LinkedList<>();
        for (int i = 0; i < sourcesNumber; i++) {
            sources.add(new Source(i + 1, a, b));
        }
        this.toSentSource = sources.get(0);
        this.chooseSource();
        this.setEventTime();
    }

    //    public void chooseSource() { //ищем заявку, у которой createdTime меньше всех
//        Source sourceWithMinTime = this.sources.get(0);
//        for (Source source : sources) {
//            //ищем сначала заявку, созданную раньше остальных
//            if (source.getCurrentTask().getCreationTime() < sourceWithMinTime.getCurrentTask().getCreationTime()) {
//                sourceWithMinTime = source;
//            }
//            //если у заявок совпадает время создания, то смотрим на номер источника
//            else if (source.getCurrentTask().getCreationTime() == sourceWithMinTime.getCurrentTask().getCreationTime()) {
//                if (source.getNumber() > sourceWithMinTime.getNumber()) {
//                    sourceWithMinTime = source;
//                }
//            }
//        }
//        this.eventTime = sourceWithMinTime.getCurrentTask().getCreationTime();
//        this.currentSource = sourceWithMinTime;
//    }
    public void chooseSource() { //ищем заявку, у которой createdTime меньше всех
        for (Source source : sources) {
            if (this.toSentSource.getCurrentTask().getCreationTime() > source.getCurrentTask().getCreationTime()) {
                this.toSentSource = source;
            }
        }
    }

    private void setEventTime() { //event time - самое раннее время создания заявки из заявок на источниках
        this.eventTime = this.toSentSource.getCurrentTask().getCreationTime();
    }

    public Task sendTask() {
        Task task = this.toSentSource.sendTask();
        this.chooseSource();
        this.setEventTime();
        this.statisticController.getGenerated(toSentSource.getCurrentTask());
        return task;
    }

    public double getEventTime() {
        return eventTime;
    }

    public Source getSentSource() {
        return toSentSource;
    }

    public int getNumberOfSources() {
        return sources.size();
    }
}
