// Design a playlist from the DJservice and the Recommendation service to mix the songs .
// Given the list of 10 requirements printed on paper . 
// The core idea is to mix the songs coming from the Djservice and the recommendation service , 
// in a custom proportion or in a equal proportion . Filters can be applied based on the user preferances .

// Expectation is to write the production ready classes with proper syntax on paper .

import java.util.*;
public class PlaylistDJ {
    // requirements
    // 1. get songs from different services
    // 2. filter songs based on use preferance
    // 3. mix songs using different strategy
    // entity
    // 1. song - id name genre
    // 2. filter startegy interface 
    // boolean allow()
    // more filters implement
    // 3. mix strategy 
    // List<Song> mixSongs()
    // more mix strategy
    // 4. two sources
    // DJSource & recommendation source


    // =========================================================
    // Domain models
    // =========================================================

    public static class Song {
        private final int id;
        private final String name;
        private final String genre;

        public Song(int id, String name, String genre) {
            this.id = id;
            this.name = name;
            this.genre = genre;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getGenre() {
            return genre;
        }
    }

    public static class UserPreference {
        private final Set<String> preferredGenres;

        public UserPreference(Set<String> preferredGenres) {
            this.preferredGenres = preferredGenres;
        }

        public Set<String> getPreferredGenres() {
            return preferredGenres;
        }
    }

    // =========================================================
    // Filters
    // =========================================================

    public interface SongFilter {
        boolean allow(Song song, UserPreference preference);
    }

    public static class GenreFilter implements SongFilter {

        @Override
        public boolean allow(
                Song song,
                UserPreference preference) {

            if (preference == null
                    || preference.getPreferredGenres() == null
                    || preference.getPreferredGenres().isEmpty()) {
                return true;
            }

            return song.getGenre() != null
                    && preference.getPreferredGenres()
                                 .contains(song.getGenre());
        }
    }

    // =========================================================
    // External service abstractions
    // =========================================================

    public interface DJSource {
        List<Song> getSongs();
    }

    public interface RecommendationSource {
        List<Song> getSongs(String userId);
    }

    // =========================================================
    // Mixing strategies
    // =========================================================

    public interface MixStrategy {
        List<Song> mixSongs(
                List<Song> djSongs,
                List<Song> recommendationSongs,
                int size);
    }

    public static class ProportionMix implements MixStrategy {
        private final double djRatio;

        public ProportionMix(double djRatio) {
            if (djRatio < 0 || djRatio > 1) {
                throw new IllegalArgumentException(
                        "Ratio must be between 0 and 1"
                );
            }

            this.djRatio = djRatio;
        }

        @Override
        public List<Song> mixSongs(
                List<Song> djSongs,
                List<Song> recommendationSongs,
                int size) {

            List<Song> result = new ArrayList<>();
            Set<Integer> usedSongIds = new HashSet<>();

            int djTarget = (int) Math.round(size * djRatio);
            int recommendationTarget = size - djTarget;

            addSongs(
                    djSongs,
                    djTarget,
                    size,
                    result,
                    usedSongIds
            );

            addSongs(
                    recommendationSongs,
                    recommendationTarget,
                    size,
                    result,
                    usedSongIds
            );

            // If one source does not have enough songs,
            // fill the remaining positions from either source.
            addSongs(
                    djSongs,
                    size - result.size(),
                    size,
                    result,
                    usedSongIds
            );

            addSongs(
                    recommendationSongs,
                    size - result.size(),
                    size,
                    result,
                    usedSongIds
            );

            return result;
        }

        private void addSongs(
                List<Song> source,
                int count,
                int maxSize,
                List<Song> result,
                Set<Integer> usedSongIds) {

            if (source == null) {
                return;
            }

            for (Song song : source) {
                if (count == 0 || result.size() == maxSize) {
                    break;
                }

                if (song != null
                        && usedSongIds.add(song.getId())) {
                    result.add(song);
                    count--;
                }
            }
        }
    }

    public static class EqualMixingStrategy
            extends ProportionMix {

        public EqualMixingStrategy() {
            super(0.5);
        }
    }

    // =========================================================
    // Playlist generator
    // =========================================================

    public static class PlaylistGenerator {
        private final DJSource djSource;
        private final RecommendationSource recommendationSource;
        private final List<SongFilter> filters;

        public PlaylistGenerator(
                DJSource djSource,
                RecommendationSource recommendationSource,
                List<SongFilter> filters) {

            this.djSource = djSource;
            this.recommendationSource = recommendationSource;
            this.filters = filters == null
                    ? Collections.emptyList()
                    : filters;
        }

        public List<Song> generate(
                String userId,
                int size,
                MixStrategy mixStrategy,
                UserPreference preference) {

            List<Song> djSongs = filter(
                    djSource.getSongs(),
                    preference
            );

            List<Song> recommendationSongs = filter(
                    recommendationSource.getSongs(userId),
                    preference
            );

            return mixStrategy.mixSongs(
                    djSongs,
                    recommendationSongs,
                    size
            );
        }

        private List<Song> filter(
                List<Song> originalList,
                UserPreference preference) {

            List<Song> result = new ArrayList<>();

            if (originalList == null) {
                return result;
            }

            for (Song song : originalList) {
                boolean allowed = true;

                for (SongFilter filter : filters) {
                    if (!filter.allow(song, preference)) {
                        allowed = false;
                        break;
                    }
                }

                if (allowed) {
                    result.add(song);
                }
            }

            return result;
        }
    }
}