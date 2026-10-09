package env;

import java.util.*;

public class Environment {
    private final Set<String> rooms = new HashSet<>();
    private final Map<String, Set<String>> adjacency = new HashMap<>();
    private final Map<String, Set<String>> objectsInRoom = new HashMap<>();
    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, String> agentLocations = new HashMap<>();
    private final Map<String, Set<String>> inventoryByAgent = new HashMap<>();
    private final List<TaskListener> listeners = new ArrayList<>();

    private Set<String> inventoryOf(String agentId) {
        return inventoryByAgent.computeIfAbsent(agentId, k -> new HashSet<>());
    }

    private Set<String> objectsIn(String room) {
        return objectsInRoom.computeIfAbsent(room, k -> new HashSet<>());
    }

    public void addRoom(String room) {
        rooms.add(room);
        adjacency.putIfAbsent(room, new HashSet<>());
        objectsInRoom.putIfAbsent(room, new HashSet<>());
    }

    public void connectRooms(String a, String b) {
        adjacency.get(a).add(b);
        adjacency.get(b).add(a);
    }

    public Set<String> getNeighbors(String room) {
        return adjacency.getOrDefault(room, Set.of());
    }

    public Set<String> getRooms() {
        return new HashSet<>(rooms);
    }

    public void setAgentLocation(String agentId, String room) {
        agentLocations.put(agentId, room);
    }

    public String getAgentLocation(String agentId) {
        return agentLocations.get(agentId);
    }

    public void addObject(String room, String object) {
        objectsIn(room).add(object);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task getTask(String id) {
        for (Task t : tasks) if (t.getId().equals(id)) return t;
        return null;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void addTaskListener(TaskListener listener) {
        listeners.add(listener);
    }

    private void notifyTaskUpdated(Task task) {
        for (TaskListener listener : listeners) {
            listener.onTaskCompleted(task);
        }
    }

    public Set<String> getObjects(String room) {
        return new HashSet<>(objectsIn(room));
    }

    public ActionResult tryAct(String agentId, Action action) {
        String currentRoom = agentLocations.get(agentId);
        if (currentRoom == null) {
            return ActionResult.fail("Agent has no location.");
        }

        String actionType = action.getType();

        if (ActionType.MOVE.equals(actionType)) {
            String target = action.getTargetRoom();
            if (target == null || !adjacency.getOrDefault(currentRoom, Set.of()).contains(target)) {
                return ActionResult.fail("Cannot move: room not adjacent.");
            }
            agentLocations.put(agentId, target);
            return ActionResult.ok("Moved to " + target);
        }

        if (ActionType.PICK.equals(actionType)) {
            String obj = action.getObjectType();
            if (obj == null || !objectsIn(currentRoom).contains(obj)) {
                return ActionResult.fail("Object not in room.");
            }
            objectsIn(currentRoom).remove(obj);
            inventoryOf(agentId).add(obj);
            return ActionResult.ok("Picked " + obj);
        }

        if (ActionType.DROP.equals(actionType)) {
            String obj = action.getObjectType();
            if (obj == null || !inventoryOf(agentId).contains(obj)) {
                return ActionResult.fail("Object not in inventory.");
            }
            inventoryOf(agentId).remove(obj);
            objectsIn(currentRoom).add(obj);
            return ActionResult.ok("Dropped " + obj);
        }

        if (ActionType.WASH_DISHES.equals(actionType)) {
            Task t = getTask(action.getTaskId());
            if (t == null) return ActionResult.fail("Unknown task.");
            if (!agentId.equals(t.getAssignedTo())) return ActionResult.fail("Task assigned to another agent.");
            if (!TaskType.WASH_DISHES.equals(t.getType())) return ActionResult.fail("Task is not WASH_DISHES.");
            if (!currentRoom.equals(t.getRoom())) return ActionResult.fail("Not in task room.");
            if (TaskState.DONE.equals(t.getState())) return ActionResult.fail("Task already done.");
            if (!objectsIn(currentRoom).contains(ObjectType.DISHES)) return ActionResult.fail("No dishes here.");
            if (!t.dependenciesSatisfied(this)) return ActionResult.fail("Dependencies not satisfied.");

            t.setState(TaskState.DONE);
            objectsInRoom.get(currentRoom).remove(ObjectType.DISHES);
            notifyTaskUpdated(t);
            return ActionResult.ok("Dishes washed.");
        }

        if (ActionType.CLEAN_ROOM.equals(actionType)) {
            Task t = getTask(action.getTaskId());
            if (t == null) return ActionResult.fail("Unknown task.");
            if (!agentId.equals(t.getAssignedTo())) return ActionResult.fail("Task assigned to another agent.");
            if (!TaskType.CLEAN_ROOM.equals(t.getType())) return ActionResult.fail("Task is not CLEAN_ROOM.");
            if (!currentRoom.equals(t.getRoom())) return ActionResult.fail("Not in task room.");
            if (TaskState.DONE.equals(t.getState())) return ActionResult.fail("Task already done.");
            if (!inventoryOf(agentId).contains(ObjectType.VACUUM)) return ActionResult.fail("Need VACUUM in inventory.");

            t.setState(TaskState.DONE);
            notifyTaskUpdated(t);
            return ActionResult.ok("Room cleaned.");
        }

        return ActionResult.fail("Unknown action.");
    }

    public Perception perceive(String agentId) {
        String room = agentLocations.get(agentId);
        Set<String> objectsHere = new HashSet<>(objectsInRoom.get(room));
        List<Task> tasksHere = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getRoom().equals(room)) {
                tasksHere.add(t);
            }
        }
        Set<String> neighbors = new HashSet<>(adjacency.get(room));
        return new Perception(room, objectsHere, tasksHere, neighbors);
    }
}
