import java.util.*;
public class PackageDependency {
    // Question was framed over working of Maven/Gradle. 
    // Given the dependencies of the packages to be installed. 
    // For example, A -> B, C, D (Which means A depends upon B, C & D. 
    // Hence, B,C & D should be installed before installing A) B -> F D -> E 
    // Print the order in which packages should be installed. 
    // dependency of packages
    // a -> b : instal b before install a
    // 
    public List<String> findInstallOrder(List<String[]> projectDependency){
        // 1. build the graph and indegree
        // key: package, value: list of packages which depend on it
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        Set<String> allPackages = new HashSet<>();
        List<String> ans = new ArrayList<>();
        for(String[] depend: projectDependency){
            String a = depend[0]; String b = depend[1];
            allPackages.add(a); allPackages.add(b);
            graph.computeIfAbsent(b, k->new ArrayList<>()).add(a);
            indegree.put(a, indegree.getOrDefault(a, 0)+1);
            indegree.putIfAbsent(b, 0);
        }
        // 2.start with the packages with no dependency
        Queue<String> queue = new LinkedList<>();
        for(String pack: allPackages){
            if(indegree.get(pack)==0){
                queue.offer(pack); ans.add(pack);
            }
        }
        while(!queue.isEmpty()){
            String cur = queue.poll();
            if(!graph.containsKey(cur)) continue;
            for(String next: graph.get(cur)){
                indegree.put(next, indegree.get(next)-1);
                if(indegree.get(next) == 0){
                    queue.offer(next); ans.add(next);
                }
            }
        }

        return allPackages.size()==ans.size()?ans:new ArrayList<>();
    }
    // Later this question is modified and asked: Given an order of packages,
    // tell whether with the given dependencies, 
    // is it possible to install packages in the given order or not.
    public boolean checkValid(List<String[]> dependencies, List<String> order){
        // 1.build the graph
        // 2.use an index to traverse the order list, if the indegree of the service we currently check 
        // is not 0, means it still require prerequisite, so the order is not valid
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();
        Set<String> allPackages = new HashSet<>();
        for(String[] depend: dependencies){
            String a = depend[0]; String b = depend[1];
            graph.computeIfAbsent(b, k->new ArrayList<>()).add(a);
            indegree.put(a, indegree.getOrDefault(a, 0)+1);
            allPackages.add(a); allPackages.add(b);
            indegree.putIfAbsent(b, 0);
        }
        Set<String> processed = new HashSet<>();
        for(int i = 0; i < order.size(); i++){
            String cur = order.get(i);
            if(!allPackages.contains(cur) || !processed.add(cur) || indegree.get(cur) != 0) return false;
            if(!graph.containsKey(cur)) continue;
            for(String next: graph.get(cur)){
                indegree.put(next, indegree.get(next)-1);
            }
        }
        return true;
    }
//     Version 1: Install Package With Dependencies
// You are given a map of software packages and dependencies.
// Example:
// A depends on B, C
// B depends on D, E, F
// C depends on F
// F depends on G
// H depends on I, J
// J depends on G
// Write a class/method that installs an individual package along with all of its dependencies.
// Assume every package has an install() method that installs only that package, not its dependencies.
// For example, to install package A, a valid install order could be:
// G, F, C, E, D, B, A
    public List<String> getPackages(List<String[]> connections,String target){
        // dfs 
        // clarification
        // 1.input: vallid?
        // 2.is guaranteed to have a solution? -> what if there is a 
        // 1.build the graph
        Map<String, Set<String>> graph = new HashMap<>();
        for(String[] con: connections){
            String a = con[0];String b = con[1];
            graph.computeIfAbsent(a, k->new HashSet<>()).add(b);
        } 
        // 2.perform dfs and find all the packages, return empty list if there is a cycle
        List<String> ans = new ArrayList<>();
        Map<String,Integer> visited = new HashMap<>();
        boolean noCycle = dfs(graph, ans, target,visited);
        return noCycle?ans:new ArrayList<>();
    }
    // for visited -> no string in the graph, never visited; 1, visiting; 2, finish visiting
    public boolean dfs(Map<String, Set<String>> graph, List<String> ans, String cur, Map<String,Integer> visited){
        if(visited.containsKey(cur) && visited.get(cur)==1) return false;
        if(visited.containsKey(cur) && visited.get(cur)==2) return true;
        visited.put(cur, 1);
        
        // boolean canInstall = true;
        for(String next: graph.getOrDefault(cur,Collections.emptySet())){
            if(!dfs(graph, ans, next,visited)){
                return false;
            }
        }
        ans.add(cur);
        // cur.install();
        visited.put(cur, 2);
        return true;
    }


    // Version 2: Build Package With Dependencies
    // You are given package dependencies and a package name x.
    // Return the build order for package x.
    // Example:

    // A -> {B, C}
    // B -> {E}
    // C -> {D, E, F}
    // D -> {}
    // F -> {}
    // G -> {C}
    // For package A, one valid build order is:

    // E, B, F, D, C, A
    // The interviewer may also ask how to handle cyclic dependencies.
}
