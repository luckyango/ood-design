
import java.util.*;

public class amazonCode {
    // ：有 t 个测试场景。每个场景有 truckCapacities（数组长度 n）和 packageWeights（数组长度 m）。
// 一辆车能运送包裹的条件：当前容量 >= 包裹重量，运送后容量 = floor(当前容量 / 2)。
// 每辆车可以运送多次包裹（只要满足条件）。每辆车的初始容量固定。
// 问：用这些车能否运送完所有包裹（包裹任意分配给车，每辆车按顺序运送包裹时容量会下降）。
    public boolean ifDeliver(int[] truckCapacities, int[] packageWeights){
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for(int cap: truckCapacities) pq.offer(cap);
        Arrays.sort(packageWeights);
        for(int i = packageWeights.length; i>=0; i--){
            if(pq.isEmpty()) return false;
            int cur = pq.poll();
            if(cur < packageWeights[i]) return false;
            int newCap = cur/2;
            if(newCap > 0) pq.offer(newCap);
        }
        return true;
    }
    // 给定多个用户的 活跃时间区间 [start, end]，
    // 每个用户每秒贡献 1 usage，usage 是按时间累计的总和，求 累计 usage 第一次 ≥ max_usage 的时间点。
    public int firstMaxUsage(List<int[]> usages, int maxUsage) {
        TreeMap<Integer, Integer> map = new TreeMap<>();

        for (int[] use : usages) {
            int start = use[0];
            int end = use[1];

            map.put(start, map.getOrDefault(start, 0) + 1);
            map.put(end + 1, map.getOrDefault(end + 1, 0) - 1); // 👈关键
        }

        int count = 0;

        for (int time : map.keySet()) {
            count += map.get(time);

            if (count >= maxUsage) {
                return time;
            }
        }

        return -1;
    }
    // 亚马逊有很多的服务, 每个服务之间存在依赖关系, 当我们将其中的一些服务停机后, 求哪些服务会被影响.
    public List<String> findAffectedServices(List<String[]> dependencies, List<String> downServices){
        //1.build graph
        Map<String, List<String>> graph = new HashMap<>();
        // Map<String, Integer> indegree = new HashMap<>();
        for(String[] depen: dependencies){
            // u depends v
            // if v down -> affect u
            String u = depen[0]; String v = depen[1];
            graph.computeIfAbsent(v,k-> new ArrayList<>()).add(u);

        }
        List<String> ans = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        for(String down: downServices){
            queue.offer(down); visited.add(down);
        }
        while(!queue.isEmpty()){
            String cur = queue.poll();
            if(!graph.containsKey(cur)) continue;
            for(String next: graph.get(cur)){
                if(visited.contains(next)) continue;
                ans.add(next); visited.add(next);queue.offer(next);
            }
        }
        return ans;
    }
}
