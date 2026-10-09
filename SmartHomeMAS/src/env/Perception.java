package env;

import java.util.List;
import java.util.Set;

public class Perception {
    private final String currentRoom;
    private final Set<String> objectsHere;
    private final List<Task> tasksHere;
    private final Set<String> neighbors;

    public Perception(String currentRoom,
                      Set<String> objectsHere,
                      List<Task> tasksHere,
                      Set<String> neighbors) {
        this.currentRoom = currentRoom;
        this.objectsHere = objectsHere;
        this.tasksHere = tasksHere;
        this.neighbors = neighbors;
    }

    public String getCurrentRoom() { return currentRoom; }
    public Set<String> getObjectsHere() { return objectsHere; }
    public List<Task> getTasksHere() { return tasksHere; }
    public Set<String> getNeighbors() { return neighbors; }
}
