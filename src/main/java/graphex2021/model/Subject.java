package graphex2021.model;

import java.util.List;

/**
 *
 * @author D. Flohs, K. Marquardt, P. Rink
 * @version 1.0 14.01.2021
 */
public abstract class Subject {
    List<Observer> observers;

    public void register(Observer observer) {

    }

    public void unregister(Observer observer) {

    }

    public void notifyObservers() {

    }

    public abstract Object getState();
}
