// log处理（两份log，找到同时出现在两个log里and浏览了不同的网站的人）
import java.util.*;
public class detectUserInLog {
    public class Log{
        int userId; String webName;
    }
    public List<Integer> detectUser(List<Log> log1, List<Log> log2){
        // iterate log1 -> map key:userId; value: webName
        // iterate log2 -> if the id occured in log1 and name is different -> add in ans
        Map<Integer, Set<String>> map = new HashMap<>();
        for(Log log: log1){
            map.computeIfAbsent(log.userId, k->new HashSet<>()).add(log.webName);
        }
        Set<Integer> ans = new HashSet<>();
        for(Log log: log2){
            if(map.containsKey(log.userId) && !map.get(log.userId).contains(log.webName)) ans.add(log.userId);
        }
        return new ArrayList<>(ans);
    }
}
