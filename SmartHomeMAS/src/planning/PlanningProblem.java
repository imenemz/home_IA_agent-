package planning;

import java.util.HashMap;
import java.util.Map;

public class PlanningProblem {

    private final Map<String, String> taskRooms = new HashMap<>();
    private final Map<String, String> taskTypes = new HashMap<>();
    private PlanningGoal goal;

    public void addTask(String taskId, String room, String type) {
        taskRooms.put(taskId, room);
        taskTypes.put(taskId, type);
    }

    public String getTaskRoom(String taskId) {
        return taskRooms.get(taskId);
    }

    public String getTaskType(String taskId) {
        return taskTypes.get(taskId);
    }

    public boolean hasTask(String taskId) {
        return taskRooms.containsKey(taskId);
    }

    public PlanningGoal getGoal() {
        return goal;
    }

    public void setGoal(PlanningGoal goal) {
        this.goal = goal;
    }
}
