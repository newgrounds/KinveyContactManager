package com.example.android.contactmanager;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * The ContactEntity class represents a single contact
 * 
 * @author adamgressen
 *
 */
public class ContactEntity extends GenericJson {
    @Key("_id")
    private String id;
    @Key
    private String name;
    @Key
    private String email;
    @Key
    private String phoneNumber;
    // username/email that links to associated user account
    @Key
    private String username;
    
    // public empty constructor
    public ContactEntity(){}
}