import java.util.*;
// Amazon has many services with dependencies; some are shut down. 
// Return the full set of services that become unavailable, including transitive consumers.
public class ServiceDependency {
    // b -> a  : b rely on a
    // a -> c  : a rely on c
    // if a is shutdown, find all the following depending services
    // 1. build the graph, key: the current service; value: all the services rely on current services
    // 2. start from the shutdown service and find all the affective servies
    public List<Integer> findShutdownServices(List<int[]> dependencyList, int shutdownService){
        // dependency list - {a, b} -> service a rely on b
        // 1.build the graph
        Map<Integer,List<Integer>> graph = new HashMap<>();
        for(int[] dependency: dependencyList){
            int a = dependency[0]; int b = dependency[1];
            // b affect a if shutdown
            graph.computeIfAbsent(b, k->new ArrayList<>()).add(a);
        }
        // 
        // if(!graph.containsKey(shutdownService)) return null;
        Set<Integer> ans = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(shutdownService); ans.add(shutdownService);
        while(!queue.isEmpty()){
            int cur = queue.poll();
            for(int next: graph.get(cur)){
                if(ans.contains(next)) continue;
                ans.add(next); queue.add(next);
            }
        }

        return new ArrayList<>(ans);
    }
}
