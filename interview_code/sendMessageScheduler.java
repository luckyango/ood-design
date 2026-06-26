// Design a messaging system that allows companies to schedule messages
// to be sent to their subscribers at specific times.

// Requirements:
// 1. Each company has a list of subscribers (phone numbers).
// 2. Companies can schedule messages with a send time.
// 3. The system automatically sends messages at the scheduled time.
// 4. Each subscriber should receive each message only once.
// 5. Multiple companies may share subscribers.
// 6. The system should support multiple scheduled messages.

// Example:
// Crocs and Aeropostale both have overlapping subscribers.
// Messages should be delivered correctly without duplication.
import java.util.*;
import java.util.concurrent.*;

// 1. 定义订阅者（虽然目前只是手机号，但封装类更易扩展）
class Subscriber {
    private final String phoneNumber;

    public Subscriber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() { return phoneNumber; }
}

// 2. 定义公司/品牌
class Company {
    private final String name;
    private final Set<Subscriber> subscribers = new HashSet<>();

    public Company(String name) { this.name = name; }

    public void addSubscriber(Subscriber s) { subscribers.add(s); }
    public Set<Subscriber> getSubscribers() { return subscribers; }
    public String getName() { return name; }
}

// 3. 定义消息任务
class MessageTask {
    private final Company sender;
    private final String content;
    private final long scheduledTime; // 时间戳形式

    public MessageTask(Company sender, String content, long scheduledTime) {
        this.sender = sender;
        this.content = content;
        this.scheduledTime = scheduledTime;
    }

    public void send() {
        Set<Subscriber> targets = sender.getSubscribers();
        for (Subscriber s : targets) {
            // 模拟发送逻辑
            System.out.printf("[Sending to %s from %s]: %s at %d\n", 
                s.getPhoneNumber(), sender.getName(), content, System.currentTimeMillis());
        }
    }

    public long getScheduledTime() { return scheduledTime; }
}

// 4. 核心调度系统
class MessageScheduler {
    // 使用优先队列按时间排序任务
    private final PriorityQueue<MessageTask> taskQueue = 
        new PriorityQueue<>(Comparator.comparingLong(MessageTask::getScheduledTime));
    
    // 模拟发送服务（生产环境通常接第三方接口如 Twilio）
    public void schedule(MessageTask task) {
        taskQueue.offer(task);
    }

    // 模拟运行循环
    public void run() {
        while (!taskQueue.isEmpty()) {
            MessageTask nextTask = taskQueue.peek();
            long now = System.currentTimeMillis();

            if (now >= nextTask.getScheduledTime()) {
                taskQueue.poll().send();
            } else {
                try {
                    // 等待直到下一个任务时间
                    Thread.sleep(nextTask.getScheduledTime() - now);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

// 5. 测试主类
public class sendMessageScheduler {
    public static void main(String[] args) {
        MessageScheduler scheduler = new MessageScheduler();

        // 初始化数据
        Company crocs = new Company("Crocs");
        crocs.addSubscriber(new Subscriber("+1555555555"));
        crocs.addSubscriber(new Subscriber("+1555666666"));

        Company aero = new Company("Aeropostale");
        aero.addSubscriber(new Subscriber("+1555555555")); // 两个品牌共有的订阅者

        // 安排任务（示例用当前时间加偏移量模拟时间）
        long now = System.currentTimeMillis();
        scheduler.schedule(new MessageTask(crocs, "Hello! We are Crocs!", now + 1000));
        scheduler.schedule(new MessageTask(aero, "Buy our clothes!", now + 2000));

        scheduler.run();
    }
}
