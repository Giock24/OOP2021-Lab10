package it.unibo.oop.lab.lambda.ex02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {	
    	final Set<String> mySong = new HashSet<>();
    	this.songs.forEach(t -> {
    		mySong.add(t.getSongName());
    	});
    	//System.out.println(mySong);
        return mySong.stream().sorted();
    }

    @Override
    public Stream<String> albumNames() {
    	final Set<String> myAlbum = new HashSet<>();
    	this.albums.forEach((k, v) -> {
    		myAlbum.add(k);
    	});
        return myAlbum.stream();
    }

    @Override
    public Stream<String> albumInYear(final int year) {
    	final Set<String> albumYear = new HashSet<>();
    	this.albums.forEach((k, v) -> {
    		if (v.equals(year)) {
    			albumYear.add(k);
    		}
    	});
        return albumYear.stream();
    }

    @Override
    public int countSongs(final String albumName) {
    	final Set<String> counterSong = new HashSet<>();
    	this.songs.forEach(t -> {
    		if (t.albumName.filter(name -> name.equals(albumName)).isPresent()) {
    			counterSong.add(t.getSongName());
    		}
    	});
    	//System.out.println(counterSong);
        return (int) counterSong.stream().count();
    }

    @Override
    public int countSongsInNoAlbum() {
    	final Set<String> countSongNoAlbum = new HashSet<>();
    	this.songs.forEach(t -> {
    		if (t.albumName.isEmpty()) {
    			countSongNoAlbum.add(t.getSongName());
    		}
    	});
        return (int) countSongNoAlbum.stream().count();
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
    	final Set<Double> averageDurSong = new HashSet<>();
    	this.songs.forEach(t -> {
    		if (t.albumName.filter(name -> name.equals(albumName)).isPresent()) {
    			averageDurSong.add(t.duration);
    		}
    	});
        return OptionalDouble.of(averageDurSong.stream().collect(Collectors.averagingDouble(x -> x)));
    }

    @Override
    public Optional<String> longestSong() {
    	final Map<Optional<Double>, String> longSong = new HashMap<>();
    	final Set<Double> durSong = new HashSet<>();
    	this.songs.forEach(t -> {
    		longSong.put(Optional.of(t.getDuration()), t.getSongName());
    		durSong.add(t.getDuration());
    	});
    	Optional<Double> max = durSong.stream().max((Double::compare));
    	Optional<String> name = Optional.ofNullable(longSong.get(max));
    	//System.out.println(name);
        return name;
    }

    @Override
    public Optional<String> longestAlbum() {
    	final Map<Optional<Double>, String> longAlbum = new HashMap<>();
    	final Set<Double> durAlbum = new HashSet<>();
    	this.albums.forEach((k, v) -> {
    		longAlbum.put(Optional.of(averageDurationOfSongs(k).getAsDouble()), k);
    		durAlbum.add(averageDurationOfSongs(k).getAsDouble());
    	});
    	Optional<Double> max = durAlbum.stream().max((Double::compare));
    	Optional<String> name = Optional.ofNullable(longAlbum.get(max));
    	//System.out.println(name);
        return name;
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        public String getSongName() {
            return songName;
        }

        public Optional<String> getAlbumName() {
            return albumName;
        }

        public double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
