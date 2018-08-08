package com.andyshon.moviedb.data.entity;

/**
 * Created by andyshon on 08.08.18.
 */

public class MovieSearchResult_productionCompanies {
    public MovieSearchResult_productionCompanies() {}

    private int id;
    private String logo_path;
    private String name;
    private String origin_country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }
}
