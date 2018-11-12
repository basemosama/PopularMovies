package com.basemosama.popularmovies.Objects;

public class Trailer {

   private String name;
   private String type;
    private String key;


    public Trailer(String name, String type, String key) {
        this.name = name;
        this.type = type;
        this.key = key;

    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }


}
