package planning;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlanningState {

    private String agentRoom;
    private boolean hasVacuum;
    private String vacuumRoom; // null if already carried
    private final Set<String> doneTasks;
    private final Set<String> pendingTasks;
    private final Set<String> dishesRooms;

    public PlanningState(String agentRoom,
                         boolean hasVacuum,
                         String vacuumRoom,
                         Set<String> doneTasks,
                         Set<String> pendingTasks,
                         Set<String> dishesRooms) {
        this.agentRoom = agentRoom;
        this.hasVacuum = hasVacuum;
        this.vacuumRoom = vacuumRoom;
        this.doneTasks = new HashSet<>(doneTasks);
        this.pendingTasks = new HashSet<>(pendingTasks);
        this.dishesRooms = new HashSet<>(dishesRooms);
    }

    public PlanningState(PlanningState other) {
        this.agentRoom = other.agentRoom;
        this.hasVacuum = other.hasVacuum;
        this.vacuumRoom = other.vacuumRoom;
        this.doneTasks = new HashSet<>(other.doneTasks);
        this.pendingTasks = new HashSet<>(other.pendingTasks);
        this.dishesRooms = new HashSet<>(other.dishesRooms);
    }

    public String getAgentRoom() {
        return agentRoom;
    }

    public void setAgentRoom(String agentRoom) {
        this.agentRoom = agentRoom;
    }

    public boolean hasVacuum() {
        return hasVacuum;
    }

    public void setHasVacuum(boolean hasVacuum) {
        this.hasVacuum = hasVacuum;
    }

    public String getVacuumRoom() {
        return vacuumRoom;
    }

    public void setVacuumRoom(String vacuumRoom) {
        this.vacuumRoom = vacuumRoom;
    }

    public Set<String> getDoneTasks() {
        return doneTasks;
    }

    public Set<String> getPendingTasks() {
        return pendingTasks;
    }

    public Set<String> getDishesRooms() {
        return dishesRooms;
    }

    public boolean isTaskDone(String taskId) {
        return doneTasks.contains(taskId);
    }

    public boolean isTaskPending(String taskId) {
        return pendingTasks.contains(taskId);
    }

    public void markTaskDone(String taskId) {
        pendingTasks.remove(taskId);
        doneTasks.add(taskId);
    }

    public boolean hasDishesInRoom(String room) {
        return dishesRooms.contains(room);
    }

    public void removeDishesFromRoom(String room) {
        dishesRooms.remove(room);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanningState)) return false;
        PlanningState that = (PlanningState) o;
        return hasVacuum == that.hasVacuum
                && Objects.equals(agentRoom, that.agentRoom)
                && Objects.equals(vacuumRoom, that.vacuumRoom)
                && Objects.equals(doneTasks, that.doneTasks)
                && Objects.equals(pendingTasks, that.pendingTasks)
                && Objects.equals(dishesRooms, that.dishesRooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentRoom, hasVacuum, vacuumRoom, doneTasks, pendingTasks, dishesRooms);
    }

    @Override
    public String toString() {
        return "PlanningState{" +
                "agentRoom='" + agentRoom + '\'' +
                ", hasVacuum=" + hasVacuum +
                ", vacuumRoom='" + vacuumRoom + '\'' +
                ", doneTasks=" + doneTasks +
                ", pendingTasks=" + pendingTasks +
                ", dishesRooms=" + dishesRooms +
                '}';
    }
}