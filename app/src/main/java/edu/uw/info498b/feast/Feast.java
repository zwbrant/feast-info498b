package edu.uw.info498b.feast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by astro.domine on 5/21/2016.
 * Data model for Feasts
 */
public class Feast {
    String name;
    Date date;
    Date dateCreated;
    Date deadline;
    ArrayList<String> categories;
    boolean completed;
    ArrayList<Contact> people;

    public Feast(String name) {
        this.name = name;
    }

    class Contact {
        String name;
        int phoneNumber;

    }
}
