package com.example.second;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Holiday {
    @JsonProperty("date")
    private String date;

    public String getDate() {
        return date;
    }

    @JsonProperty("localName")
    private String localName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("fixed")
    private boolean fixed;

    @JsonProperty("global")
    private boolean global;

    @JsonProperty("counties")
    private Object counties;

    @JsonProperty("launchYear")
    private Integer launchYear;

    @JsonProperty("types")
    private List<String> types;
}
