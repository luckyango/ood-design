// 题目背景
// 在一个购物网站中，为了挽留流失用户，需要分析那些“非回访用户”（只访问过一次的用户），并向他们提供促销活动。
// 核心任务
// 你需要实现一个类，包含以下两个核心方法：
// newUserLogin(username): 每当用户登录时调用该方法。
// getOldestOneTimeVisitingUser(): 返回在所有仅访问过一次的用户中，登录时间最早（最久远）的那位用户的用户名。
// Follow-up (图片 9)
// getOldestOneTimeVisitingUsers(int X): 扩展功能，要求返回前 $X$ 个最久远的、仅访问过一次的用户。
// 示例分析
// 用户登录顺序：john $\rightarrow$ jeff $\rightarrow$ jeff。
// 调用 getOldestOneTimeVisitingUser()。
// 结果：返回 john。虽然 jeff 也登录过，但他登录了两次，不符合“仅访问一次”的条件。
// https://www.1point3acres.com/bbs/thread-1166590-1-1.html
import java.util.*;
// LinkedHashSet = hashmap + doubly linked list!!!
// 写一下不用linkedhashmap的实现方法！！
class UserTracker {

    private Map<String, Integer> count = new HashMap<>();
    private LinkedHashSet<String> oneTimeUsers = new LinkedHashSet<>();

    public void newUserLogin(String username) {
        count.put(username, count.getOrDefault(username, 0) + 1);

        if (count.get(username) == 1) {
            oneTimeUsers.add(username);
        } else if (count.get(username) == 2) {
            oneTimeUsers.remove(username);
        }
    }

    public String getOldestOneTimeVisitingUser() {
        if (oneTimeUsers.isEmpty()) return null;
        return oneTimeUsers.iterator().next();
    }
    public List<String> getOldestOneTimeVisitingUsers(int x) {
        List<String> res = new ArrayList<>();

        for (String user : oneTimeUsers) {
            res.add(user);
            if (res.size() == x) break;
        }

        return res;
    }
}
