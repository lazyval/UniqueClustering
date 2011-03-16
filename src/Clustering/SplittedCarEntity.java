package Clustering;

import Harvester.CarEntity;

/**
 * User: john
 * Date: 12.03.11
 * Time: 21:25
 */
public class SplittedCarEntity {
    private int price;
    private String[] shortDesc;
    private String[] longDesc;
    private String city;
    private int year;
    private int cluster;

    public SplittedCarEntity(int price, String shortDesc, String longDesc, String city, int year, int cluster) {
        this.price = price;
        this.shortDesc = shortDesc.split(",");
        this.longDesc = longDesc.split(",");
        this.city = city;
        this.year = year;
        this.cluster = cluster;
    }

    public SplittedCarEntity(CarEntity c) {
        this.price = c.getPrice();
        this.shortDesc = c.getShortDesc().split(",");
        this.longDesc = c.getLongDesc().split(",");
        this.city = c.getCity();
        this.year = c.getYear();
        this.cluster = c.getCluster();
    }

    public int getPrice() {
        return price;
    }

    public String[] getShortDesc() {
        return shortDesc;
    }

    public String[] getLongDesc() {
        return longDesc;
    }

    public String getCity() {
        return city;
    }

    public int getYear() {
        return year;
    }

    public int getCluster() {
        return cluster;
    }
    public void setCluster(int id) {
        this.cluster = id;
    }


}
