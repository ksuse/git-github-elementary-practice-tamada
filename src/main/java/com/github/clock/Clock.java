package com.github.clock;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    private List<UpdateObserver> observers = new ArrayList<>();
    private boolean running = false;
    private int hour;
    private int minute;
    private int second;
    private Timer timer = new Timer();
    private TimerTask task = new TimerTask(){
        @Override
        public void run() {
            // Observers それぞれの呼び出しに時間がかからないよう
            // 別Threadの処理として実行する．
            new Thread(() -> callUpdateObservers())
                .start();
        }
    };
    private ZonedDateTime datetime;
    private ZoneId zoneId;

    public Clock(){
        this(ZoneId.systemDefault());
    }

    public Clock(ZoneId id) {
        this.zoneId = id;
        datetime = ZonedDateTime.now(zoneId);
    }

    private void updateTime(){
        datetime = ZonedDateTime.now(zoneId);
        hour = datetime.getHour();
        minute = datetime.getMinute();
        second = datetime.getSecond();
    }

    private void callUpdateObservers(){
        synchronized(observers){
            updateTime();
            for(UpdateObserver observer: observers){
                observer.update(this);
            }
        }
    }

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }

    public int getSecond(){
        return second;
    }

    public boolean isRunning(){
        return running;
    }

    public void start(){
        timer.schedule(task, 1000, 1000);
        running = true;
    }

    public void stop(){
        timer.cancel();
        running = false;
    }

    public void addUpdateObserver(UpdateObserver observer){
        observers.add(observer);
    }

    public void removeUpdateObserver(UpdateObserver observer){
        observers.remove(observer);
    }
}
