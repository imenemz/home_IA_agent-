package agents;

import comm.CommunicationHub;
import env.Action;
import env.Environment;
import env.Perception;
import env.Task;
import env.TaskState;
import env.TaskType;

public class ReactiveAgent extends Agent {

    public ReactiveAgent(String id, int energy, String role, CommunicationHub hub) {
        super(id, energy, role, hub);
    }

    @Override
    public Action decide(Environment env, Perception p) {
        if (assignedTaskId == null) {
            return null;
        }

        Task task = env.getTask(assignedTaskId);

        if (task == null || TaskState.DONE.equals(task.getState())) {
            assignedTaskId = null;
            return null;
        }

        if (p.getCurrentRoom().equals(task.getRoom())) {
            if (TaskType.WASH_DISHES.equals(task.getType())) {
                return Action.washDishes(task.getId());
            }
            if (TaskType.CLEAN_ROOM.equals(task.getType())) {
                return null;
            }
            return null;
        }

        if (p.getNeighbors().contains(task.getRoom())) {
            return Action.moveTo(task.getRoom());
        }

        for (String neighbor : p.getNeighbors()) {
            return Action.moveTo(neighbor);
        }

        return null;
    }
}
