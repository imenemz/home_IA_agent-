package org;

import agents.Agent;
import comm.CommunicationHub;
import comm.Mailbox;
import comm.Message;
import comm.MessageType;
import env.Environment;
import env.Task;
import env.TaskState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

    private final String id = "MANAGER";
    private final Mailbox mailbox = new Mailbox();
    private final CommunicationHub hub;

    private final Map<String, String> assignments = new HashMap<>();
    private final AllocationStrategy strategy;

    public TaskManager(CommunicationHub hub, AllocationStrategy strategy) {
        this.hub = hub;
        this.strategy = strategy;
        hub.register(id, mailbox);
    }

    public void processMessages() {
        while (!mailbox.isEmpty()) {
            Message m = mailbox.poll();
            if (MessageType.INFORM_DONE.equals(m.getType())) {
                String taskId = m.getContent();
                assignments.remove(taskId);
                System.out.println("MANAGER received DONE for task " + taskId + " from " + m.getSenderId());
            }
        }
    }

    public void allocate(Environment env, List<Agent> agents) {
        for (Task t : env.getAllTasks()) {
            if (!TaskState.PENDING.equals(t.getState())) continue;
            if (assignments.containsKey(t.getId())) continue;

            strategy.chooseWinner(env, t, agents).ifPresent(best -> {
                assignments.put(t.getId(), best.getAgentId());
                t.setAssignedTo(best.getAgentId());
                hub.send(new Message("MANAGER", best.getAgentId(), MessageType.ASSIGN, best.getTaskId()));

                System.out.println("MANAGER assigned " + t.getId() + " to " + best.getAgentId()
                        + " (cost=" + best.getCost() + ")");
            });
        }
    }
}
