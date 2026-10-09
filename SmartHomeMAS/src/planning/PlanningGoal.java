package planning;

public class PlanningGoal {

    private final String targetTaskId;

    public PlanningGoal(String targetTaskId) {
        this.targetTaskId = targetTaskId;
    }

    public String getTargetTaskId() {
        return targetTaskId;
    }

    @Override
    public String toString() {
        return "taskDone(" + targetTaskId + ")";
    }
}