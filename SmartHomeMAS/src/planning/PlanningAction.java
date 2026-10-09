package planning;

public class PlanningAction {

    public static final class Type {
        private Type() {}

        public static final String MOVE = "MOVE";
        public static final String PICK_VACUUM = "PICK_VACUUM";
        public static final String WASH_DISHES = "WASH_DISHES";
        public static final String CLEAN_ROOM = "CLEAN_ROOM";
    }

    private final String type;
    private final String target;

    public PlanningAction(String type, String target) {
        this.type = type;
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return type + (target != null ? "(" + target + ")" : "");
    }
}
