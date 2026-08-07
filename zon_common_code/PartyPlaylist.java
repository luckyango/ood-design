// assume there’s an API that returns a user’s liked songs. Create a playlist capable of 
// adding, removing users and also playing liked songs based on liked count. 
// The main constraint is that songs should not be repeated unless all songs have been played.

// public class PartyPlaylist {

//     // =========================================================
//     // Requirements
//     // =========================================================
//     // 1. Add/remove users from the party.
//     // 2. Get each user's liked songs from an external API.
//     // 3. Aggregate liked songs and maintain like count per song.
//     // 4. Play songs based on like count.
//     // 5. A song cannot be repeated until all available songs
//     //    have been played once.
//     //
//     // Clarifications:
//     // 1. When a user joins/leaves, should the change affect
//     //    the playlist immediately?
//     // 2. Does "based on like count" mean highest-like first
//     //    or weighted random?
//     // 3. Assume liked songs do not change while a user
//     //    stays in the party, or keep a snapshot.

//     // =========================================================
//     // Entities
//     // =========================================================

//     public static class User {
//         String id;
//     }

//     public static class Song {
//         String id;
//         String name;

//         public String getId() {
//             return id;
//         }

//         public String getName() {
//             return name;
//         }
//     }

//     // =========================================================
//     // External API / Source
//     // =========================================================

//     public static class LikedSongSource {

//         public List<Song> getLikedSongs(String userId) {
//             // Call external API and return the user's liked songs.
//             return null;
//         }
//     }

//     // =========================================================
//     // Playlist
//     // =========================================================

//     public static class PlaylistGenerator {

//         // Current users in the party.
//         Set<String> users;

//         // songId -> number of current users who like the song.
//         Map<String, Integer> songToLikedCount;

//         // songId -> Song object.
//         Map<String, Song> idToSong;

//         // userId -> songs contributed when the user joined.
//         // Used to correctly remove the user's contribution later.
//         Map<String, List<Song>> userLikedSnapshot;

//         // Current songs ordered by like count.
//         List<String> completePlaylist;

//         // Songs already played in the current round.
//         Set<String> playedSongs;

//         LikedSongSource likedSongSource;

//         int playIdx;

//         public PlaylistGenerator(LikedSongSource likedSongSource) {
//             // Initialize data structures and dependencies.
//         }

//         public void addUser(String userId) {
//             // 1. Ignore if the user already exists.
//             // 2. Fetch liked songs from external API.
//             // 3. Save the user's liked-song snapshot.
//             // 4. Increment like count for those songs.
//             // 5. Rebuild/reorder the playlist.
//         }

//         public void removeUser(String userId) {
//             // 1. Ignore if the user does not exist.
//             // 2. Get the user's saved liked-song snapshot.
//             // 3. Decrement like count for those songs.
//             // 4. Remove songs whose like count becomes zero.
//             // 5. Rebuild/reorder the playlist.
//         }

//         private void updateLikedCount(
//                 List<Song> likedSongs,
//                 int delta) {

//             // For each song:
//             // - update its like count by delta (+1 or -1)
//             // - maintain idToSong
//             // - remove songs whose count becomes zero
//         }

//         public Song playNext() {
//             // 1. Return null if there are no available songs.
//             // 2. Starting from playIdx, skip songs already played.
//             // 3. If all songs have been played:
//             //      clear playedSongs and start a new round.
//             // 4. Select the next highest-ranked unplayed song.
//             // 5. Mark it as played.
//             // 6. Advance playIdx and return the Song.
//             return null;
//         }

