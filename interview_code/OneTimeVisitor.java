// Problem

// You are given a stream of customer visits. Each customer is identified by an integer customerId.

// A customer is called a one-time visitor if they have visited exactly once.

// Implement the following methods:

// postCustomerVisit(int customerId) 
// // Records a visit for the given customer.
// getFirstOneTimeVisitor()
// // Returns the first customer who has visited exactly once.
// // If no such customer exists, return -1.
// Example :

// postCustomerVisit(2)
// postCustomerVisit(5)
// postCustomerVisit(2)
// postCustomerVisit(3)
// getFirstOneTimeVisitor() → 5

// postCustomerVisit(2)
// postCustomerVisit(4)
// postCustomerVisit(5)
// getFirstOneTimeVisitor() → 3
// Constraints
// Up to 100,000 operations
// Customer IDs are positive integers

// Expected Complexity :

// postCustomerVisit -> O(1)
// getFirstOneTimeVisitor -> O(1)
import java.util.*;
public class OneTimeVisitor {

    // use a map - record <id, frequency>
    // use a linked list - to record the visiting order of one time customer
    // use a variable - the current one time visit node
    // Records a visit for the given customer.
    class Node{
        int id; int visitCount; Node next; Node prev;
        public Node(int id){
            this.id = id; visitCount = 1;
        }
    }
    Map<Integer, Node> idToNode;
    Node head; Node tail;// list for one time customer

    public OneTimeVisitor(){
        idToNode = new HashMap<>();
        head = new Node(-1); tail = new Node(-1);
        head.next = tail; tail.prev = head;
    }


    public void postCustomerVisit(int customerId){
        // first visit -> add to the map, add to the one time customer
        if(!idToNode.containsKey(customerId)){
            Node newCustomer = new Node(customerId);
            idToNode.put(customerId, newCustomer);
            addToList(newCustomer);
        }else{
            // visit again
            // count++;
            // if visitCount == 2 -> remove from one time list
            Node oldCustomer = idToNode.get(customerId);
            oldCustomer.visitCount++;
            if(oldCustomer.visitCount == 2) removeFromList(oldCustomer);
        }
    }

    // Returns the first customer who has visited exactly once.
    // If no such customer exists, return -1.
    public int getFirstOneTimeVisitor(){
        if(head.next.id == -1) return -1;
        return head.next.id;
    }

    public void addToList(Node node){
        node.prev = tail.prev;
        tail.prev.next = node;
        node.next = tail;
        tail.prev = node;
    }

    public void removeFromList(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public static void main(String[] args){
        OneTimeVisitor oneTimeVisitor = new OneTimeVisitor();
        // edge case 1: get one time visitor when no user visits -> should be -1
        System.out.println("edge case 1: get one time visitor when no user visits -> should be -1");
        System.out.println("Expected: -1");
        System.out.println(oneTimeVisitor.getFirstOneTimeVisitor());
        // test case: one customer visits and check the oneTimeVisitor
        int[] visitors = {2,5,2,3,2,4,5,3,4};
        for(int visitor: visitors){
            oneTimeVisitor.postCustomerVisit(visitor);
            System.out.println("cur first one time visitor: " + oneTimeVisitor.getFirstOneTimeVisitor());
        }
        // edge case 2: return -1 when all customers visit twice or more
    }
}
