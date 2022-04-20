package com.guzic.myapplication.model;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Date;

/**
 * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
 */
public class ClockModel {

    public static ArrayList<ClockModel> clockArrayList = new ArrayList<>();
    public static String CLOCK_EDIT = "clockEdit";
    private int id;
    private String title;
    private String category;
    private Date createdAt;
    public Bitmap bitmap;
    private boolean deleted;

    public ClockModel(int id, String title, String category, Date createdAt, Bitmap bitmap, boolean deleted) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.createdAt = createdAt;
        this.bitmap = bitmap;
        this.deleted = deleted;

    }

    public ClockModel(int id, String title, String category, Date createdAt, Bitmap bitmap) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.createdAt = createdAt;
        this.bitmap = bitmap;
        this.deleted = false;

    }

    public static ClockModel getClock(int getClock) {

        for(ClockModel clock : clockArrayList) {

            if(clock.getId() == getClock) {
                return clock;
            }

        }

        return null;

    }

    public static ArrayList<ClockModel> remainingClocks() {

        ArrayList<ClockModel> remaining = new ArrayList<>();

        for(ClockModel clock : clockArrayList) {

            if(clock.getDeleted() == false) {
                remaining.add(clock);
            }

        }

        return remaining;

    }

    /**
     * Getters & setters
     */

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getTitle() {

        return title;

    }

    public void setTitle(String title) {

        this.title = title;

    }

    public String getCategory() {

        return category;

    }

    public void setCategory(String category) {

        this.category = category;

    }

    public boolean getDeleted() {

        return deleted;

    }

    public void setDeleted(boolean deleted) {

        this.deleted = deleted;

    }

    public Date getCreatedAt() {

        return createdAt;

    }

    public void setCreatedAt(Date createdAt) {

        this.createdAt = createdAt;

    }

    public Bitmap getBitmap() {

        return bitmap;

    }

    public void setBitmap(Bitmap bitmap) {

        this.bitmap = bitmap;

    }

}