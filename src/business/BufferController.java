package business;

import java.util.LinkedList;
import java.util.List;

public class BufferController {
    private List<Buffer> buffer;
    private Buffer toStoreBuffer;
    private Buffer toSentBuffer;
    private StatisticController statisticsController;

    public BufferController(StatisticController statisticsController, int bufferNumber) {
        this.statisticsController = statisticsController;
        buffer = new LinkedList<>();
        for (int i = 0; i < bufferNumber; i++) {
            buffer.add(new Buffer(i + 1));
        }
        this.toSentBuffer = buffer.get(0);//для корректного отображения в stepByStep
        this.toStoreBuffer = buffer.get(0);
    }

    public void chooseToStore() {
        if (this.isFull()) { //выбираем самую старую заявку в буфере
            System.out.println("BUFFER IS FULL");
            for (Buffer el : buffer) {
                //ищем самую старую заявку в буфере
                if (el.getCurrentTask().getCreationTime() < this.toStoreBuffer.getCurrentTask().getCreationTime()) {
                    this.toStoreBuffer = el;
                }
            }
        } else { //на первое свободное место
            for (Buffer el : buffer) {
                if (el.isFree()) {
                    this.toStoreBuffer = el;
                    return;
                }
            }
        }
    }

    public void chooseToSent() { //приоритет по номеру источника; если совпадает, то по времени создания заяки
        for (Buffer el : buffer) {
            if (this.toSentBuffer.isFree() && !el.isFree()) {
                toSentBuffer = el;
            } else if (!toStoreBuffer.isFree() && !el.isFree()) {
                if (el.getSourceNumber() < toSentBuffer.getSourceNumber()) {
                    toSentBuffer = el;
                } else if (el.getSourceNumber() == toSentBuffer.getSourceNumber()
                        && el.getCurrentTask().getCreationTime() > toSentBuffer.getCurrentTask().getCreationTime()) {
                    toSentBuffer = el;
                }
            }
        }
    }

    public Task sendTask() {
        chooseToSent();
        return toSentBuffer.sendTask();
    }

    public Task receiveTask(Task task) {
        this.chooseToStore();
        Task failedTask = null;
        if (this.isFull()) {
            System.out.println(" BUFFER: " + toStoreBuffer.getCurrentTask().getTaskId() +
                    " replaced by " + task.getTaskId());
            this.statisticsController.getFailed(task, task.getCreationTime());
            failedTask = toStoreBuffer.getCurrentTask();
        } else {
            System.out.println("BUFFER " + buffer.indexOf(toStoreBuffer) + ": source number - " + task.getSourceNumber()
                    + " task id-" + task.getTaskId() + " received");
        }
        toStoreBuffer.receiveTask(task);
        return failedTask;
    }

    public boolean isFull() {
        for (Buffer el : buffer) {
            if (el.isFree()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (Buffer el : buffer) {
            if (!el.isFree()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int index) {
        return buffer.get(index).isFree();
    }

    public int getNumberOfBuffers() {
        return buffer.size();
    } //для вывода статуса буфера

    public String getCurrentTaskId(int index) {//для вывода статуса буфера
        return buffer.get(index).getCurrentTaskId();
    }

    public double getReceiveTime(int index) {
        return buffer.get(index).getReceiveTime();
    }//для вывода статуса буфера

    public Buffer getStoreBuffer() {
        return toStoreBuffer;
    }

    public Buffer getSentBuffer() {
        return toSentBuffer;
    }
}
