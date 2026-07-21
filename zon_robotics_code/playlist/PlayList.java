//  given a getPlaylist(userName) method, write a next() method for a ipod.
//  The next method would play the next most frequent played song. So basically the input was - 
//  [(a,10), (b,20), (c,5)]. So b should be played first and then a and then c.
//  Followup question - how would you manage if the playlist was for a party and the playlist 
//  should get updated with any user entering or leaving the party basically 
//  if user x, y and z are in the party and have a playlist like - 
//  [(a,10), (b,20)], [(a,20), (b,15), ], [(a,10), (b, 10), (c,50)] 
//  then the consolidated/party playlist should look like and have the order - [(c,50), (b,45), (a,40)]
// I suggested we could use priority queue to keep the aggregated list sorted but everytime a user 
// left/entered the party then updating the list would take O(n) time and 
// sorting it again would take O(nlogn) which isn't great. I couldn't come up with a better solution 
// in that time. Probably this is what got me rejected.
import java.util.*;
class PlayList{
    class Song{
        String name; int frequency;
        public Song(String name, int frequency){
            this.name = name; this.frequency = frequency;
        }
    }
    public List<Song> getPlayList(String userName){
        Song s1 = new Song("a",10);
        return Arrays.asList(s1);
    }
    List<Song> songs = getPlayList("a");
    public String next(String userName){
        // next most freqyent played song
        List<Song> songs = getPlayList(userName);
        Song maxFreqSong = new Song("a", 0);
        
        for(Song s:songs){
            if(s.frequency > maxFreqSong.frequency) maxFreqSong = s;
        }
        return maxFreqSong.name;
    }
}


// 设计思路
// 这道题分两层:

// 静态排序——给定一份 (song, count) 列表,按播放次数从高到低播放。
// 动态聚合——多个用户的播放列表实时合并,并且要支持用户加入/离开时的增量更新,而不是每次都全量重算。

// 第二问才是考察重点(类似 LFU Cache 的变种),第一问主要是热身。

// Part 1: 单用户 next()
// 关键问题: 播放次数是否会在播放过程中变化?
// 如果只是"启动时排一次序,然后按顺序播放/循环",最简单高效的方式是排序一次 + 指针遍历,不需要堆:
// javaclass Song {
//     String name;
//     int count;
//     Song(String name, int count) { this.name = name; this.count = count; }
// }

// class MusicPlayer {
//     private List<Song> sorted;
//     private int idx = 0;

//     public MusicPlayer(String userName) {
//         sorted = new ArrayList<>(getPlaylist(userName));
//         // 相同 count 时按名字兜底排序,保证结果确定性
//         sorted.sort((a, b) -> b.count != a.count 
//                               ? b.count - a.count 
//                               : a.name.compareTo(b.name));
//     }

//     public Song next() {
//         if (sorted.isEmpty()) return null;
//         Song s = sorted.get(idx);
//         idx = (idx + 1) % sorted.size(); // 像iPod一样循环
//         return s;
//     }
// }

// 初始化 O(n log n),每次 next() O(1)。
// 如果面试官追问"如果播放次数会实时更新怎么办"(比如播一首歌它的 count 就 +1,顺序可能变化),那就需要换成支持 increase-key 的结构,比如下面 Part 2 用到的"TreeMap 分桶"方案——这也是为什么我在 Part 2 直接用这个更通用的结构,可以自然地引出 follow-up。


// Part 2: 派对合并播放列表(核心考点)
// 需求拆解

// 多个用户的播放列表需要按歌曲名聚合播放次数。
// 用户加入 → 把他的播放列表累加进总数。
// 用户离开 → 把他的播放列表扣除出总数。
// 每次都要能拿到"当前最热门歌曲",不能每次加入/离开都 O(n log n) 重新排序全部数据。

// 数据结构:频次分桶(类似 LFU Cache 的设计)
// songCount:     song -> 当前总播放次数        (HashMap)
// countToSongs:  count -> 该次数下的歌曲集合    (TreeMap<Integer, LinkedHashSet<String>>)
// activeUsers:   userId -> 该用户原始播放列表   (用于 leave 时反向扣减)

