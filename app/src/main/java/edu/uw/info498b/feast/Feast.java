package edu.uw.info498b.feast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by astro.domine on 5/21/2016.
 * Data model for Feasts that the main screen and detail screen will use
 */
public class Feast {
    String name;
    Date date;        //I used the Date datatype for all the dates. I think it will be the
    Date dateCreated; //most robust solution, but open to suggestions.
    Date deadline;
    ArrayList<String> categories;
    boolean completed;
    ArrayList<Contact> people;

    //TODO Add a real constructor with more parameters. This is just starter code.
    public Feast(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    //Created this class to model the people that are a part of a Feast. Android might have
    //a standard solution to this, but I couldn't find one.
    class Contact {
        String name;
        int phoneNumber;
        //Need to store other things, like the contact image?
    }
}
