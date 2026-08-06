// You are given a continuous string of lowercase English characters 
// without any spaces (e.g., "myhousehavecat").

// You are also provided with a magic helper method, boolean isWord(String str),
//  which queries a dictionary and returns true if the given string is a valid word, and false otherwise.

// Task: Write a function that uses backtracking to segment the input string 
// into a sequence of valid dictionary words. Return the result as an array of strings.

// Notes / Constraints:
// There may be multiple valid ways to segment the input string. You only need to return one valid solution.
// You can safely assume that the input string will always have at least one valid segmentation.
// You cannot change the order of the characters in the input string.
// Example input: s = "myhousehavecat"
// Example output: ["my", "house", "have", "cat"]
import java.util.*;
public class SegmentString {
    public boolean isWord(String str){
        return true;
    }
    public List<String> splitString(String str){
        // find one valid way to split the string
        // 1. edge case: (1)input valid
        //  (2) if str it self is a valid word -> return it
        // 2. backtracking 
        // start from 'start' idx, end with 'end' index, if the substring is a valid word,
        // record the substring, and continue finding the next valid word
        // stop: start idx == str.length()
        List<String> ans = new ArrayList<>();
        if(str==null || str.length()==0) return ans;
        if(isWord(str)){
            ans.add(str); return ans;
        }
        backtracking(str,ans,0, new HashSet<>());
        return ans;
    }
    public boolean backtracking(String str, List<String> ans, int start, Set<Integer> failStart){
        if(start == str.length()) return true;
        if(failStart.contains(start)) return false;
        for(int end = start+1; end <= str.length(); end++){
            String subString = str.substring(start, end);
            if(isWord(subString)){
                ans.add(subString);
                if(backtracking(str, ans, end,failStart)) return true;
                ans.remove(ans.size()-1);
            }
        }
        failStart.add(start);
        return false;
    }
}
