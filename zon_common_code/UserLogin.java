import java.util.*;

// https://www.reddit.com/r/leetcode/comments/1jj2xco/amazon_sde_intern_experience_got_the_offer/?utm_source=chatgpt.com

// Track user login attempts. 
// Identify the oldest user who has logged in only once.

// The interviewer was satisfied with the initial working solution.
// Then came the follow-up: "Can you optimize this?"
// I suggested using a Doubly Linked List + HashMap to track users who logged in only once, 
// in order — kind of like an LRU pattern. 
// That brought it down to near O(1) operations.
public class UserLogin {
    // map: key: user, value: list of login timestamp
    // pq: user, first timestamp the user logs in -> sort by the timestamp in increasing order
    Map<Integer, Integer> userLoginCount;
    PriorityQueue<int[]> pq;
    public UserLogin(){
        userLoginCount = new HashMap<>();
        pq = new PriorityQueue<>(
            (a,b)->a[1]-b[1]
        );
    }
    public void addNewLogin(int userId, int timestamp){
        if(!userLoginCount.containsKey(userId)) pq.offer(new int[]{userId, timestamp});
        userLoginCount.put(userId, userLoginCount.getOrDefault(userId,0)+1);
    }
    public int findOldestUserLogOnce(){
        while(!pq.isEmpty() && userLoginCount.get(pq.peek()[0]) > 1){
            pq.poll();
        }
        if(pq.isEmpty()) return -1;
        return pq.peek()[0];
    }
}

// import java.util.*;

// public class UserLogin {

//     private static class Node {
//         int userId;
//         Node prev;
//         Node next;

//         Node(int userId) {
//             this.userId = userId;
//         }
//     }

//     // userId -> login count
//     private final Map<Integer, Integer> loginCount;

//     // Only stores users who have logged in exactly once
//     private final Map<Integer, Node> userToNode;

//     private final Node head;
//     private final Node tail;

//     public UserLogin() {
//         loginCount = new HashMap<>();
//         userToNode = new HashMap<>();

//         head = new Node(-1);
//         tail = new Node(-1);

//         head.next = tail;
//         tail.prev = head;
//     }

//     public void addNewLogin(int userId, int timestamp) {
//         int count = loginCount.getOrDefault(userId, 0) + 1;
//         loginCount.put(userId, count);

//         if (count == 1) {
//             Node node = new Node(userId);
//             addLast(node);
//             userToNode.put(userId, node);
//         } else if (count == 2) {
//             Node node = userToNode.remove(userId);
//             remove(node);
//         }
//     }

//     public int findOldestUserLogOnce() {
//         return head.next == tail ? -1 : head.next.userId;
//     }

//     private void addLast(Node node) {
//         Node prev = tail.prev;

//         prev.next = node;
//         node.prev = prev;

//         node.next = tail;
//         tail.prev = node;
//     }

//     private void remove(Node node) {
//         node.prev.next = node.next;
//         node.next.prev = node.prev;
//     }
// }