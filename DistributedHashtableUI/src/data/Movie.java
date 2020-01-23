package data;

import java.util.Arrays;

public class Movie {
	
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
	
	
	/**
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
	*/






	public int getMovieID() {
		return movieID;
	}


	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public double getImdbScore() {
		return imdbScore;
	}


	public void setImdbScore(double imdbScore) {
		this.imdbScore = imdbScore;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDirector() {
		return director;
	}


	public void setDirector(String director) {
		this.director = director;
	}


	public String[] getActor() {
		return actor;
	}


	public void setActor(String[] actor) {
		this.actor = actor;
	}


	public String getImdbLink() {
		return imdbLink;
	}


	public void setImdbLink(String imdbLink) {
		this.imdbLink = imdbLink;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getContentRating() {
		return contentRating;
	}


	public void setContentRating(String contentRating) {
		this.contentRating = contentRating;
	}


	@Override
	public String toString() {
		return "Movie [movieID=" + movieID + ", duration=" + duration + ", year=" + year + ", imdbScore=" + imdbScore
				+ ", color=" + color + ", title=" + title + ", director=" + director + ", actor="
				+ Arrays.toString(actor) + ", imdbLink=" + imdbLink + ", language=" + language + ", country=" + country
				+ ", contentRating=" + contentRating + "]";
	}
	
	
	
	

}