// TreeMap 让"次数最高的桶"变成 O(1) 或 O(log C) 可取(lastEntry()),而不是每次全量排序,C 是不同次数值的个数,通常远小于歌曲数 n。
// 这本质上是"桶排序 + 可增量更新"的思路。

// javaclass PartyPlaylist {
//     private Map<String, Integer> songCount = new HashMap<>();
//     private TreeMap<Integer, LinkedHashSet<String>> countToSongs = new TreeMap<>();
//     private Map<String, List<Song>> activeUsers = new HashMap<>();

//     public void userJoin(String userId, List<Song> playlist) {
//         activeUsers.put(userId, playlist);
//         for (Song s : playlist) adjustCount(s.name, s.count);
//     }

//     public void userLeave(String userId) {
//         List<Song> playlist = activeUsers.remove(userId);
//         if (playlist == null) return;
//         for (Song s : playlist) adjustCount(s.name, -s.count);
//     }

//     private void adjustCount(String song, int delta) {
//         int oldCount = songCount.getOrDefault(song, 0);
//         int newCount = oldCount + delta;

//         if (oldCount != 0) {
//             LinkedHashSet<String> bucket = countToSongs.get(oldCount);
//             bucket.remove(song);
//             if (bucket.isEmpty()) countToSongs.remove(oldCount);
//         }

//         if (newCount <= 0) {
//             songCount.remove(song);
//         } else {
//             songCount.put(song, newCount);
//             countToSongs.computeIfAbsent(newCount, k -> new LinkedHashSet<>()).add(song);
//         }
//     }

//     // 拿当前最热门歌曲(不消费)
//     public String peekTop() {
//         if (countToSongs.isEmpty()) return null;
//         return countToSongs.lastEntry().getValue().iterator().next();
//     }

//     // 拿当前完整排序快照,用于展示 / next() 遍历
//     public List<Map.Entry<String, Integer>> currentOrder() {
//         List<Map.Entry<String, Integer>> result = new ArrayList<>();
//         for (var e : countToSongs.descendingMap().entrySet()) {
//             for (String song : e.getValue()) {
//                 result.add(Map.entry(song, e.getKey()));
//             }
//         }
//         return result;
//     }
// }
// next() 在动态列表上怎么做
// 问题:如果播放到一半有人加入/离开,"下一首"该怎么算?不能简单地重放已经放过的歌。做法是维护一个当前循环内已播放集合,每次 next() 都基于当前实时的 countToSongs去找第一个还没播过的歌:
// javaprivate Set<String> playedThisCycle = new HashSet<>();

// public String next() {
//     for (var e : countToSongs.descendingMap().entrySet()) {
//         for (String song : e.getValue()) {
//             if (!playedThisCycle.contains(song)) {
//                 playedThisCycle.add(song);
//                 return song;
//             }
//         }
//     }
//     playedThisCycle.clear();  // 一轮播完,重置
//     return next();
// }
// 这样即使中途有人加入/离开导致顺序变化,next() 每次都基于最新状态取值,天然兼容动态更新,不需要额外同步逻辑。

// 复杂度与权衡(面试官大概率会追问)
// 操作复杂度说明userJoin/userLeaveO(k log C)k=该用户歌曲数,C=不同次数值个数peekTopO(1)~O(log C)TreeMap.lastEntry()next() 最坏情况O(n)遍历完所有桶才找到未播放的歌;可以用"桶内指针+跳过已播放"进一步优化,避免每次全扫
// 替代方案对比(可以主动提出,展示广度):

// 懒删除堆(lazy-deletion max-heap):把 (count, song, version) 塞进堆,pop 时校验是否是最新值,不是就丢弃重 pop。实现更简单,但堆里会堆积过期条目,长期运行需要定期清理,内存占用不如 TreeMap 分桶稳定。
// 重新全量排序:每次 join/leave 都 O(n log n) 重排。实现最简单,但在"party 场景人来人往很频繁"时明显效率太差,面试官问这个 follow-up 大概率就是想看你能不能避免这种暴力方案。

// 可以主动提的边界情况

