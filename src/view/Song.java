package view;

public class Song implements Comparable<Song> {

	String name;
	String artist;
	String album;
	String year;

	/*
	 * Every Song object needs at least a name and an artist
	 * Album and year properties are optional
	 */
	
	public Song() {
		this.name = "";
		this.artist = "";
		this.album = "optional";
		this.year = "optional";
	}
	
	public Song(String name, String artist) {
		this.name = name;
		this.artist = artist;
		this.album = "";
		this.year = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String toString() {
		return getName() + ":\n" + getArtist();
	}
	@Override
	public int compareTo(Song arg0) {
		// TODO Auto-generated method stub
		return name.compareToIgnoreCase(arg0.name);
	}
	
	public boolean equals(Object object) {
		if(object == null) {
			return false;
		}
		
		Song song = (Song)object;
		
		int sName = name.compareToIgnoreCase(song.name);
		int sArtist = artist.compareToIgnoreCase(song.artist);
		
		return ((sName == sArtist) && (sName == 0));
		
	}


}
