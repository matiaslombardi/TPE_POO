package backend;

@FunctionalInterface
public interface Observer {
    void update(DrawData model);
}
