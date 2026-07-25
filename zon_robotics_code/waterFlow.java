import java.util.*;
public class waterFlow {
    // 一个2d terrain map of different heights，然后water drops on one point that flows out，
    // flowing是会从高处流到低处，最后要给出所有的points that got wet。
    // O(m*n) O(m*n)

    // solution 1: use dfs -> can record all the reachable cells
    // explore one path as deeply as possible
    int[][] dirs = {{-1,0},{0,-1},{1,0},{0,1}};
    public List<int[]> getWetPointsDFS(int[][] heights, int i, int j){
        // start from (i,j)
        List<int[]> ans = new ArrayList<>();
        if (heights == null || heights.length == 0 || heights[0].length == 0) {
            return ans;
        }
        if (i < 0 || i >=  heights.length || j < 0 || j >= heights[0].length) {
            return ans;
        }
        String startPoint = i+","+j;
        Set<String> visited = new HashSet<>();
        visited.add(startPoint);
        ans.add(new int[]{i,j});
        dfs(heights, i, j, ans, visited);
        return ans;
    }
    public void dfs(int[][] heights, int i, int j, List<int[]> ans, Set<String> visited){
        for(int[] dir: dirs){
            int ni = i+dir[0]; int nj = j+dir[1];
            String curCell = ni+","+nj;
            if(ni < 0 || ni >= heights.length || nj < 0 || nj >= heights[0].length || 
                heights[i][j] <=heights[ni][nj] || visited.contains(curCell)) continue;
            visited.add(curCell); ans.add(new int[]{ni,nj});
            dfs(heights, ni, nj, ans, visited);
        }
    }

    // solution 2: bfs -> record the traversal order in which the water spreads
    // spreading order because we explores the grid level by level
    public List<int[]> getWetPointsBFS(int[][] heights, int i, int j){
        List<int[]> ans = new ArrayList<>();
        
        if(heights==null || heights.length==0 || heights[0].length==0 ) return ans;
        int m = heights.length; int n = heights[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n) {
            return ans;
        }
        
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        queue.offer(new int[]{i,j});
        visited[i][j] = true;
        ans.add(new int[]{i,j});
        while (!queue.isEmpty()) {
            // int size = queue.size();
            // for(int k = 0; k < size; k++){
                int[] cur = queue.poll();
                int curi = cur[0]; int curj = cur[1];
                for(int[] dir:dirs){
                    int ni = curi+dir[0]; int nj = curj+dir[1];
                    if(ni<0||ni>=m||nj<0||nj>=n||visited[ni][nj]||heights[curi][curj]<=heights[ni][nj]){
                        continue;
                    }
                    ans.add(new int[]{ni,nj});
                    visited[ni][nj]=true;
                    queue.offer(new int[]{ni,nj});
                }
            // }
        }
        return ans;
    }
}
