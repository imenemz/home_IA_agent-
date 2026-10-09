package env;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Room {
    private final String name;
    private final Set<String> objects;
    private final Set<String> neighbors;

    public Room(String name) {
        this.name = name;
        this.objects = new HashSet<>();
        this.neighbors = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<String> getObjects() {
        return new HashSet<>(objects);
    }

    public void addObject(String object) {
        objects.add(object);
    }

    public void removeObject(String object) {
        objects.remove(object);
    }

    public boolean containsObject(String object) {
        return objects.contains(object);
    }

    public Set<String> getNeighbors() {
        return new HashSet<>(neighbors);
    }

    public void addNeighbor(String roomName) {
        neighbors.add(roomName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Room{name='" + name + "', objects=" + objects + ", neighbors=" + neighbors + "}";
    }
}
