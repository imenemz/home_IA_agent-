package comm;

import java.util.HashMap;
import java.util.Map;

public class CommunicationHub {

    private final Map<String, Mailbox> mailboxes = new HashMap<>();

    public void register(String agentId, Mailbox mailbox) {
        mailboxes.put(agentId, mailbox);
    }

    public void send(Message m) {
        Mailbox box = mailboxes.get(m.getReceiverId());
        if (box != null) {
            box.push(m);
        }
    }
}
