package edu.uw.info498b.feast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by astro.domine on 5/21/2016.
 * Data model for Feasts that the main screen and detail screen will use
 */
public class Feast {
    String name;
    String date;
    String time;
    Date dateCreated;

    boolean completed;
    ArrayList<String> categories;
    ArrayList<String> phonenumbers;

    public Feast(String name, String date, String time, Date dateCreated) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.dateCreated = dateCreated;
        categories = new ArrayList<String>();
        phonenumbers = new ArrayList<String>();
    }

    public Feast(String name, String date, String time, Date dateCreated,
                 ArrayList<String> categories, ArrayList<String> phonenumbers) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.dateCreated = dateCreated;
        this.categories = categories;
        this.phonenumbers = phonenumbers;
    }

    public void addPhonenumber(String number){
        phonenumbers.add(number);
    }

    public ArrayList<String> getPhonenumbers(){
        return  phonenumbers;
    }

    public ArrayList<String> getCategories(){
        return  categories;
    }

    public void addCategory(String category){
        categories.add(category);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    @Override
    public String toString(){
        return name;
    }

}
