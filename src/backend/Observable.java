package backend;

import java.util.ArrayList;
import java.util.List;
public abstract class Observable {
    private final List<Observer> appObservers = new ArrayList<>();
    public void notifyObservers(){
        appObservers.forEach(observer -> observer.update(getData()));
    }
    public void addObserver(Observer listener){
        appObservers.add(listener);
    }
    public abstract DrawData getData();
}
