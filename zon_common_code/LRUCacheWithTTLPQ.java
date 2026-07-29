import java.util.*;

public class LRUCacheWithTTLPQ {
    class Node {
        int key, value;
        long expiredAt;
        Node prev, next;

        Node(int key, int value, long expiredAt) {
            this.key = key;
            this.value = value;
            this.expiredAt = expiredAt;
        }
    }

    class Expiry {
        Node node;
        long expiredAt;

        Expiry(Node node) {
            this.node = node;
            this.expiredAt = node.expiredAt;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final PriorityQueue<Expiry> pq =
        new PriorityQueue<>((a, b) ->
            Long.compare(a.expiredAt, b.expiredAt));

    private final Node head = new Node(-1, -1, 0);
    private final Node tail = new Node(-1, -1, 0);

    public LRUCacheWithTTLPQ(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        Node node = map.get(key);

        if (node == null) {
            return -1;
        }

        if (node.expiredAt <= System.currentTimeMillis()) {
            removeNode(node);
            return -1;
        }

        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value, long ttl) {
        if (capacity == 0) {
            return;
        }

        long now = System.currentTimeMillis();
        Node node = map.get(key);

        if (node != null) {
            if (node.expiredAt <= now) {
                removeNode(node);
            } else {
                node.value = value;
                node.expiredAt = now + ttl;
                moveToHead(node);
                pq.offer(new Expiry(node));
                return;
            }
        }

        removeExpired(now);

        if (map.size() == capacity) {
            removeNode(tail.prev);
        }

        Node newNode = new Node(key, value, now + ttl);
        map.put(key, newNode);
        addToHead(newNode);
        pq.offer(new Expiry(newNode));
    }

    private void removeExpired(long now) {
        while (!pq.isEmpty() && pq.peek().expiredAt <= now) {
            Expiry expiry = pq.poll();
            Node node = expiry.node;

            // Node was removed/replaced, or its TTL was updated.
            if (map.get(node.key) != node ||
                node.expiredAt != expiry.expiredAt) {
                continue;
            }

            removeNode(node);
        }
    }

    private void removeNode(Node node) {
        map.remove(node.key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
}