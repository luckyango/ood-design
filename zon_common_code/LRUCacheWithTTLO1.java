import java.util.*;
public class LRUCacheWithTTLO1 {
    
// v1: normal LRUCache
// v2: add TTL
// v3: add priority rank
    // 1. the system should store the key, value pair -> use could get the value using the key
    // 2. there is a capacity limit for the system
    // 2a. if the current size exceeds the capacity -> kick out/remove the least recently used entry
    // 3.constraints on time complexity -> get & put: O(1)
    
    // solution:
    // 1.use a node class to store the pair information. key, value, expiration time
    // 2.use a map to achieve O(1) look up - <key:key of the node, value: node>
    // 3.use a doubly linked list to achieve O(1) add/remove: add the most recently used node
    //  at the beginning of the list; remove the least recently used node at the end if exceed


    // methods in the class
    // 1. add a new key val pair, if already exists -> update the value & check the expired time.
    // mark the node as the most recently used after making change
    // if the size exceeds the capacity -> evict the least recently used cache 
    // then add the new one
    // 2. find a val using a key. if the key exists & not expired, find the value and update the position
    // if not exists, return false;
    class Node{
        int key; int val; Node prev; Node next; long expired_at;
        public Node(int key, int val, long ttl){
            this.key = key; this.val = val; prev=null;next=null;
            expired_at = System.currentTimeMillis() + ttl;
        }
    }
    Map<Integer,Node> map;
    Node head; Node tail;
    int capacity;
    public LRUCacheWithTTLO1(int capacity){
        this.capacity = capacity;
        head = new Node(-1, -1, 0);  tail = new Node(-1, -1,0);head.next=tail;tail.prev=head;
        map = new HashMap<>();
    }
    // O(1)
    // if not exist or expire -> return -1;
    // other wise, return the value and update
    public int getWithLazyDeletion(int key){
        
        Node foundNode = map.get(key);
        if(foundNode == null) return -1;
        if(foundNode.expired_at <= System.currentTimeMillis()){
            remove(foundNode);map.remove(key);return -1;
        }
        // remove the node from the list
        // add the node to the beginning of the list
        remove(foundNode);
        addToHead(foundNode);
        return foundNode.val;
    }
    // O(1)
    // if the key exists & not expired -> update the val, the list and return
    // if the key exists & expired -> remove the entry
    // if exceed the capacity -> remove the last node
    // add the new node
    public void putWithLazyDeletion(int key, int val, long ttl){
        if(capacity==0) return;
        // 1.if the node already exists, update the value and return 
        if(map.containsKey(key)){
            Node updateNode = map.get(key);
            if(updateNode.expired_at <= System.currentTimeMillis()){
                remove(updateNode);map.remove(key);
            }else{
                // not expire
                updateNode.val = val;
                updateNode.expired_at = System.currentTimeMillis()+ttl;
                remove(updateNode); addToHead(updateNode);
                return;
            }
        }
        // 2.if the size == capacity, remove the least recently used one
        if(map.size() == capacity){
            Node lastNode = tail.prev;
            remove(lastNode); map.remove(lastNode.key);
        }
        // 3.build the new node 
        Node newNode = new Node(key,val,ttl);
        // 4. and add it to the beginning of the list and the map
        addToHead(newNode);map.put(key,newNode);
    }
    public void remove(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    public void addToHead(Node node){
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }

}
