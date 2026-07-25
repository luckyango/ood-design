import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PathInGrid {
    // Shortest Path in a 2D Grid with Obstacles (with Path Reconstruction)
    // Given a 2D grid of a specified size, start coordinates, and end coordinates, 
    // find the shortest path between the two points. The problem is presented in parts: 
    // Part 1 — find the shortest distance between start and end on an empty grid; 
    // Part 2 — given a list of obstacle coordinates that cannot be traversed, 
    // find the shortest distance avoiding them; 
    // Part 3 — print the actual path taken for the shortest route.
    int[][] dirs = {{-1,0},{0,-1},{1,0},{0,1}};
    // part 1
    public int shortestDisNoObs(int[][] dis, int[] start, int[] end){
        if(dis==null || start==null || end == null) return -1;
        if(start[0]==end[0] && start[1]==end[1]) return 0;
        int m = dis.length; int n = dis[0].length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        queue.offer(new int[]{start[0],start[1]}); visited[start[0]][start[1]] = true;
        int ans = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i< size; i++){
                int[] cur = queue.poll();
                if(cur[0]==end[0] && cur[1]==end[1]) return ans;
                for(int[] dir: dirs){
                    int ni = cur[0] + dir[0]; int nj = cur[1]+dir[1];
                    if(ni < 0 || ni >= m || nj < 0 || nj >= n || visited[ni][nj]) continue;
                    queue.offer(new int[]{ni,nj});
                    visited[ni][nj] = true;
                }
            }
            ans++;
        }
        return -1;
    }
    // part 2
    public int shortestDisWithObs(int[][] dis, int[][] obs, int[] start, int[] end){
        if(dis==null || start==null || end == null ) return -1;
        
        int m = dis.length; int n = dis[0].length;
        int[][] visited = new int[m][n];// -1 - ob; 0 - not visit; 1 - visited
        for(int[] ob: obs){
            int i = ob[0]; int j = ob[1];
            if((i==start[0]&&j==start[1]) || (i==end[0]&&j==end[1])) return -1;
            visited[i][j] = -1;
        }
        if(start[0]==end[0] && start[1]==end[1]) return 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start[0],start[1]}); visited[start[0]][start[1]] = 1;
        int ans = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i< size; i++){
                int[] cur = queue.poll();
                if(cur[0]==end[0] && cur[1]==end[1]) return ans;
                for(int[] dir: dirs){
                    int ni = cur[0] + dir[0]; int nj = cur[1]+dir[1];
                    if(ni < 0 || ni >= m || nj < 0 || nj >= n || visited[ni][nj]==1 || 
                        visited[ni][nj]==-1) continue;
                    queue.offer(new int[]{ni,nj});
                    visited[ni][nj] = 1;
                }
            }
            ans++;
        }
        return -1;
    }
    // part 3
    public List<int[]> actualPathDisWithObs(int[][] dis, int[][] obs, int[] start, int[] end){
        List<int[]> ans = new ArrayList<>();
        if(dis==null || start==null || end == null) return ans;
        
        int m = dis.length; int n = dis[0].length;
        int[][] visited = new int[m][n];// -1 - ob; 0 - not visit; 1 - visited
        for(int[] ob: obs){
            int i = ob[0]; int j = ob[1];
            if((i==start[0]&&j==start[1]) || (i==end[0]&&j==end[1])) return ans;
            visited[i][j] = -1;
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start[0],start[1]}); visited[start[0]][start[1]] = 1;

        int[][][] parent = new int[m][n][2];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                parent[i][j][0]=-1; parent[i][j][1]=-1;
            }
        }
        
        while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int i = cur[0]; int j = cur[1];
                if(i==end[0] && j==end[1]) break;
                for(int[] dir: dirs){
                    int ni = i + dir[0]; int nj = j+dir[1];
                    if(ni < 0 || ni >= m || nj < 0 || nj >= n || visited[ni][nj]==1 || 
                        visited[ni][nj]==-1) continue;
                    queue.offer(new int[]{ni,nj});
                    visited[ni][nj] = 1;
                    parent[ni][nj][0] = i; parent[ni][nj][1] = j;
                }
        }
        // build the path
        if(visited[end[0]][end[1]] != 1) return ans;
        int i = end[0]; int j = end[1];
        while(!(i==start[0] && j ==start[1])){
            ans.add(new int[]{i,j});
            int ti = parent[i][j][0]; int tj = parent[i][j][1];
            i = ti; j = tj;
        }
        ans.add(new int[]{i,j});
        Collections.reverse(ans);
        return ans;
    }
    // part 4
    // https://github.com/doocs/leetcode/blob/main/lcci/08.02.Robot%20in%20a%20Grid/README.md
    // Description
    // Imagine a robot sitting on the upper left corner of grid with r rows and c columns. 
    // The robot can only move in two directions, right and down, but certain cells are "off limits" 
    // such that the robot cannot step on them. 
    // Design an algorithm to find a path for the robot from the top left to the bottom right.

    // "off limits" and empty grid are represented by 1 and 0 respectively.

    // Return a valid path, consisting of row number and column number of grids in the path.

    // Example 1:

    // Input:
    // [ [0,0,0], [0,1,0], [0,0,0] ]

    // Output: [[0,0],[0,1],[0,2],[1,2],[2,2]]

    // Note: // r, c <= 100
    public List<List<Integer>> getPathCornerToCorner(int[][] obstacle){
        List<List<Integer>> ans = new ArrayList<>();
        if(obstacle==null || obstacle[0][0] == 1 || obstacle[obstacle.length-1][obstacle[0].length-1] == 1) return ans;
        boolean[][] failed = new boolean[obstacle.length][obstacle[0].length];
        dfs(obstacle, 0, 0,ans,failed);
        return ans;
    }
    public boolean dfs(int[][] obstacle, int i, int j, List<List<Integer>> ans,boolean[][] failed){
        if(i >= obstacle.length || j >= obstacle[0].length || obstacle[i][j] == 1 || failed[i][j]) return false;
        ans.add(List.of(i,j));
        if( (i==obstacle.length-1 && j==obstacle[0].length-1) || dfs(obstacle, i+1, j, ans,failed) || dfs(obstacle, i, j+1, ans, failed)){
            return true;
        }
        ans.remove(ans.size()-1);
        failed[i][j] = true;
        return false;
    }
    public static void main(String[] args) {
        PathInGrid solution = new PathInGrid();

        // =========================================================
        // Part 1: Empty grid, shortest distance
        // =========================================================
        int[][] emptyGrid = new int[4][5];

        int[] start1 = {0, 0};
        int[] end1 = {3, 4};

        int distanceNoObstacle =
                solution.shortestDisNoObs(emptyGrid, start1, end1);

        System.out.println("Part 1:");
        System.out.println("Shortest distance without obstacles = "
                + distanceNoObstacle);
        System.out.println("Expected = 7");
        System.out.println();

        // =========================================================
        // Part 2: Grid with obstacles, shortest distance
        //
        // Grid:
        // S . X . .
        // X . X . .
        // . . . . .
        // . X X X E
        //
        // One shortest path:
        // (0,0) -> (0,1) -> (1,1) -> (2,1)
        // -> (2,2) -> (2,3) -> (2,4) -> (3,4)
        //
        // Distance = 7
        // =========================================================
        int[][] gridWithObstacles = new int[4][5];

        int[][] obstacles = {
            {0, 2},
            {1, 0},
            {1, 2},
            {3, 1},
            {3, 2},
            {3, 3}
        };

        int[] start2 = {0, 0};
        int[] end2 = {3, 4};

        int distanceWithObstacle =
                solution.shortestDisWithObs(
                        gridWithObstacles,
                        obstacles,
                        start2,
                        end2
                );

        System.out.println("Part 2:");
        System.out.println("Shortest distance with obstacles = "
                + distanceWithObstacle);
        System.out.println("Expected = 7");
        System.out.println();

        // =========================================================
        // Part 3: Actual shortest path with obstacles
        // =========================================================
        List<int[]> shortestPath =
                solution.actualPathDisWithObs(
                        gridWithObstacles,
                        obstacles,
                        start2,
                        end2
                );

        System.out.println("Part 3:");
        System.out.println("Actual shortest path:");

        printIntArrayPath(shortestPath);

        System.out.println("Path distance = "
                + (shortestPath.isEmpty()
                ? -1
                : shortestPath.size() - 1));

        System.out.println("Expected distance = 7");
        System.out.println();

        // =========================================================
        // Part 4: Robot in a Grid
        //
        // 0 = empty
        // 1 = obstacle
        //
        // Grid:
        // 0 0 0
        // 0 1 0
        // 0 0 0
        // =========================================================
        int[][] cornerGrid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };

        List<List<Integer>> cornerPath =
                solution.getPathCornerToCorner(cornerGrid);

        System.out.println("Part 4:");
        System.out.println("Corner-to-corner path:");
        System.out.println(cornerPath);

        System.out.println(
                "One expected path = "
                        + "[[0, 0], [1, 0], [2, 0], [2, 1], [2, 2]]"
        );

        System.out.println();

        // =========================================================
        // Additional test: no valid path
        // =========================================================
        int[][] blockedGrid = {
            {0, 1, 0},
            {1, 1, 0},
            {0, 0, 0}
        };

        List<List<Integer>> noPath =
                solution.getPathCornerToCorner(blockedGrid);

        System.out.println("Additional test:");
        System.out.println("No path result = " + noPath);
        System.out.println("Expected = []");
    }

    private static void printIntArrayPath(List<int[]> path) {
        if (path.isEmpty()) {
            System.out.println("[]");
            return;
        }

        System.out.print("[");

        for (int i = 0; i < path.size(); i++) {
            int[] point = path.get(i);

            System.out.print(
                    "[" + point[0] + ", " + point[1] + "]"
            );

            if (i != path.size() - 1) {
                System.out.print(", ");
            }
        }

        System.out.println("]");
    }
}
