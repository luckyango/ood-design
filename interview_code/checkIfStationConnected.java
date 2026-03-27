// 纽约地铁站A,B,C,D,E....，告诉你每一个地铁站通向哪里。然后写一个function判断任意给定的两个地铁站是否互通
// https://www.1point3acres.com/bbs/thread-1167463-1-1.html
import java.util.*;
public class checkIfStationConnected{
    class UnionFind{
        Map<String,String> parent;
        public UnionFind(){
            parent = new HashMap<>();
        }
        public void union(String a, String b){
            String ap = find(a); String bp = find(b);
            if(ap.equals(bp)) return;
            parent.put(ap, bp);
        }
        public String find(String a){
            //attention!!
            if(!parent.containsKey(a)){
                parent.put(a,a);
            }
            if(!a.equals(parent.get(a))){
                parent.put(a, find(parent.get(a)));
            }
            return parent.get(a);
        }
    }
    UnionFind uf = new UnionFind();
    public void buildGraph(List<String[]> cons){
        for(String[] con: cons){
            String a = con[0]; String b = con[1];
            uf.union(a, b);
        }
    }
    public boolean checkIfConnected(String s1, String s2){
        if(s1==null||s2==null) return false;
        String s1p = uf.find(s1); String s2p = uf.find(s2);
        return s1p.equals(s2p);
    }
}