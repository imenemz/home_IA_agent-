package planning;

import java.util.LinkedList;
import java.util.List;

public class Plan {

    private final LinkedList<PlanningAction> actions;

    public Plan(List<PlanningAction> actions) {
        this.actions = new LinkedList<>(actions);
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public PlanningAction next() {
        return actions.poll();
    }

    @Override
    public String toString() {
        return actions.toString();
    }
}