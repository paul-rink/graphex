package graphex2021.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public abstract class Subject {
    private List<Observer> observers;

    public Subject() {
        this.observers = new LinkedList<>();
    }

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.doUpdate(this);
        }
    }

    public abstract Object getState();
}
