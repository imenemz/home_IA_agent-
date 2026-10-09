package env;

public class Action {
    private final String type;
    private final String targetRoom;
    private final String objectType;
    private final String taskId;

    private Action(String type, String targetRoom, String objectType, String taskId) {
        this.type = type;
        this.targetRoom = targetRoom;
        this.objectType = objectType;
        this.taskId = taskId;
    }

    public static Action moveTo(String room) {
        return new Action(ActionType.MOVE, room, null, null);
    }

    public static Action pick(String obj) {
        return new Action(ActionType.PICK, null, obj, null);
    }

    public static Action drop(String obj) {
        return new Action(ActionType.DROP, null, obj, null);
    }

    public static Action washDishes(String taskId) {
        return new Action(ActionType.WASH_DISHES, null, null, taskId);
    }

    public static Action cleanRoom(String taskId) {
        return new Action(ActionType.CLEAN_ROOM, null, null, taskId);
    }

    public String getType() { return type; }
    public String getTargetRoom() { return targetRoom; }
    public String getObjectType() { return objectType; }
    public String getTaskId() { return taskId; }
}