// 并发:多个用户同时 join/leave,countToSongs/songCount 需要加锁或用并发安全结构(比如 ConcurrentSkipListMap + 分段锁),不然会有竞态。
// 同分处理:count 相同时用什么做 tie-break(字母序/加入时间),需要在 LinkedHashSet 或排序 comparator 里显式定义,否则结果不确定。
// 用户离开时数据一致性:必须保存原始 playlist 快照用于扣减,而不是"重新查一次这个用户当前的播放列表"——否则如果他在party期间自己听歌导致count变化,扣减值就对不上,会导致总数出现负数或漂移。

// 这个设计的核心亮点就是把"多用户实时聚合 + 高效取最大值"转化成了经典的 LFU Cache 分桶模式,是这题的最佳落脚点,建议面试时明确点出这个联系。

// Part 1 讲解(静态排序)
// "Let me start with the simple case.
// Given a list of (song, count) pairs, I just need to sort once and iterate. So in the constructor, I sort the playlist descending by count — with a tie-breaker, like alphabetical order, in case two songs have the same count, just to make the ordering deterministic.
// Then next() just returns the current index and advances a pointer, wrapping around like a real iPod would. That's O(n log n) once at setup, and O(1) per call.
// One thing I want to clarify — is the play count static, or does it update in real time as songs get played? If it's static, this simple sorted-list approach is enough. If counts change dynamically, I'd want a data structure that supports efficient re-ranking, which is actually exactly what the follow-up is asking about — so let me jump into that."
// Part 2 讲解(动态派对播放列表 — 核心)
// "So now the challenge is: multiple users' playlists need to be merged by song name, and the merged totals need to update efficiently whenever a user joins or leaves — without re-sorting everything from scratch each time.
// This is essentially the same problem as an LFU cache — songs need to move between 'frequency buckets' as their count changes, and I need fast access to the current maximum.
// Here's the data structure I'd use:

// A HashMap<song, count> — the current total play count per song.
// A TreeMap<count, Set<song>> — grouping songs by their current count, so I can jump to the highest bucket without scanning everything.
// A Map<userId, playlist> — storing each user's original playlist, so when they leave, I know exactly what to subtract.

// When a user joins: for every song in their playlist, I add their count to the running total for that song. That means removing the song from its old bucket in the TreeMap and inserting it into the new bucket for the updated count.
// When a user leaves: same thing, but I look up their playlist and subtract instead of add. If a song's count hits zero, I remove it from both maps.
// To get the current top song: I just call lastEntry() on the TreeMap and grab any song from that bucket — that's O(1) amortized, or O(log C) where C is the number of distinct count values, which is usually much smaller than the number of songs.
// For next() on this dynamic structure — since the ranking can change mid-playback if someone joins or leaves, I keep a 'played this cycle' set, and each call walks the TreeMap from the top bucket down, returning the first song not yet played in this cycle. Once everything's been played, I clear the set and start a new cycle. This way next() always reflects the live state, and I don't need to manually re-sync anything when users join or leave — it just reads off whatever the TreeMap currently looks like.
// Complexity-wise: join/leave is O(k log C) where k is the number of songs that user brought. Getting the top song is close to O(1). Worst case for next() is O(n) if it has to scan through many buckets, but that could be optimized further with per-bucket iterators if needed.
// One alternative I considered was a lazy-deletion max-heap — push (count, song, version) tuples, and when popping, check if the entry is stale and discard it if so. That's simpler to implement, but heap entries pile up over time and need periodic cleanup, whereas the TreeMap approach keeps state clean at all times — which matters more in a long-running party scenario with lots of joins and leaves.
// A couple of edge cases I'd want to flag: first, tie-breaking when two songs have the same count — I'd need a deterministic secondary sort, like alphabetical or join order. Second, concurrency — if multiple users can join or leave at the same time, I'd need thread-safety, maybe a ConcurrentSkipListMap or a lock around the update path. And third, when a user leaves, I subtract based on the snapshot of their playlist I stored at join time — not by re-querying their live playlist — otherwise the numbers could drift if their own listening habits changed the counts on my side."**