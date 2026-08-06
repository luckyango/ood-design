// Design a system to track how many people are in the office at any given time.
// Follow-ups:
// Design queries to return the number of people at a specific timestamp.
// Find the maximum number of people during a time interval.
import java.util.*;
public class TrackPeopleNum {
    // requirement
    // 1. track how many people come in at one timestamp
    // 2. track how many people leave at one timestamp
    // 3. find the number of people at a given time
    // untity
    // 1.enterRecord - people id, timestamp
    // 2.leave record - people id, timestamp
    // 3. a list to record the final people at one timestamp
    // for example: enter, 
    // method
    // 1.number of people at a given time
    // 2.max number of people until now
    // 3.max number of people during a time interval
    // 4. the people stay for the longest time 

    // ENTER = +1, EXIT = -1

    public static class Event {
        long timestamp;
        int change; // ENTER = 1, EXIT = -1

        public Event(long timestamp, int change) {
            this.timestamp = timestamp;
            this.change = change;
        }
    }

    public static class Record {
        long timestamp;
        int count;

        public Record(long timestamp, int count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }

    // =====================================================
    // Version 1: Offline
    // All events are provided at the beginning.
    // =====================================================
    public static class OfflineTracker {

        private final List<Record> history = new ArrayList<>();

        public OfflineTracker(List<Event> events) {
            events.sort(
                    Comparator.comparingLong(event -> event.timestamp)
            );

            int currentCount = 0;

            for (Event event : events) {
                currentCount += event.change;
                addRecord(event.timestamp, currentCount);
            }
        }

        private void addRecord(long timestamp, int count) {
            if (!history.isEmpty()
                    && history.get(history.size() - 1).timestamp == timestamp) {

                // Keep the final count at this timestamp.
                history.get(history.size() - 1).count = count;
            } else {
                history.add(new Record(timestamp, count));
            }
        }

        public int getCountAt(long timestamp) {
            int index = findLastTimestampAtMost(timestamp);

            return index == -1 ? 0 : history.get(index).count;
        }

        public int getMaxCount(long startTime, long endTime) {
            if (startTime > endTime) {
                throw new IllegalArgumentException("Invalid interval");
            }

            // Occupancy at the beginning of the interval.
            int maxCount = getCountAt(startTime);

            // Start checking from the first timestamp > startTime.
            int index = findFirstTimestampGreaterThan(startTime);

            while (index < history.size()
                    && history.get(index).timestamp <= endTime) {

                maxCount = Math.max(
                        maxCount,
                        history.get(index).count
                );

                index++;
            }

            return maxCount;
        }

        private int findLastTimestampAtMost(long target) {
            int left = 0;
            int right = history.size() - 1;
            int answer = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (history.get(mid).timestamp <= target) {
                    answer = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            return answer;
        }

        private int findFirstTimestampGreaterThan(long target) {
            int left = 0;
            int right = history.size();

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (history.get(mid).timestamp <= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left;
        }
    }

    // =====================================================
    // Version 2: Ordered stream
    // Events arrive one by one in chronological order.
    // =====================================================
    public static class StreamTracker {

        private final List<Record> history = new ArrayList<>();

        private int currentCount = 0;
        private long lastTimestamp = Long.MIN_VALUE;

        public void enter(long timestamp) {
            recordEvent(timestamp, 1);
        }

        public void exit(long timestamp) {
            recordEvent(timestamp, -1);
        }

        private void recordEvent(long timestamp, int change) {
            if (timestamp < lastTimestamp) {
                throw new IllegalArgumentException(
                        "Events must arrive in chronological order"
                );
            }

            currentCount += change;
            addRecord(timestamp, currentCount);

            lastTimestamp = timestamp;
        }

        private void addRecord(long timestamp, int count) {
            if (!history.isEmpty()
                    && history.get(history.size() - 1).timestamp == timestamp) {

                // Multiple events at the same timestamp:
                // keep only the final count.
                history.get(history.size() - 1).count = count;
            } else {
                history.add(new Record(timestamp, count));
            }
        }

        public int getCurrentCount() {
            return currentCount;
        }

        public int getCountAt(long timestamp) {
            int index = findLastTimestampAtMost(timestamp);

            return index == -1 ? 0 : history.get(index).count;
        }

        public int getMaxCount(long startTime, long endTime) {
            if (startTime > endTime) {
                throw new IllegalArgumentException("Invalid interval");
            }

            int maxCount = getCountAt(startTime);
            int index = findFirstTimestampGreaterThan(startTime);

            while (index < history.size()
                    && history.get(index).timestamp <= endTime) {

                maxCount = Math.max(
                        maxCount,
                        history.get(index).count
                );

                index++;
            }

            return maxCount;
        }

        private int findLastTimestampAtMost(long target) {
            int left = 0;
            int right = history.size() - 1;
            int answer = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;

                if (history.get(mid).timestamp <= target) {
                    answer = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            return answer;
        }

        private int findFirstTimestampGreaterThan(long target) {
            int left = 0;
            int right = history.size();

            while (left < right) {
                int mid = left + (right - left) / 2;

                if (history.get(mid).timestamp <= target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            return left;
        }
    }

    public static void main(String[] args) {

        // ---------------- Offline ----------------

        List<Event> events = new ArrayList<>(Arrays.asList(
                new Event(1, 1),
                new Event(4, 1),
                new Event(4, 1),
                new Event(7, -1),
                new Event(9, 1),
                new Event(12, -1)
        ));

        OfflineTracker offline = new OfflineTracker(events);

        System.out.println(offline.getCountAt(5));       // 3
        System.out.println(offline.getMaxCount(5, 10)); // 3

        // ---------------- Ordered stream ----------------

        StreamTracker stream = new StreamTracker();

        stream.enter(1);
        stream.enter(4);
        stream.enter(4);
        stream.exit(7);
        stream.enter(9);
        stream.exit(12);

        System.out.println(stream.getCurrentCount());    // 3
        System.out.println(stream.getCountAt(5));        // 3
        System.out.println(stream.getMaxCount(5, 10));   // 3
    }


}
