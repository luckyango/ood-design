import java.util.*;

// https://www.reddit.com/r/leetcode/comments/1jj2xco/amazon_sde_intern_experience_got_the_offer/?utm_source=chatgpt.com

// Track user login attempts. 
// Identify the oldest user who has logged in only once.

// The interviewer was satisfied with the initial working solution.
// Then came the follow-up: "Can you optimize this?"
// I suggested using a Doubly Linked List + HashMap to track users who logged in only once, 
// in order — kind of like an LRU pattern. 
// That brought it down to near O(1) operations.

public class UserLogin{
    // method2
    // use doubly linked list -> record the order user appears
    // Map<userId, node> -> find the node by O(1) so that we can operate the node
    // Map<userId, count> -> count=1, add a new node to the list, update the userToNode map, update the count; count = 2, remove the node,
    //                      update the count; count >= 3, update the count
    public static class Node {
        String userId; long timestamp; Node prev; Node next;
        public Node(String userId, long timestamp){
            this.userId = userId; this.timestamp = timestamp;
        }
    }
    Node head; Node tail;
    Map<String, Node> userToNode;
    Map<String, Integer> userToCount;
    public UserLogin(){
        head = new Node("",-1); tail = new Node("",-1); head.next=tail; tail.prev = head;
        userToNode = new HashMap<>(); userToCount = new HashMap<>();
    }
    public void addRecord(String userId, long timestamp){
        int count = userToCount.getOrDefault(userId, 0)+1;
        userToCount.put(userId, count);
        if(count == 1){
            Node node = new Node(userId,timestamp);
            addToTail(node);
            userToNode.put(userId, node);
        }else if(count == 2){
            Node removeNode = userToNode.get(userId);
            remove(removeNode);
            userToNode.remove(userId);
        }
    }
    public String oldestUserLoginOnce(){
        return head.next.userId;
    }
    public void addToTail(Node node){
        tail.prev.next = node;
        node.prev = tail.prev;
        node.next = tail;
        tail.prev = node;
    }
    public void remove(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    // method 1
    // record user with the latest login time
    // Map<String, Long> userFirstLogin = new HashMap<>();
    // Map<String, Integer> userLoginCount = new HashMap<>();
    // public void addRecord(String userId, long timestamp){
    //     if(!userFirstLogin.containsKey(userId)) userFirstLogin.put(userId, timestamp);
    //     userLoginCount.put(userId, userLoginCount.getOrDefault(userId,0)+1);
    // }
    // // O(n)
    // public String oldestUserLoginOnce(){
    //     String ans = null;
    //     long earliestTime = Long.MAX_VALUE;
    //     for(String userId: userFirstLogin.keySet()){
    //         if(userLoginCount.get(userId) == 1 && (ans==null || userFirstLogin.get(userId).compareTo(earliestTime) < 0)){
    //             ans = userId;
    //             earliestTime = userFirstLogin.get(userId);
    //         }
    //     }
    //     return ans;
    // }

}

// public class UserLogin {
//     // map: key: user, value: list of login timestamp
//     // pq: user, first timestamp the user logs in -> sort by the timestamp in increasing order
//     Map<Integer, Integer> userLoginCount;
//     PriorityQueue<int[]> pq;
//     public UserLogin(){
//         userLoginCount = new HashMap<>();
//         pq = new PriorityQueue<>(
//             (a,b)->a[1]-b[1]
//         );
//     }
//     public void addNewLogin(int userId, int timestamp){
//         if(!userLoginCount.containsKey(userId)) pq.offer(new int[]{userId, timestamp});
//         userLoginCount.put(userId, userLoginCount.getOrDefault(userId,0)+1);
//     }
//     public int findOldestUserLogOnce(){
//         while(!pq.isEmpty() && userLoginCount.get(pq.peek()[0]) > 1){
//             pq.poll();
//         }
//         if(pq.isEmpty()) return -1;
//         return pq.peek()[0];
//     }
// }

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