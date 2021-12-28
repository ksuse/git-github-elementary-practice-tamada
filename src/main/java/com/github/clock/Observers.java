package com.github.clock;

import java.util.ArrayList;
import java.util.List;

public class Observers {
    private List<Observer> observers = new ArrayList<>();

    public void notify(Clock clock) {
        observers.stream()
            .forEach(observer -> observer.update(clock));
    }

    public void add(Observer observer){
        observers.add(observer);
    }

    public void remove(Observer observer){
        observers.remove(observer);
    }

}
