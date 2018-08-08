package com.andyshon.moviedb.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.andyshon.moviedb.data.entity.converters.ListTypeConverters_companies;
import com.andyshon.moviedb.data.entity.converters.ListTypeConverters_countries;
import com.andyshon.moviedb.data.entity.converters.ListTypeConverters_genres;
import com.andyshon.moviedb.data.entity.converters.ListTypeConverters_languages;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andyshon on 08.08.18.
 */

@Entity(tableName = "MovieSearchResult")
public class MovieSearchResult {

    public MovieSearchResult() {}

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("backdrop_path")
    private String backdrop_path;


    @Embedded(prefix = "belongsToCollection_")
    @SerializedName("belongs_to_collection")
    private MovieSearchResult_belongsToCollection movieSearchResultbelongsToCollection;


    @SerializedName("budget")
    private double budget;


    @TypeConverters(ListTypeConverters_genres.class)
    @SerializedName("genres")
    private List<MovieSearchResult_genres> genres;


    @SerializedName("homepage")
    private String homepage;

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private int id;


    @SerializedName("imdb_id")
    private String imdb_id;



    @SerializedName("original_language")
    private String original_language;



    @SerializedName("original_title")
    private String original_title;



    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String poster_path;

    @TypeConverters(ListTypeConverters_companies.class)
    @SerializedName("production_companies")
    private List<MovieSearchResult_productionCompanies> movieSearchResultproductionCompanies;

    @TypeConverters(ListTypeConverters_countries.class)
    @SerializedName("production_countries")
    private List<MovieSearchResult_productionCountries> movieSearchResultproductionCountries;

    @SerializedName("release_date")
    private String release_date;

    @SerializedName("revenue")
    private double revenue;

    @SerializedName("runtime")
    private int runtime;

    @TypeConverters(ListTypeConverters_languages.class)
    @SerializedName("spoken_languages")
    private List<MovieSearchResult_spokenLanguages> movieSearchResultspokenLanguages;

    @SerializedName("status")
    private String status;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double vote_average;

    @SerializedName("vote_count")
    private double vote_count;


    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public MovieSearchResult_belongsToCollection getMovieSearchResultbelongsToCollection() {
        return movieSearchResultbelongsToCollection;
    }

    public void setMovieSearchResultbelongsToCollection(MovieSearchResult_belongsToCollection movieSearchResultbelongsToCollection) {
        this.movieSearchResultbelongsToCollection = movieSearchResultbelongsToCollection;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<MovieSearchResult_genres> getGenres() {
        return genres;
    }

    public void setGenres(List<MovieSearchResult_genres> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<MovieSearchResult_productionCompanies> getMovieSearchResultproductionCompanies() {
        return movieSearchResultproductionCompanies;
    }

    public void setMovieSearchResultproductionCompanies(List<MovieSearchResult_productionCompanies> movieSearchResultproductionCompanies) {
        this.movieSearchResultproductionCompanies = movieSearchResultproductionCompanies;
    }

    public List<MovieSearchResult_productionCountries> getMovieSearchResultproductionCountries() {
        return movieSearchResultproductionCountries;
    }

    public void setMovieSearchResultproductionCountries(List<MovieSearchResult_productionCountries> movieSearchResultproductionCountries) {
        this.movieSearchResultproductionCountries = movieSearchResultproductionCountries;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<MovieSearchResult_spokenLanguages> getMovieSearchResultspokenLanguages() {
        return movieSearchResultspokenLanguages;
    }

    public void setMovieSearchResultspokenLanguages(List<MovieSearchResult_spokenLanguages> movieSearchResultspokenLanguages) {
        this.movieSearchResultspokenLanguages = movieSearchResultspokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public double getVote_count() {
        return vote_count;
    }

    public void setVote_count(double vote_count) {
        this.vote_count = vote_count;
    }
}
