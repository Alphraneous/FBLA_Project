package com.bimandivlabs.fbla_project;

public class Attraction2 implements Comparable<Attraction2> {
    String SubjectName;
    String Image;
    String Distance;
    String Website;
    public Attraction2(String subjectName, String image, String distance, String website) {
        this.SubjectName = subjectName;
        this.Image = image;
        this.Distance = distance;
        this.Website = website;
    }

    public int compareTo(Attraction2 attraction)
    {
        if (Distance.equals(attraction.Distance))
            return 0;
        else if (Integer.parseInt(Distance) > Integer.parseInt(attraction.Distance))
            return 1;
        else
            return -1;
    }
}
