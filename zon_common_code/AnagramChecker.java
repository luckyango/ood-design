// https://leetcode.com/discuss/post/6841339/passed-amazon-sde-new-grad-by-anonymous_-1p42/
import java.util.*;
public class AnagramChecker {
// 1.Anagram Checker
// Determine if two strings are anagrams.
   public boolean checkAnagram(String s1, String s2){
    if(s1==null&&s2==null) return true;
    if(s1==null||s2==null||s1.length()!=s2.length()) return false;
    Map<Character, Integer> map1 = new HashMap<>();
    for(char c: s1.toCharArray()){
        map1.put(c, map1.getOrDefault(c, 0)+1);
    }
    Map<Character, Integer> map2 = new HashMap<>();
    for(char c: s2.toCharArray()){
        map2.put(c, map2.getOrDefault(c, 0)+1);
        if(map2.get(c) > map1.getOrDefault(c, 0)) return false;
    }
    return true;
   }

// 2.Stream of Words

// For each incoming word, return the last seen anagram (if any), or the word itself.
Map<String, String> lastSeen = new HashMap<>();
public String findLastAnagram(String newWord){
    char[] c = newWord.toCharArray();
    Arrays.sort(c);
    String key = new String(c);
    String ans = lastSeen.getOrDefault(key, newWord);
    lastSeen.put(key, newWord);
    return ans;
}
}
