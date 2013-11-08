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
    @Key("name")
    private String name;
    @Key("email")
    private String email;
    @Key("phone")
    private String phoneNumber;
    // username/email that links to associated user account
    @Key("accountName")
    private String accountName;
    
    // public empty constructor
    public ContactEntity(){}
}