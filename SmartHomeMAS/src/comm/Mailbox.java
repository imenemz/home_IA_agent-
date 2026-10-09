package comm;

import java.util.LinkedList;
import java.util.Queue;

public class Mailbox {
    private final Queue<Message> queue = new LinkedList<>();

    public void push(Message m) {
        queue.add(m);
    }

    public Message poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