//         private void updatePlaylist() {
//             // 1. Build playlist from all songs with positive like count.
//             // 2. Sort songs by like count in descending order.
//             // 3. Reset playIdx because ranking may have changed.
//             //
//             // Already-played songs remain in playedSongs, so user
//             // changes can immediately affect the order without
//             // replaying songs in the same round.
//         }
//     }
// }
import java.util.*;
public class PartyPlaylist {
    // requirement -
    // 1. list of users  - need add/remove user => clarification: does the added song list affect the play list immediately?
    // 2. get the song list from one user
    // 3. integrate multiple song lists from all the users
    // 4. cant play repeat songs
    // entity
    // 1.User -  id
    // 2.Song - id, name
    // method: 
    // 3.Playlist Generator - Map<Song, liked count>
    // method: generate list based on liked count, play songs without duplicate
    // 4. api to return user's liked songs
    public static class User{
        String id; 
    }
    public static class Song{
        String id; String name;
        public String getId(){
            return id;
        }
        public String getName(){
            return name;
        }
    } 
    // external service - LikedSongService
    public static class LikedSongSource{
        public List<Song> getLikedSongs(String userId){
            // call external api
            return new ArrayList<>();
        }
    }
    public static class PlaylistGenerator {
        Set<String> users;
        Map<String, Integer> songToLikedCount;
        Map<String,Song> idToSong;
        List<String> completePlayList;
        Set<String> playedSongs;
        LikedSongSource likedSongSource;
        int playIdx ;
        public PlaylistGenerator(LikedSongSource likedSongSource){
            users = new HashSet<>();
            songToLikedCount = new HashMap<>();
            this.likedSongSource = likedSongSource;
            completePlayList = new ArrayList<>();
            playedSongs = new HashSet<>();
            idToSong = new HashMap<>();
            playIdx = 0;
        }
        // method 1: add user
        // add user in the list and update the count of each song
        // public void addUser(String userId){
        //     if(users.add(userId)){
        //         updateLikedCount(likedSongSource.getLikedSongs(userId),1);
        //     }
        // }
        // // method2: remove user in the list and update the count of each song
        // public void removeUser(String userId){
        //     if(users.remove(userId)){
        //         updateLikedCount(likedSongSource.getLikedSongs(userId),-1);
        //     }
        // }
        Map<String, List<Song>> userLikedSnapshot = new HashMap<>();

        public void addUser(String userId){
            if(users.add(userId)){
                List<Song> liked = likedSongSource.getLikedSongs(userId);
                userLikedSnapshot.put(userId, liked);
                updateLikedCount(liked, 1);
            }
        }

        public void removeUser(String userId){
            if(users.remove(userId)){
                List<Song> liked = userLikedSnapshot.remove(userId); // 用快照,不重新查询
                updateLikedCount(liked, -1);
            }
        }
        public void updateLikedCount(List<Song> likedSongs, int dlt){
            for(Song song: likedSongs){

                int newCount =
                        songToLikedCount.getOrDefault(song.getId(), 0) + dlt;

                if (newCount <= 0) {
                    songToLikedCount.remove(song.getId());
                    idToSong.remove(song.getId());
                } else {
                    songToLikedCount.put(song.getId(), newCount);
                    idToSong.put(song.getId(), song);
                }
            }
            // update the list
            updatePlayList();
        }
        // question, when user is added, do we need to update the list and play next according to the new list?
        // current solution: the song list from new user would immediately affect the playlist
        // method 3: play songs based on liked count without duplicate
        // 1. use a play list to record the current list (songs is sort by liked count)
        // 2. record which song is played already -> if the song is already played, skip; 
        // if all songs have been played, clear the played list, and we can start from the beginning again

        public Song playNext(){
            // boolean findSong = false;
            // case1: i reach the end of the list -> update it to 0
            // case2: the following songs are all played songs -> start from the beginning again
            if(completePlayList ==null || completePlayList.size()==0) return null;
            while(playIdx < completePlayList.size() && playedSongs.contains(completePlayList.get(playIdx))){
                // findSong = true; 
                //  ansName = completePlayList.get(playIdx);
                playIdx++;
            }
            // start from the beginning
            if(playIdx >= completePlayList.size()){
                playedSongs.clear();
                playIdx = 0;
            }
            String ansId = completePlayList.get(playIdx);
            playedSongs.add(ansId);
            playIdx++;
            return idToSong.get(ansId);
        }
        public void updatePlayList(){
            completePlayList.clear();
            completePlayList.addAll(songToLikedCount.keySet());
            Collections.sort(completePlayList, (a,b)->songToLikedCount.get(b)-songToLikedCount.get(a));
            playIdx=0;
        }
    }
}
