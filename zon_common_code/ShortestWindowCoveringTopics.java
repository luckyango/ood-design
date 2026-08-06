//  Parse log file for ip address: find shortest window of covering all topics
// log file format was:
// "timestamp" "ip" "topic" .....
// Input was: int findshortestWindow(String[][] tokenizedlogfile, string ip, new string[] { topic1, topic2, topic3})

// 1  ip1  topic1
// 4  ip1  topic2
// 5  ip1  topic2
// 7  ip1  topic3
// 8  ip1  topic3
// 9  ip1  topic1
// There are 2 windows 7 -1= 6,  9-4 = 5
// The answer should be 5

// Done

        // find the min window covering all the topics with the same ip
        // 1.edge case: (1) input valid (2) what if no log is valid -> return ?
        // 2.solution: sliding window
        // left, right
        // if the topics in the current window didnt cover all the required topic -> expand the window
        // if cover all -> update the answer and try to shrink the window to find a smaller window
        // for each log, need to check if the ip equals, and then if the topics is what we want 

        // 1.use a set to store the required topics -> O(1) lookup
        // 2.use a map to store the topics & freq in the window
        // 3.shrink/expand the window
import java.util.*;

public class ShortestWindowCoveringTopics {

    public long findShortestWindow(
            String[][] tokenizedLogFile,
            String targetIp,
            String[] topics) {

        if (tokenizedLogFile == null
                || targetIp == null
                || topics == null
                || topics.length == 0) {
            return -1;
        }

        Set<String> requiredTopics = new HashSet<>(Arrays.asList(topics));

        // Store only logs that match both the target IP and required topics.
        // Each element is: [timestamp, topic]
        List<String[]> relevantLogs = new ArrayList<>();

        for (String[] log : tokenizedLogFile) {
            if (log == null || log.length < 3) {
                continue;
            }

            String ip = log[1];
            String topic = log[2];

            if (targetIp.equals(ip) && requiredTopics.contains(topic)) {
                relevantLogs.add(new String[]{log[0], topic});
            }
        }

        Map<String, Integer> windowCount = new HashMap<>();
        int left = 0;
        long minWindow = Long.MAX_VALUE;

        for (int right = 0; right < relevantLogs.size(); right++) {
            String rightTopic = relevantLogs.get(right)[1];

            windowCount.put(
                    rightTopic,
                    windowCount.getOrDefault(rightTopic, 0) + 1
            );

            // map.size() equals the number of distinct topics
            // currently covered by the window.
            while (windowCount.size() == requiredTopics.size()) {
                long startTime =
                        Long.parseLong(relevantLogs.get(left)[0]);
                long endTime =
                        Long.parseLong(relevantLogs.get(right)[0]);

                minWindow = Math.min(minWindow, endTime - startTime);

                String leftTopic = relevantLogs.get(left)[1];
                int newCount = windowCount.get(leftTopic) - 1;

                if (newCount == 0) {
                    windowCount.remove(leftTopic);
                } else {
                    windowCount.put(leftTopic, newCount);
                }

                left++;
            }
        }

        return minWindow == Long.MAX_VALUE ? -1 : minWindow;
    }

    public static void main(String[] args) {
        String[][] logs = {
                {"1", "ip1", "topic1"},
                {"4", "ip1", "topic2"},
                {"5", "ip1", "topic2"},
                {"7", "ip1", "topic3"},
                {"8", "ip1", "topic3"},
                {"9", "ip1", "topic1"}
        };

        ShortestWindowCoveringTopics solution =
                new ShortestWindowCoveringTopics();

        long result = solution.findShortestWindow(
                logs,
                "ip1",
                new String[]{"topic1", "topic2", "topic3"}
        );

        System.out.println(result); // 5
    }
}
