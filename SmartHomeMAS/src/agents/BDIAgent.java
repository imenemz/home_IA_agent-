package agents;

import bdi.BeliefBase;
import bdi.Desire;
import comm.CommunicationHub;
import env.Action;
import env.Environment;
import env.ObjectType;
import env.Perception;
import env.Task;
import env.TaskState;
import env.TaskType;
import planning.Plan;
import planning.Planner;
import planning.PlanningAction;
import planning.PlanningGoal;
import planning.PlanningProblem;
import planning.PlanningState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BDIAgent extends Agent {

    private final BeliefBase beliefs = new BeliefBase();
    private String desire = Desire.NONE;
    private PlanningGoal currentGoal = null;
    private Plan currentIntention = new Plan(Collections.emptyList());
    private String intentionTaskId = null;

    public BDIAgent(String id, int energy, String role, CommunicationHub hub) {
        super(id, energy, role, hub);
    }

    @Override
    public Action decide(Environment env, Perception p) {
        if (assignedTaskId == null) {
            clearDeliberation();
            return null;
        }

        Task task = env.getTask(assignedTaskId);
        if (task == null || TaskState.DONE.equals(task.getState())) {
            assignedTaskId = null;
            clearDeliberation();
            return null;
        }

        updateBeliefs(p);
        updateDesireFromTask(task);
        currentGoal = deriveGoalFromAssignedTask(task);

        if (!hasValidIntention(env, task)) {
            currentIntention = buildPlanForTaskGoal(env, p, task, currentGoal);
            intentionTaskId = task.getId();

            System.out.println(id + " beliefs: room=" + beliefs.currentRoom
                    + ", objects=" + beliefs.objectsHere
                    + ", hasVacuum=" + beliefs.hasVacuum);
            System.out.println(id + " desire: " + desire);
            System.out.println(id + " goal: " + currentGoal);
            System.out.println(id + " intention: " + currentIntention);
        }

        if (currentIntention == null || currentIntention.isEmpty()) {
            return null;
        }

        PlanningAction next = currentIntention.next();

        if (next != null && PlanningAction.Type.PICK_VACUUM.equals(next.getType())) {
            beliefs.hasVacuum = true;
        }

        return toEnvAction(next);
    }

    private void updateBeliefs(Perception p) {
        beliefs.currentRoom = p.getCurrentRoom();
        beliefs.objectsHere.clear();
        beliefs.objectsHere.addAll(p.getObjectsHere());
        beliefs.energy = energy;
    }

    private void updateDesireFromTask(Task task) {
        if (TaskType.WASH_DISHES.equals(task.getType())) {
            desire = Desire.WASH_DISHES;
        } else if (TaskType.CLEAN_ROOM.equals(task.getType())) {
            desire = Desire.CLEAN_ROOM;
        } else {
            desire = Desire.NONE;
        }
    }

    private PlanningGoal deriveGoalFromAssignedTask(Task task) {
        return new PlanningGoal(task.getId());
    }

    private boolean hasValidIntention(Environment env, Task task) {
        if (currentIntention == null || currentIntention.isEmpty()) return false;
        if (currentGoal == null) return false;
        if (intentionTaskId == null || !intentionTaskId.equals(task.getId())) return false;
        if (TaskState.DONE.equals(task.getState())) return false;

        if (TaskType.WASH_DISHES.equals(task.getType())) {
            return env.getObjects(task.getRoom()).contains(ObjectType.DISHES);
        }

        if (TaskType.CLEAN_ROOM.equals(task.getType())) {
            boolean vacuumInSomeRoom = false;
            for (String room : env.getRooms()) {
                if (env.getObjects(room).contains(ObjectType.VACUUM)) {
                    vacuumInSomeRoom = true;
                    break;
                }
            }
            return vacuumInSomeRoom || beliefs.hasVacuum;
        }

        return true;
    }

    private Plan buildPlanForTaskGoal(Environment env, Perception p, Task task, PlanningGoal goal) {
        Set<String> doneTasks = new HashSet<>();
        Set<String> pendingTasks = new HashSet<>();
        pendingTasks.add(task.getId());

        Set<String> dishesRooms = new HashSet<>();
        for (String room : env.getRooms()) {
            if (env.getObjects(room).contains(ObjectType.DISHES)) {
                dishesRooms.add(room);
            }
        }

        String vacuumRoom = null;
        if (!beliefs.hasVacuum) {
            for (String room : env.getRooms()) {
                if (env.getObjects(room).contains(ObjectType.VACUUM)) {
                    vacuumRoom = room;
                    break;
                }
            }
        }

        PlanningState initial = new PlanningState(
                p.getCurrentRoom(),
                beliefs.hasVacuum,
                vacuumRoom,
                doneTasks,
                pendingTasks,
                dishesRooms
        );

        PlanningProblem problem = new PlanningProblem();
        problem.addTask(task.getId(), task.getRoom(), task.getType());
        problem.setGoal(goal);

        return Planner.plan(env, initial, problem);
    }

    private Action toEnvAction(PlanningAction action) {
        if (action == null) return null;

        if (PlanningAction.Type.MOVE.equals(action.getType())) {
            return Action.moveTo(action.getTarget());
        }
        if (PlanningAction.Type.PICK_VACUUM.equals(action.getType())) {
            return Action.pick(ObjectType.VACUUM);
        }
        if (PlanningAction.Type.WASH_DISHES.equals(action.getType())) {
            return Action.washDishes(action.getTarget());
        }
        if (PlanningAction.Type.CLEAN_ROOM.equals(action.getType())) {
            return Action.cleanRoom(action.getTarget());
        }
        return null;
    }

    private void clearDeliberation() {
        desire = Desire.NONE;
        currentGoal = null;
        currentIntention = new Plan(Collections.emptyList());
        intentionTaskId = null;
    }
}
