package env;

import java.util.HashSet;
import java.util.Set;

public class Task {
    private final String id;
    private final String type;
    private final String room;
    private String assignedTo = null;
    private String state = TaskState.PENDING;
    private final Set<String> dependencies = new HashSet<>();

    public Task(String id, String type, String room) {
        this.id = id;
        this.type = type;
        this.room = room;
    }

    public void addDependency(String taskId) {
        dependencies.add(taskId);
    }

    public boolean dependenciesSatisfied(Environment env) {
        for (String depId : dependencies) {
            Task dep = env.getTask(depId);
            if (dep == null || !TaskState.DONE.equals(dep.getState())) return false;
        }
        return true;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getRoom() { return room; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}
