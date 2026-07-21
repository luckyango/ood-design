// Given an array of strings, you need to group isomorphic strings together.

// Example:

// Input: ["apple", "apply", "dog", "cog", "romi"]
// Output: [["dog", "cog"], ["romi"], ["apple", "apply"]]
// import java.util.*;
// // solution 1 - time complexity - O(n^2)
// class GroupIsomorphicStrings {

//     public List<List<String>> groupStrings(List<String> words) {
//         List<List<String>> result = new ArrayList<>();
//         boolean[] grouped = new boolean[words.size()];

//         for (int i = 0; i < words.size(); i++) {
//             if (grouped[i]) {
//                 continue;
//             }

//             List<String> currentGroup = new ArrayList<>();
//             currentGroup.add(words.get(i));
//             grouped[i] = true;

//             for (int j = i + 1; j < words.size(); j++) {
//                 if (!grouped[j]
//                         && checkIsomorphic(words.get(i), words.get(j))) {
//                     currentGroup.add(words.get(j));
//                     grouped[j] = true;
//                 }
//             }

//             result.add(currentGroup);
//         }

//         return result;
//     }

//     private boolean checkIsomorphic(String w1, String w2) {
//         if (w1.length() != w2.length()) {
//             return false;
//         }

//         int[] map1 = new int[256];
//         int[] map2 = new int[256];

//         for (int i = 0; i < w1.length(); i++) {
//             char c1 = w1.charAt(i);
//             char c2 = w2.charAt(i);

//             if (map1[c1] == 0 && map2[c2] == 0) {
//                 map1[c1] = c2;
//                 map2[c2] = c1;
//             } else if (map1[c1] != c2 || map2[c2] != c1) {
//                 return false;
//             }
//         }

//         return true;
//     }
// }
// solution 2 - O(n)
import java.util.*;

class GroupIsomorphicStrings {

    public List<List<String>> groupStrings(List<String> words) {
        Map<String, List<String>> groups = new LinkedHashMap<>();

        for (String word : words) {
            String pattern = getPattern(word);

            groups.computeIfAbsent(pattern, key -> new ArrayList<>())
                  .add(word);
        }

        return new ArrayList<>(groups.values());
    }

    private String getPattern(String word) {
        Map<Character, Integer> indexMap = new HashMap<>();
        StringBuilder pattern = new StringBuilder();
        int nextId = 0;

        for (char c : word.toCharArray()) {
            if (!indexMap.containsKey(c)) {
                indexMap.put(c, nextId++);
            }

            pattern.append(indexMap.get(c)).append('#');
        }

        return pattern.toString();
    }
    public static void main(String[] args) {
        GroupIsomorphicStrings solution = new GroupIsomorphicStrings();

        List<String> words = Arrays.asList(
                "apple",
                "apply",
                "dog",
                "cog",
                "romi"
        );

        List<List<String>> result = solution.groupStrings(words);

        System.out.println("Input:");
        System.out.println(words);

        System.out.println("\nGroups:");
        for (List<String> group : result) {
            System.out.println(group);
        }

        System.out.println("\nPatterns:");
        for (String word : words) {
            System.out.println(
                    word + " -> " + solution.getPattern(word)
            );
        }
    }
}