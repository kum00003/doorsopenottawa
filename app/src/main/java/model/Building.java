package model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sercan on 11/22/16.
 * Created by David on 11/22/16.
 */

public class Building {

    private Integer buildingID;
    private String name;
    private String address;
    private String image;
    private Bitmap bitmap;
    private String description;
    private List<String> openHours=new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(Integer buildingID) {
        this.buildingID = buildingID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address + "Ottawa, Ontario";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<String> getOpenHours() { return openHours; }

    public void setDate( String date) {
        this.openHours.add(date);
        }
}



