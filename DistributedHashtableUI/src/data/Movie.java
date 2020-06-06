package data;

import java.util.Arrays;

// for the data set movie_metadata.csv
public class Movie {
	
	//fields
	private int movieID;	
	private int duration;
	private int year;
	private double imdbScore;
	private String color;
	private String title;
	private String director;
	private String[] actor;
	private String imdbLink;
	private String language;
	private String country;
	private String contentRating;
	
	//constructor
	public Movie(int movieID, String title, String color, int duration, String director, 
			String[] actor, String imdbLink, String language, 
			String country, String contentRating, int year, double imdbScore) {
		
				this.movieID = movieID;
				this.title = title;
				this.color = color;
				this.duration = duration;
				this.director = director;
				this.actor = actor;
				this.imdbLink = imdbLink;
				this.language = language;
				this.country = country;
				this.contentRating = contentRating;
				this.year = year;
				this.imdbScore = imdbScore;
	}
	
	//gets movie ID
	public int getMovieID() {
		return movieID;
	}

	// sets movie ID
	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	//gets duration
	public int getDuration() {
		return duration;
	}

	//sets duration
	public void setDuration(int duration) {
		this.duration = duration;
	}

	//gets year
	public int getYear() {
		return year;
	}

	//sets year
	public void setYear(int year) {
		this.year = year;
	}

	//gets score
	public double getImdbScore() {
		return imdbScore;
	}

	//sets score
	public void setImdbScore(double imdbScore) {
		this.imdbScore = imdbScore;
	}

	//gets color
	public String getColor() {
		return color;
	}

	//sets color
	public void setColor(String color) {
		this.color = color;
	}

	//gets title
	public String getTitle() {
		return title;
	}

	//sets title
	public void setTitle(String title) {
		this.title = title;
	}

	//gets director
	public String getDirector() {
		return director;
	}

	//sets director
	public void setDirector(String director) {
		this.director = director;
	}

	//gets actor
	public String[] getActor() {
		return actor;
	}

	//sets actor
	public void setActor(String[] actor) {
		this.actor = actor;
	}

	//gets link
	public String getImdbLink() {
		return imdbLink;
	}

	//sets link
	public void setImdbLink(String imdbLink) {
		this.imdbLink = imdbLink;
	}

	//gets language
	public String getLanguage() {
		return language;
	}

	//sets language
	public void setLanguage(String language) {
		this.language = language;
	}

	//gets country
	public String getCountry() {
		return country;
	}

	//sets country
	public void setCountry(String country) {
		this.country = country;
	}

	//gets ratings
	public String getContentRating() {
		return contentRating;
	}

	//sets rating
	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}

	//toString method
	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", duration=" + duration + ", year=" + year + ", imdbScore=" + imdbScore
				+ ", color=" + color + ", title=" + title + ", director=" + director + ", actor="
				+ Arrays.toString(actor) + ", imdbLink=" + imdbLink + ", language=" + language + ", country=" + country
				+ ", contentRating=" + contentRating + "]";
	}

}
