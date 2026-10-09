package org;

import agents.Agent;
import env.Environment;
import env.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GreedyStrategy implements AllocationStrategy {

    @Override
    public Optional<Bid> chooseWinner(Environment env, Task task, List<Agent> agents) {
        return agents.stream()
                .map(a -> new Bid(a.getId(), task.getId(), a.bidFor(env, task)))
                .filter(b -> b.getCost() >= 0)
                .min(Comparator.comparingInt(Bid::getCost));
    }
}
