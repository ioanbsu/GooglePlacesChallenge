package com.artigile.android.placesapi.api.service;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 3:31 PM
 */
public enum RankByType {

    PROMINENCE("prominence"),
    DISTANCE("distance");

    private String value;

    RankByType(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }
}
