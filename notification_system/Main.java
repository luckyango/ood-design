import java.util.*;

// ===== 1. Channel =====
enum Channel {
    SMS, EMAIL
}

// ===== 2. Notification =====
class Notification {
    private final Channel channel;
    private final String recipient;
    private final String content;

    public Notification(Channel channel, String recipient, String content) {
        this.channel = channel;
        this.recipient = recipient;
        this.content = content;
    }

    public Channel getChannel() { return channel; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
}

// ===== 3. Strategy =====
interface NotificationSender {
    Channel getChannel();
    void send(Notification notification);
}

// ===== 4. Concrete Strategy =====
class SmsSender implements NotificationSender {
    public Channel getChannel() {
        return Channel.SMS;
    }
    public void send(Notification notification) {
        System.out.println("Sending SMS to " + notification.getRecipient());
    }
}

class EmailSender implements NotificationSender {
    public Channel getChannel() {
        return Channel.EMAIL;
    }
    public void send(Notification notification) {
        System.out.println("Sending EMAIL to " + notification.getRecipient());
    }
}

// ===== 5. Factory（核心）=====
class NotificationSenderFactory {
    private final Map<Channel, NotificationSender> senderMap = new HashMap<>();

    public NotificationSenderFactory(List<NotificationSender> senders) {
        for(NotificationSender sender: senders){
            senderMap.put(sender.getChannel(), sender);
        }
    }

    public NotificationSender getSender(Channel channel) {
        return senderMap.get(channel);
    }
}

// ===== 6. Dispatcher =====
class NotificationDispatcher {
    private final NotificationSenderFactory factory;

    public NotificationDispatcher(NotificationSenderFactory factory) {
        this.factory = factory;
    }

    public void dispatch(Notification notification) {
        NotificationSender sender = factory.getSender(notification.getChannel());
        if (sender == null) {
            throw new RuntimeException("Unsupported channel");
        }
        sender.send(notification);
    }
}

// ===== 7. Main =====
public class Main {
    public static void main(String[] args) {
        List<NotificationSender> senders = Arrays.asList(new SmsSender(), new EmailSender());
        NotificationSenderFactory senderFactory = new NotificationSenderFactory(senders);
        NotificationDispatcher dispatcher = new NotificationDispatcher(senderFactory);
        Notification notification = new Notification(Channel.SMS, "91934545", "your package has been delivered");
        dispatcher.dispatch(notification);
    }
}
