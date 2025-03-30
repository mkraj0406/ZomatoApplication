package com.slf.zmt.requestdto;


import java.time.LocalTime;

public class RestaurantRequestDto {

    private String name;

    private String location;

    private String cuisineType;

    private Double rating;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private String phoneNo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    @Override
    public String toString() {
        return "RestaurantRequestDto{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", rating=" + rating +
                ", openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
