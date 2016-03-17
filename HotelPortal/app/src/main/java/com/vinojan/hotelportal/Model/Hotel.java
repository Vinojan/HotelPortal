package com.vinojan.hotelportal.Model;

/**
 * Created by vino-pc on 1/27/16.
 */
public class Hotel {
    private String name;
    private String address;
    private String city;
    private double longitude;
    private double latitude;

    /*
    *double distance : only for sorting hotels
     */
    private double distanceFromUser;

    public Hotel(String name,String city,String address,double longitude, double latitude){
        this.name=name;
        this.city=city;
        this.address=address;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public void setCity(String city){
        this.city=city;
    }
    public String getCity(){
        return city;
    }

    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return  address;
    }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }
    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }
    public double getLatitude(){
        return latitude;
    }

    public void setDistanceFromUser(double distance){
        this.distanceFromUser=distance;
    }
    public double getDistanceFromUser(){
        return  distanceFromUser;
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /*For Jenkin Test */
    public void sayHello(){
    	System.out.println("Hello..!");
    }
    
}
