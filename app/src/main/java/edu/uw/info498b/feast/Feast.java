package edu.uw.info498b.feast;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by astro.domine on 5/21/2016.
 * Data model for Feasts that the main screen and detail screen will use
 */
public class Feast {
    String name;
    String date;
    String time;
    Date dateCreated;
    int color;

    boolean completed;
    HashMap<String, Integer> categories;
    HashMap<String, String> phonenumbers;
    ArrayList<Integer> colorArrayList;

    public Feast(String name) {
        this(name, null, null, null);
    }

    public Feast(String name, String date, String time, Date dateCreated) {
        this.name = name;
        this.date = date;
        this.time = time;
        completed = false;
        this.dateCreated = dateCreated;
        categories = new HashMap<String, Integer>();
        phonenumbers = new HashMap<String, String>();
        colorArrayList = new ArrayList<>();
        color = pickColor();
    }

    public Feast(String name, String date, String time, Date dateCreated, HashMap<String, Integer>
            categories, HashMap<String, String> phonenumbers) {
        this.name = name;
        this.date = date;
        this.time = time;
        completed = false;

        this.dateCreated = dateCreated;
        this.categories = categories;
        this.phonenumbers = phonenumbers;
        colorArrayList = new ArrayList<>();
        color = pickColor();
    }

//    public void addPhonenumber(String number){
//        phonenumbers.put(number);
//    }

    public HashMap<String, String> getPhonenumbers(){
        return  phonenumbers;
    }

    public HashMap<String, Integer> getCategories(){
        return  categories;
    }
    public void addCategory(String category){
        categories.put(category, 0);
    }

    public void vote(String category) {
        category = category.toLowerCase();
        if (categories.containsKey(category)) {
            categories.put(category, categories.get(category) + 1);
        } else {
            categories.put(category, 1);
        }
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }
    public int getColor() {return color;}

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }
    public Date getDateCreated(){
        return dateCreated;
    }


    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public boolean getCompleted() {
        return completed;
    }

    @Override
    public String toString(){
        return name;
    }

    private int pickColor() {
        colorArrayList.add(Color.argb(255, 67, 145, 171));
        colorArrayList.add(Color.argb(255, 255, 218, 185));
        colorArrayList.add(Color.argb(255, 240, 255, 240));
        colorArrayList.add(Color.argb(255, 240, 255, 25));
        colorArrayList.add(Color.argb(255, 210, 105, 30));
        colorArrayList.add(Color.argb(255, 245, 255, 250));
        colorArrayList.add(Color.argb(255, 119, 136, 153));

        Random r = new Random();
        int picker = r.nextInt(colorArrayList.size() - 1);

        Log.v("TAG", "" + colorArrayList.get(picker));

        return colorArrayList.get(picker);

    }

}
