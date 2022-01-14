package com.bimandivlabs.fbla_project;

public class Attraction {
    String SubjectName;
    Boolean Bathrooms;
    Boolean Food;
    Boolean Accessible;
    Integer ActivityType;
    String Image;
    Double Lat;
    Double Long;
    public Attraction(String subjectName, Boolean bathrooms, Boolean food, Boolean accessible, Integer activityType, Double lat, Double longitude, String image) {
        this.SubjectName = subjectName;
        this.Bathrooms = bathrooms;
        this.Food = food;
        this.Accessible = accessible;
        this.ActivityType = activityType;
        this.Image = image;
        this.Lat = lat;
        this.Long = longitude;
    }

    public Boolean getBathrooms() {
        return Bathrooms;
    }
    public Boolean getFood() {
        return Food;
    }
    public Boolean getAccessible() {
        return Accessible;
    }
    public Integer getActivityType() {return ActivityType;}
    public String getName() {
        return SubjectName;
    }
    public String getImage() {
        return Image;
    }
    public Double getLat() {return Lat;}
    public Double getLong() {return Long;}
}
