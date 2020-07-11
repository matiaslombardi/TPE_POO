package backend;

import java.util.ArrayList;
import java.util.List;
public abstract class Observable {
    private final List<Observer> appObservers = new ArrayList<>();
    public void notifyObservers(){
        appObservers.forEach(observer -> observer.update(getData()));
    }
    public void addObserver(Observer observer){
        appObservers.add(observer);
    }
    public abstract DrawData getData();
}
