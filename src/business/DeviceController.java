package business;

import java.util.LinkedList;
import java.util.List;

public class DeviceController {
    private List<Device> devices;
    private Device toReceiveDevice;
    private Device toFinishDevice;
    private StatisticController statisticController;
    private double eventTime;

    public DeviceController(StatisticController statisticsController, int devicesNumber, double lambda) {
        this.statisticController = statisticsController;
        devices = new LinkedList<>();
        for (int i = 0; i < devicesNumber; i++) {
            devices.add(new Device(i + 1, lambda));
        }
        this.toFinishDevice = devices.get(0);
        this.toReceiveDevice = devices.get(0);
        this.setEventTime(); //изначально устанавливаем максимальное значение,
        // чтобы начать работу программу с источникрв и буфера
    }

    private void chooseToFinishDevice() { //ищем заявку, у которой finishedTime меньше всех
        for (Device device : devices) {
            if (!device.isFree()) {
                if (this.toFinishDevice.isFree()) {
                    this.toFinishDevice = device;
                } else if (this.toFinishDevice.getCurrentTask().getFinishedTime() > device.getCurrentTask().getFinishedTime()) {
                    this.toFinishDevice = device;
                }
            }
        }
    }

    private void chooseToReceiveDevice() { //приоритет постановки на обслуживание по номеру прибора
        for (Device device : devices) {
            if (device.isFree()) {
                this.toReceiveDevice = device;
                return;
            }
        }
    }

    private void setEventTime() { //event time - самое раннее время окончание заявки из заявок на приборах
        this.eventTime = Double.MAX_VALUE;
        for (Device device : devices) {
            if (!device.isFree() && device.getCurrentTask().getFinishedTime() < this.eventTime) {
                this.eventTime = device.getCurrentTask().getFinishedTime();
            }
        }
    }

    public void receiveTask(Task task) {
        this.chooseToReceiveDevice();
        toReceiveDevice.receiveTask(task);
        this.statisticController.getReceived(task);
        this.setEventTime();
    }

    public Task finishTask() {
        this.chooseToFinishDevice();
        this.statisticController.getSucceed(toFinishDevice.getCurrentTask(), toFinishDevice.getNumber());
        Task finishedTask = this.toFinishDevice.finishTask();
        this.setEventTime();
        return finishedTask;
    }

    public boolean isFull() {
        for (Device device : devices) {
            if (device.isFree()) {
                return false;
            }
        }
        return true;
    }

    public double getEventTime() {
        return eventTime;
    }

    public int getNumberOfDevices() {
        return devices.size();
    }

    public Device getReceiveDevice() {
        return toReceiveDevice;
    }

    public Device getFinishDevice() {
        return toFinishDevice;
    }
}
