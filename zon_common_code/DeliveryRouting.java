// You are a software engineer at Amazon working on the Fulfillment Network routing system. 
// The network consists of several fulfillment centers (represented as nodes) 
// connected by bidirectional delivery routes.
// A specific fulfillment center (the destination) is currently experiencing a shortage of a popular item 
// and needs to request inventory transfers from nearby centers.
// However, due to delivery time constraints, 
// the inventory can only be transferred if the sending center is within a certain number of delivery routes
//  (maxStep) from the destination.
// You are given the following inputs:
// 1. connections: A 2D array of integers representing the bidirectional delivery routes 
// between fulfillment centers (e.g.,[u, v]
// means there is a direct route between center u
// and center v).
// 2. destination : An integer representing the ID of the fulfillment center that needs the inventory.
// 3. maxStep: An integer representing the maximum number of routes (edges) 
// an inventory transfer can take to reach the destination.
// 4. inventory: A hash map (or dictionary) where the key is the fulfillment center ID 
// and the value is the current quantity of the item in stock at that center.
// Task: Write a function that returns an array of all fulfillment center IDs
//  that can successfully transfer inventory to the destination center
// To be included in the result, a fulfillment center must meet all of the following criteria:
// It is not the destination center itself.
// It has at least 1 item in stock ( inventory > 0)
// The shortest path from the center to the destination is less than or equal to maxStep
// The returned array can be in any order.

// Example 
// input: 
// connections = [[1, 2], [1, 3], [2, 4], [3, 4], [4, 5]]
// destination = 4
// maxStep = 1
// inventory = {1: 2, 2: 0, 3: 5, 4: 3, 5: 6}

// output
// [3, 5]
import java.util.*;
public class DeliveryRouting {
    public List<Integer> findAllSuitableCenters(int destination, int maxStep, 
        Map<Integer, Integer> centers,int[][] connections
    ){
        List<Integer> ans = new ArrayList<>();
        // 1.build the graph
        // 2. start from the destination, use bfs to find the suitable centers until the step reach to maxStep
        // for each center, check the quantity 
        // 3.during the process, use a set to track the visited center
        Map<Integer,List<Integer>> graph = new HashMap<>();
        for(int[] con: connections){
            int u = con[0]; int v = con[1];
            graph.computeIfAbsent(u, k->new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k->new ArrayList<>()).add(u);
        }
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> set = new HashSet<>();
        queue.offer(destination); set.add(destination); int step = 0;
        while(!queue.isEmpty() && step<maxStep){
            int size = queue.size();
            for(int i = 0; i < size; i++){
                int cur = queue.poll();
                // if(cur==destination || centers.get(cur)<=0) continue;
                for(int next: graph.getOrDefault( cur, Collections.emptyList())){
                    if(set.contains(next)|| next==destination) continue;
                    if(centers.getOrDefault(next, 0)>0){
                        ans.add(next);
                    }
                    queue.offer(next); set.add(next); 
                }
            }
            step++;
        }
        return ans;
    }
}
