// Implement insert, search and startsWith. 
// search(word) should return true if word has been inserted before. 
// startsWith(word) should return a list of words that have been inserted before and start with word.

// clarify
// 1.insert -> should we return the result of the insert. like if the word already insert before,
// should we return false?
// 2.search -> if the searched word is only part of other word, return false?
// 3.start with - if the given prefix is a word itself, should we return? ab, word：ab,abc,abd,ac
//  - is there any requirement on the output?
// 4.what does the string contain? it only consists of lowercase letters? or both upper & lower? 
// or special chars?

// implementation:
// trie - 
import java.util.*;
public class AutoComplete {
    class Node{
        Map<Character,Node> children = new HashMap<>();
        boolean isWord = false;
        String content = null;
    }
    Node root = new Node();
    public boolean insert(String word){
        Node node = root;
        for(char c: word.toCharArray()){
            node.children.putIfAbsent(c, new Node());
            node = node.children.get(c);
        }
        if(node.isWord) return false;
        node.isWord = true;
        node.content = word;
        return true;
    }
    public boolean search(String word){
        Node node = root;
        for(char c: word.toCharArray()){
            if(!node.children.containsKey(c)) return false;
            node = node.children.get(c);
        }
        return node.isWord;
    }
    public List<String> startWith(String word){
        List<String> ans = new ArrayList<>();
        Node node = root;
        for(char c: word.toCharArray()){
            // cant find -> return empty list
            if(!node.children.containsKey(c)) return ans;
            node = node.children.get(c);
        }
        findAllWords(node, ans);
        return ans;
    }   
    public void findAllWords(Node node, List<String> ans){
        // use bfs
        Queue<Node> queue = new LinkedList<>();
        queue.offer(node);
        while(!queue.isEmpty()){
            Node cur = queue.poll();
            if(cur.isWord) ans.add(cur.content);
            if(cur.children == null || cur.children.size()==0) continue;
            for(Node next: cur.children.values()){
                queue.offer(next);
            }
        }
    }
}
