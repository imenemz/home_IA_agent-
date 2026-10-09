package agents;

import comm.CommunicationHub;
import comm.Mailbox;
import comm.Message;
import comm.MessageType;
import env.Action;
import env.ActionResult;
import env.Environment;
import env.Perception;
import env.Task;
import env.TaskState;
import org.Role;

public abstract class Agent {
    protected final String id;
    protected int energy;
    protected String role;
    protected String assignedTaskId = null;
    protected final Mailbox mailbox = new Mailbox();
    protected final CommunicationHub hub;

    public Agent(String id, int energy, String role, CommunicationHub hub) {
        this.id = id;
        this.energy = energy;
        this.role = role;
        this.hub = hub;
        hub.register(id, mailbox);
    }

    public String getId() {
        return id;
    }

    public Perception perceive(Environment env) {
        return env.perceive(id);
    }

    public abstract Action decide(Environment env, Perception p);

    public void step(Environment env) {
        if (energy <= 0) {
            System.out.println(id + " has no energy.");
            return;
        }

        Perception p = perceive(env);
        Action a = decide(env, p);

        if (a == null) {
            System.out.println(id + " does nothing.");
            return;
        }

        ActionResult r = env.tryAct(id, a);
        System.out.println(id + " -> " + a.getType() + " : " + r.getMessage());

        if (r.isSuccess() && assignedTaskId != null && isAssignedTaskDone(env)) {
            send("MANAGER", MessageType.INFORM_DONE, assignedTaskId);
            System.out.println(id + " informed MANAGER that task " + assignedTaskId + " is done.");
            assignedTaskId = null;
        }

        energy -= 1;
    }

    private boolean isAssignedTaskDone(Environment env) {
        if (assignedTaskId == null) {
            return false;
        }

        for (Task t : env.getAllTasks()) {
            if (t.getId().equals(assignedTaskId)) {
                return TaskState.DONE.equals(t.getState());
            }
        }

        return false;
    }

    public void assignTask(String taskId) {
        this.assignedTaskId = taskId;
    }

    public String getAssignedTaskId() {
        return assignedTaskId;
    }

    public int bidFor(Environment env, Task t) {
        boolean ok =
                (Role.KITCHEN_HELPER.equals(role) &&
                        (t.getType().contains("DISHES") || t.getType().contains("GARBAGE")))
                        || (Role.CLEANER.equals(role) && t.getType().contains("CLEAN"));

        if (!ok) return -1;

        String myRoom = env.getAgentLocation(id);
        return myRoom.equals(t.getRoom()) ? 0 : 1;
    }

    public void processMessages() {
        while (!mailbox.isEmpty()) {
            Message m = mailbox.poll();

            if (MessageType.ASSIGN.equals(m.getType())) {
                this.assignedTaskId = m.getContent();
                System.out.println(id + " received ASSIGN for task " + assignedTaskId);
            }
        }
    }

    protected void send(String receiverId, String type, String content) {
        hub.send(new Message(id, receiverId, type, content));
    }
}
