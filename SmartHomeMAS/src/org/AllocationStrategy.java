package org;

import agents.Agent;
import env.Environment;
import env.Task;

import java.util.List;
import java.util.Optional;

public interface AllocationStrategy {
    Optional<Bid> chooseWinner(Environment env, Task task, List<Agent> agents);
}
