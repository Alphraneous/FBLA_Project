package com.bimandivlabs.fbla_project;

public class Attraction implements Comparable<Attraction> {
    String Name;
    String Image;
    String Distance;
    String Website;
    public Attraction(String subjectName, String image, String distance, String website) {
        this.Name = subjectName;
        this.Image = image;
        this.Distance = distance;
        this.Website = website;
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
