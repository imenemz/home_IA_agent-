package org;

public class Bid {
    private final String agentId;
    private final String taskId;
    private final int cost;

    public Bid(String agentId, String taskId, int cost) {
        this.agentId = agentId;
        this.taskId = taskId;
        this.cost = cost;
    }

    public String getAgentId() { return agentId; }
    public String getTaskId() { return taskId; }
    public int getCost() { return cost; }
}
