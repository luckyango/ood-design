// Q2. Find Compound Words
// Problem:
// Given a large list of words, identify all words that can be formed 
// by combining two or more other words from the list.

// Example:
// Input: [rockstar, rock, star, rocks, tar, superhighway, super, highway, high, way]

// Valid decompositions:
// [rock, star]
// [super, highway]
// [super, high, way]
import java.util.*;
public class findCompoundWords {
    public List<List<String>> getCompound(List<String> words){
        // 1. length: 3 4 4
        // 1.sort the words by length? 
        // 2.for word at index i, check if it can be formed by word at [0,i-1]
        // for the word, take its substring, if the substring exists in the wordSet -> find one part
        // and continue until all the parts can be found
        // backtracking + use a memo (record if it can be formed)

        // clarify -> can the word be used more than once?
        // Collections.sort(words);
        List<List<String>> ans = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(words);
        
        for(int i = 0; i < words.size(); i++){
            String word = words.get(i);
            Boolean[] memo = new Boolean[word.length()];
            findCompunds(word, wordSet, 0, ans, new ArrayList<>(),memo);
        }
        return ans;
    }
    public boolean findCompunds(String target, Set<String> wordSet, int start, List<List<String>> ans,
        List<String> path,Boolean[] memo
    ){
        if(start == target.length()){
            if(path.size() > 1){
                ans.add(new ArrayList<>(path));
                return true;
            }
            return false;
        }
        if(memo[start] != null && !memo[start]) return false;
        boolean canFind = false;
        for(int end = start+1; end <= target.length(); end++){
            String subString = target.substring(start, end);
            if(wordSet.contains(subString)){
                path.add(subString);
                if(findCompunds(target, wordSet, end, ans, path,memo)) canFind=true;
                path.remove(path.size()-1);
            }
        }
        memo[start] = canFind;
        return canFind;
    }
    public static void main(String[] args) {
        findCompoundWords solver = new findCompoundWords();

        // Test 1: basic example
        List<String> words1 = Arrays.asList(
                "rockstar",
                "rock",
                "star",
                "rocks",
                "tar",
                "superhighway",
                "super",
                "highway",
                "high",
                "way"
        );

        System.out.println("Test 1:");
        printResult(solver.getCompound(words1));


        // Test 2: one word has multiple decompositions
        List<String> words2 = Arrays.asList(
                "catsdog",
                "cat",
                "cats",
                "s",
                "dog"
        );

        System.out.println("\nTest 2:");
        printResult(solver.getCompound(words2));


        // Test 3: same word can be reused
        List<String> words3 = Arrays.asList(
                "catcat",
                "cat"
        );

        System.out.println("\nTest 3:");
        printResult(solver.getCompound(words3));


        // Test 4: multiple possible decompositions
        List<String> words4 = Arrays.asList(
                "aaaa",
                "a",
                "aa",
                "aaa"
        );

        System.out.println("\nTest 4:");
        printResult(solver.getCompound(words4));


        // Test 5: no compound words
        List<String> words5 = Arrays.asList(
                "apple",
                "banana",
                "orange"
        );

        System.out.println("\nTest 5:");
        printResult(solver.getCompound(words5));


        // Test 6: longer compound
        List<String> words6 = Arrays.asList(
                "helloworldjava",
                "hello",
                "world",
                "java",
                "helloworld"
        );

        System.out.println("\nTest 6:");
        printResult(solver.getCompound(words6));
    }

    private static void printResult(List<List<String>> result) {
        if (result.isEmpty()) {
            System.out.println("No compound words");
            return;
        }

        for (List<String> decomposition : result) {
            System.out.println(decomposition);
        }
    }
}
