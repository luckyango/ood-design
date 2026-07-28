import java.util.*;
// v1: normal LRUCache
// v1: add TTL
// v3: add priority rank
public class LRUCache {
    // 1. the system should store the key, value pair -> use could get the value using the key
    // 2. there is a capacity limit for the system
    // 2a. if the current size exceeds the capacity -> kick out/remove the least recently used entry
    // 3.constraints on time complexity -> O(1)
    
    // solution:
    // 1.use a node class to store the pair information
    // 2.use a map to achieve O(1) look up - <key:key of the node, value: node>
    // 3.use a doubly linked list to achieve O(1) add/remove: add the most recently used node
    //  at the beginning of the list; remove the least recently used node at the end if exceed
    class Node{
        int key; int val; Node prev; Node next;
        public Node(int key, int val){
            this.key = key; this.val = val; prev=null;next=null;
        }
    }
    Map<Integer,Node> map;
    Node head; Node tail;
    int capacity;
    public LRUCache(int capacity){
        this.capacity = capacity;
        head = new Node(-1, -1);  tail = new Node(-1, -1);head.next=tail;tail.prev=head;
        map = new HashMap<>();
    }
    // methods in the class
    // 1. add a new key val pair, if already exists -> update the value.
    // mark the node as the most recently used after making change
    // if the size exceeds the capacity -> remove the least recently used cache and add the new one
    // 2. find a val using a key. if the key exists, find the value and update the position
    // if not exists, return false;
    public int find(int key){
        
        Node foundNode = map.get(key);
        if(foundNode == null) return -1;
        // remove the node from the list
        // add the node to the beginning of the list
        remove(foundNode);
        addToHead(foundNode);
        return foundNode.val;
    }
    
    public void add(int key, int val){
        if(capacity==0) return;
        // 1.if the node already exists, update the value and return 
        if(map.containsKey(key)){
            Node updateNode = map.get(key);
            updateNode.val = val;
            remove(updateNode); addToHead(updateNode);
            return;
        }
        // 2.if the size == capacity, remove the least recently used one
        if(map.size() == capacity){
            Node lastNode = tail.prev;
            remove(lastNode); map.remove(lastNode.key);
        }
        // 3.build the new node 
        Node newNode = new Node(key,val);
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