# SmartHomeMAS — Smart Home Multi-Agent Simulation

A university project exploring how autonomous agents coordinate household tasks in a simulated smart home using Java.

The project combines reactive decision-making, a Belief–Desire–Intention (BDI) agent, task allocation, and automated planning.

## Overview

The simulation models three connected rooms: a kitchen, a living room, and a bathroom.

Two agents receive household tasks from a central task manager:

- **Reactive agent:** chooses actions from its current perception and assigned task.
- **BDI agent:** updates its beliefs, identifies a goal, and builds a plan to achieve it.

Agents can move between adjacent rooms, interact with objects, and report task completion through messages.

## Features

- A simulated home represented as a graph of connected rooms.
- Household tasks including washing dishes and cleaning a room.
- A reactive agent and a BDI agent.
- Greedy task allocation based on agent bids.
- Agent communication through a communication hub and mailboxes.
- Breadth-first search to generate action plans.
- Action validation, including room access, task ownership, and required equipment.
- Console logs showing assignments, actions, plans, and task states.

## BDI Reasoning

The BDI agent separates its reasoning into three elements:

- **Beliefs:** its knowledge of its location, perceived objects, and equipment.
- **Desires:** the household task it aims to complete.
- **Intentions:** the action plan selected to achieve that goal.

For a cleaning task, a plan can involve moving to the vacuum, picking it up, moving to the target room, and cleaning.

## Technologies

- Java
- Java Collections Framework
- Object-oriented programming
- Multi-agent systems
- BDI architecture
- Breadth-first search
- Message-based coordination

The source uses the Java standard library and does not require external libraries.

## Source Structure

The source code is located in `SmartHomeMAS/src/`.

| File or package | Purpose |
| --- | --- |
| `Main.java` | Creates and runs the demonstration scenario |
| `agents/` | Base agent, reactive agent, and BDI agent |
| `bdi/` | Belief, desire, and intention representations |
| `comm/` | Messages, mailboxes, and communication hub |
| `env/` | Rooms, objects, tasks, perceptions, and actions |
| `org/` | Agent roles and task allocation |
| `planning/` | Planning states, actions, goals, and search |

## How to Run

Use a JDK such as **Java 17**, with both `java` and `javac` available in your terminal.

Clone the repository:

```powershell
git clone https://github.com/imenemz/home_IA_agent-.git
cd home_IA_agent-
```

Compile the application from the repository root:

```powershell
javac -d out -sourcepath SmartHomeMAS/src SmartHomeMAS/src/Main.java
```

Run the simulation:

```powershell
java -cp out Main
```

## Demonstration Scenario

The default scenario in `Main.java` contains:

- A reactive kitchen-helper agent starting in the bathroom.
- A BDI cleaning agent starting in the kitchen.
- Dishes in the kitchen.
- A vacuum in the living room.
- A dishwashing task and a living-room cleaning task.

The simulation runs for eight ticks and prints agent locations, task assignments, planning information, action results, and task states.

## Scope and Limitations

This is an educational console simulation. Rooms, objects, agents, and tasks are configured directly in the code.

The reactive agent uses simple movement rules and does not compute a complete route. Room cleaning is handled by the BDI agent, which can plan to obtain the required vacuum.

The project does not currently include a graphical interface, physical device integration, or automated tests.

## Possible Improvements

- Add more household tasks and agent roles.
- Improve reactive-agent navigation.
- Compare task-allocation strategies.
- Add automated tests for planning and action validation.
- Load simulation scenarios from configuration files.
- Add a visual interface for observing agents and tasks.
