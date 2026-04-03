// Problem: Bill of Materials (BoM) Expansion

// You are building a Bill of Materials (BoM) expansion tool for a manufacturing system.
// Each item may have zero or more child items. 
// A parent–child relationship includes a quantity indicating how many units of the child are required to build one unit of the parent.

// 🔌 Provided API
// You are given the following function:
// get_children(item_id) -> List[(child_id, quantity)]
// This function returns all direct children of the given item along with the required quantity.

// ⚠️ Constraints
// An item may appear under multiple parents (shared subcomponents)
// Cycles may exist in the graph (e.g., A → B → A)
// The hierarchy may be deeply nested
// 
// 🎯 Task
// Implement the following function:
// def expand_bom(root_id):
// The function should:
// Recursively traverse all descendants of the given root_id
// Compute the compounded quantity along each path
// Return a flattened list of results representing all traversal paths

// 📊 Output Format
// Return a list of dictionaries. Each dictionary represents one path and should include:
// root: the original root item
// parent: the direct parent of the current item
// item: the current item
// quantity: the compounded quantity from root to the current item
// path: a string representing the traversal path (e.g., "A>B>D")

// 🔁 Traversal Rules
// Traverse recursively until reaching leaf nodes
// Multiply quantities along the path

// Example:
// A → B (2)
// B → D (4)

// Then:
// D.quantity = 2 × 4 = 8

// 🔄 Cycle Handling
// If a cycle is detected (i.e., a node appears more than once in the current path):
// Stop further traversal on that path
// Return a row with an additional field:
// "cycle_detected": true

// ⚡ Performance Requirements
// Avoid redundant API calls by caching results of get_children
// Ensure the solution works efficiently for deep hierarchies

// 📌 Notes
// Each unique path should produce a separate row, even if items repeat across paths
// The output should be fully flattened (no nested structures)

// follow up1: how is the cache used; do you think it works?
// follow up2：if we run the sysytem in a production environ

// 📌 1. Why not use BFS?

// Question
// Why did you choose DFS instead of BFS for this problem?

// Answer
// DFS is more natural for this problem because we need to enumerate all root-to-leaf paths. 
// DFS allows us to use the recursion stack to represent the current path, which simplifies path tracking and backtracking.
// BFS would require storing the full path and a visited set for each state in the queue, which increases memory usage and code complexity.
//  Therefore, DFS is more efficient and easier to implement for path-based traversal problems like this.

// 📌 2. What is the purpose of the cache?

// Question
// What does the cache store, and why do we need it?

// Answer
// The cache stores the children of each item, essentially mapping an item ID to its direct child list. 
// Its purpose is to avoid redundant API calls.
// Since the same item may appear in multiple paths due to shared subcomponents, 
// without caching we would repeatedly call the API for the same node. 
// The cache ensures that each node’s children are fetched only once, improving performance and reducing latency.

// 8. How can the cache be extended in system design?

// Question
// How would you extend the cache in a production system?

// Answer
// In a production system, we can extend the cache from a local in-memory map to a distributed cache like Redis 
// to support multiple service instances.
// We can also use a database as a persistent layer and apply a cache-aside pattern. 
// Additionally, batching API requests can reduce network overhead. In large-scale systems, 
// a multi-level cache (local cache + Redis + database) is commonly used.

// 📌 9. When is caching not useful?

// Question
// When would caching not be effective in this system?

// Answer
// Caching is less effective when the data changes frequently, 
// when access patterns have low reuse, or when the API is already optimized and cached internally.
// In such cases, cache hit rates are low, and maintaining the cache may introduce unnecessary complexity without significant performance benefits.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class teslaBOMExpansion {


    class Child {
        String id;
        int quantity;

        Child(String id, int quantity) {
            this.id = id;
            this.quantity = quantity;
        }
    }

    // Cache to avoid repeated API calls
    private Map<String, List<Child>> cache = new HashMap<>();

    public List<Map<String, Object>> expandBom(String rootId) {
        List<Map<String, Object>> result = new ArrayList<>();

        // path list for building "A>B>C"
        List<String> path = new ArrayList<>();
        path.add(rootId);

        // visited set for cycle detection (per path)
        Set<String> visited = new HashSet<>();
        visited.add(rootId);

        dfs(rootId, rootId, 1, path, visited, result);

        return result;
    }

    private void dfs(String root,
                     String current,
                     int currentQty,
                     List<String> path,
                     Set<String> visited,
                     List<Map<String, Object>> result) {

        List<Child> children = getChildrenCached(current);

        // Leaf node
        if (children == null || children.isEmpty()) {
            return;
        }

        for (Child child : children) {

            int newQty = currentQty * child.quantity;

            // build path string
            path.add(child.id);
            String pathStr = String.join(">", path);

            Map<String, Object> row = new HashMap<>();
            row.put("root", root);
            row.put("parent", current);
            row.put("item", child.id);
            row.put("quantity", newQty);
            row.put("path", pathStr);

            // Cycle detection
            if (visited.contains(child.id)) {
                row.put("cycle_detected", true);
                result.add(row);

                // backtrack
                path.remove(path.size() - 1);
                continue;
            }

            result.add(row);

            // go deeper
            visited.add(child.id);
            dfs(root, child.id, newQty, path, visited, result);
            visited.remove(child.id);

            // backtrack
            path.remove(path.size() - 1);
        }
    }

    private List<Child> getChildrenCached(String itemId) {
        if (cache.containsKey(itemId)) {
            return cache.get(itemId);
        }
        List<Child> children = getChildren(itemId);
        cache.put(itemId, children);
        return children;
    }

    // Mock API (replace with real one)
    private List<Child> getChildren(String itemId) {
        return new ArrayList<>();
    }

}
