package com.bimandivlabs.fbla_project;

import java.io.Serializable;

public class Attraction implements Comparable<Attraction>, Serializable {
    Integer ID;
    String Name;
    String Image;
    String Distance;
    String Website;
    Integer Rating;
    Integer Price;
    public Attraction(Integer id, String subjectName, String image, String distance, String website,Integer rating,Integer price) {
        this.ID = id;
        this.Name = subjectName;
        this.Image = image;
        this.Distance = distance;
        this.Website = website;
        this.Price = price;
        this.Rating = rating;
    }

    public int compareTo(Attraction attraction)
    {
        if (Distance.equals(attraction.Distance))
            return 0;
        else if (Integer.parseInt(Distance) > Integer.parseInt(attraction.Distance))
            return 1;
        else
            return -1;
    }
}
