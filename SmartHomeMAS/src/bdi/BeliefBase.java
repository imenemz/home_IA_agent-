package bdi;

import java.util.HashSet;
import java.util.Set;

public class BeliefBase {
    public String currentRoom;
    public Set<String> objectsHere = new HashSet<>();
    public int energy;
    public boolean hasVacuum = false;
}
