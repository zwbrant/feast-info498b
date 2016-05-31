package edu.uw.info498b.feast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    HashMap<String, Integer> categories;
    ArrayList<String> phonenumbers;

    public Feast(String name) {
        new Feast(name, null, null, null);
    }

    public Feast(String name, String date, String time, Date dateCreated) {
        this.name = name;
        this.date = date;
        this.time = time;
        completed = false;
        this.dateCreated = dateCreated;
        categories = new HashMap<String, Integer>();
        phonenumbers = new ArrayList<String>();
    }

    public Feast(String name, String date, String time, Date dateCreated, HashMap<String, Integer>
                                categories, ArrayList<String> phonenumbers) {
        this.name = name;
        this.date = date;
        this.time = time;
        completed = false;

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

    public HashMap<String, Integer> getCategories(){
        return  categories;
    }

    public void addCategory(String category){
        categories.put(category, 0);
    }

    public void  vote(String category) {
        if (categories.containsKey(category)) {
            categories.put(category, categories.get(category) + 1);
        } else {
            categories.put(category, 1);

        }
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
