// 给一串数据流 带时间 日期 和用户名 要求输出一个list表示每个用户每天log in 多少次 按用户名排列 同时要检查错误的日期和时间
// https://www.1point3acres.com/bbs/thread-1165115-1-1.html
import java.util.*;
        class LoginCount{
            private String name; private String date; private int count;
            public LoginCount(String name, String date, int count){
                this.name=name; this.count=count; this.date = date;
            }
            public String getName(){
                return this.name;
            }
            public String getDate(){
                return this.date;
            }
        }
public class dealWithStream {
        public List<LoginCount> countLogin(List<String[]> logs){
            Map<String, Map<String, Integer>> map = new HashMap<>();
            for(String[] log: logs){
                if(log==null || log.length < 3) continue;
                String time = log[0];
                String date = log[1];
                String name = log[2];
                if(!isValidTime(time) || !isValidDate(date)) continue;
                map.putIfAbsent(name, new HashMap<>());
                Map<String,Integer> dateMap = map.get(name);
                dateMap.put(date, dateMap.getOrDefault(date, 0)+1);
            }
            List<LoginCount> ans = new ArrayList<>();
            for(String name: map.keySet()){
                Map<String, Integer> dateMap = map.get(name);
                for(String date: dateMap.keySet()){
                    ans.add(new LoginCount(name, date, dateMap.get(date)));
                }
            }
            Collections.sort(ans, (a,b)->(!a.getName().equals(b.getName())?a.getName().compareTo(b.getName()) : a.getDate().compareTo(b.getDate())));
            return ans;
        }
        public boolean isValidTime(String time){
            try {
                Integer.parseInt(time);
                return true;
            } catch (Exception e) {
                // TODO: handle exception
                return false;
            }
        }
        public boolean isValidDate(String date){
            if(date==null || date.length()!=10) return false;
            return true;
        }
}
