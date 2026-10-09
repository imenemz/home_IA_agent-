package bdi;

public class Intention {
    private final String description;
    private final String targetTaskId;

    public Intention(String description, String targetTaskId) {
        this.description = description;
        this.targetTaskId = targetTaskId;
    }

    public String getDescription() {
        return description;
    }

    public String getTargetTaskId() {
        return targetTaskId;
    }

    @Override
    public String toString() {
        return "Intention{" +
                "description='" + description + '\'' +
                ", targetTaskId='" + targetTaskId + '\'' +
                '}';
    }
}