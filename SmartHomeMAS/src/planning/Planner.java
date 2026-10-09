package planning;

import env.Environment;
import env.TaskType;

import java.util.*;

public class Planner {

    public static Plan plan(Environment env, PlanningState initial, PlanningProblem problem) {
        Queue<Node> open = new LinkedList<>();
        Set<PlanningState> closed = new HashSet<>();

        open.add(new Node(initial, new ArrayList<>()));

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (isGoal(current.state, problem)) {
                return new Plan(current.plan);
            }

            if (closed.contains(current.state)) {
                continue;
            }
            closed.add(current.state);

            List<PlanningAction> applicable = getApplicableActions(env, current.state, problem);

            for (PlanningAction action : applicable) {
                PlanningState nextState = apply(action, current.state, problem);

                if (closed.contains(nextState)) {
                    continue;
                }

                List<PlanningAction> nextPlan = new ArrayList<>(current.plan);
                nextPlan.add(action);

                open.add(new Node(nextState, nextPlan));
            }
        }

        return new Plan(Collections.emptyList());
    }

    private static boolean isGoal(PlanningState state, PlanningProblem problem) {
        if (problem.getGoal() == null) return false;
        return state.isTaskDone(problem.getGoal().getTargetTaskId());
    }

    private static List<PlanningAction> getApplicableActions(Environment env,
                                                             PlanningState state,
                                                             PlanningProblem problem) {
        List<PlanningAction> actions = new ArrayList<>();

        for (String neighbor : env.getNeighbors(state.getAgentRoom())) {
            if (canMove(state, neighbor)) {
                actions.add(new PlanningAction(PlanningAction.Type.MOVE, neighbor));
            }
        }

        if (canPickVacuum(state)) {
            actions.add(new PlanningAction(PlanningAction.Type.PICK_VACUUM, null));
        }

        for (String taskId : state.getPendingTasks()) {
            String type = problem.getTaskType(taskId);
            String taskRoom = problem.getTaskRoom(taskId);

            if (type == null || taskRoom == null) {
                continue;
            }

            if (TaskType.WASH_DISHES.equals(type) && canWashDishes(state, taskId, taskRoom)) {
                actions.add(new PlanningAction(PlanningAction.Type.WASH_DISHES, taskId));
            }

            if (TaskType.CLEAN_ROOM.equals(type) && canCleanRoom(state, taskId, taskRoom)) {
                actions.add(new PlanningAction(PlanningAction.Type.CLEAN_ROOM, taskId));
            }
        }

        return actions;
    }

    private static boolean canMove(PlanningState state, String toRoom) {
        return toRoom != null && !toRoom.equals(state.getAgentRoom());
    }

    private static boolean canPickVacuum(PlanningState state) {
        return !state.hasVacuum()
                && state.getVacuumRoom() != null
                && state.getAgentRoom().equals(state.getVacuumRoom());
    }

    private static boolean canWashDishes(PlanningState state, String taskId, String taskRoom) {
        return state.isTaskPending(taskId)
                && state.getAgentRoom().equals(taskRoom)
                && state.hasDishesInRoom(taskRoom);
    }

    private static boolean canCleanRoom(PlanningState state, String taskId, String taskRoom) {
        return state.isTaskPending(taskId)
                && state.getAgentRoom().equals(taskRoom)
                && state.hasVacuum();
    }

    private static PlanningState apply(PlanningAction action,
                                       PlanningState state,
                                       PlanningProblem problem) {
        PlanningState next = new PlanningState(state);

        if (PlanningAction.Type.MOVE.equals(action.getType())) {
            next.setAgentRoom(action.getTarget());
        } else if (PlanningAction.Type.PICK_VACUUM.equals(action.getType())) {
            next.setHasVacuum(true);
            next.setVacuumRoom(null);
        } else if (PlanningAction.Type.WASH_DISHES.equals(action.getType())) {
            String taskId = action.getTarget();
            String room = problem.getTaskRoom(taskId);
            next.markTaskDone(taskId);
            if (room != null) {
                next.removeDishesFromRoom(room);
            }
        } else if (PlanningAction.Type.CLEAN_ROOM.equals(action.getType())) {
            next.markTaskDone(action.getTarget());
        }

        return next;
    }

    private static class Node {
        private final PlanningState state;
        private final List<PlanningAction> plan;

        private Node(PlanningState state, List<PlanningAction> plan) {
            this.state = state;
            this.plan = plan;
        }
    }
}
