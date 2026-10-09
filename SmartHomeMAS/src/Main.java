import agents.Agent;
import agents.BDIAgent;
import agents.ReactiveAgent;
import comm.CommunicationHub;
import env.Environment;
import env.ObjectType;
import env.Task;
import env.TaskType;
import org.AllocationStrategy;
import org.GreedyStrategy;
import org.Role;
import org.TaskManager;

import java.util.List;

public class Main {

    private static void printAgentStates(Environment env, List<Agent> agents) {
        System.out.println("Agents:");
        for (Agent a : agents) {
            String room = env.getAgentLocation(a.getId());
            String assigned = a.getAssignedTaskId();
            System.out.println(" - " + a.getId() + " in " + room + " | assigned=" + assigned);
        }
    }

    private static void printTaskStates(Environment env) {
        System.out.println("Tasks:");
        for (Task t : env.getAllTasks()) {
            System.out.println(" - " + t.getId() + " " + t.getType() + " in " + t.getRoom()
                    + " | state=" + t.getState());
        }
    }

    public static void main(String[] args) {

        CommunicationHub hub = new CommunicationHub();
        Environment env = new Environment();

        env.addRoom("Kitchen");
        env.addRoom("LivingRoom");
        env.addRoom("Bathroom");

        env.connectRooms("Kitchen", "LivingRoom");
        env.connectRooms("LivingRoom", "Bathroom");

        env.addObject("Kitchen", ObjectType.DISHES);
        env.addObject("Kitchen", ObjectType.TRASH_BIN);
        env.addObject("LivingRoom", ObjectType.VACUUM);

        env.addTask(new Task("T3", TaskType.WASH_DISHES, "Kitchen"));
        env.addTask(new Task("T4", TaskType.CLEAN_ROOM, "LivingRoom"));

        AllocationStrategy strategy = new GreedyStrategy();
        TaskManager manager = new TaskManager(hub, strategy);

        Agent r1 = new ReactiveAgent("R1", 10, Role.KITCHEN_HELPER, hub);
        Agent b1 = new BDIAgent("B1", 10, Role.CLEANER, hub);

        env.setAgentLocation("R1", "Bathroom");
        env.setAgentLocation("B1", "Kitchen");

        List<Agent> agents = List.of(r1, b1);

        for (int tick = 1; tick <= 8; tick++) {
            System.out.println("\n=== TICK " + tick + " ===");

            printAgentStates(env, agents);
            printTaskStates(env);

            manager.processMessages();
            manager.allocate(env, agents);

            for (Agent a : agents) {
                a.processMessages();
            }

            for (Agent a : agents) {
                a.step(env);
            }

            System.out.println("End of tick " + tick + ":");
            printAgentStates(env, agents);
            printTaskStates(env);
        }

        System.out.println("\nSimulation finished.");
    }
}