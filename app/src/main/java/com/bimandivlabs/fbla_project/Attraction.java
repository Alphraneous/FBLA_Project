package com.bimandivlabs.fbla_project;

public class Attraction {
    String SubjectName;
    Boolean Bathrooms;
    String Image;
    Double Lat;
    Double Long;
    public Attraction(String subjectName, Boolean bathrooms, Double lat, Double longitude, String image) {
        this.SubjectName = subjectName;
        this.Bathrooms = bathrooms;
        this.Image = image;
        this.Lat = lat;
        this.Long = longitude;
    }

    public Boolean getBathrooms() {
        return Bathrooms;
    }
    public String getName() {
        return SubjectName;
    }
    public String getImage() {
        return Image;
    }
    public Double getLat() {return Lat;}
    public Double getLong() {return Long;}
}
