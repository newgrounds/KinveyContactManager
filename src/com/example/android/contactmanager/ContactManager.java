/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.contactmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public final class ContactManager extends Activity
{
    public static final String TAG = "ContactManager";
    public static final String CLIENT = "client";
    public static final String COLLECTION = "contactsCollection";

    private Button mAddAccountButton;
    private ListView mContactList;
    
    private static Client mKinveyClient;
    public static Client getClient(Context context) {
    	if (mKinveyClient != null)
    		return mKinveyClient;
    	else
    		return new Client.Builder(context.getApplicationContext()).build();
    }

    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);
        
        // Kinvey Client
        mKinveyClient = getClient(this);
        
        // ping kinvey with client
    	kinveyPing(mKinveyClient);
        
        // login to kinvey with client
        kinveyLogin(mKinveyClient);

        // Obtain handles to UI objects
        mAddAccountButton = (Button) findViewById(R.id.addContactButton);
        mContactList = (ListView) findViewById(R.id.contactList);


        // Register handler for UI elements
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchContactAdder();
            }
        });

        // Populate the contact list
        populateContactList();
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
    	// get contacts from Kinvey
    	AsyncAppData<ContactEntity> mycontacts = mKinveyClient.appData(COLLECTION, ContactEntity.class);
    	mycontacts.get(new KinveyListCallback<ContactEntity>()     {
			@Override
			public void onSuccess(ContactEntity[] result) { 
				Log.v(TAG, "received "+ result.length + " contacts");
				
				//ListAdapter adapter = new ListAdapter(this, R.layout.contact_entry);
		        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
		        //        fields, new int[] {R.id.contactEntryText});
				ArrayAdapter<ContactEntity> adapter = new ArrayAdapter<ContactEntity>(mKinveyClient.getContext(), android.R.layout.simple_list_item_1, result);
		        mContactList.setAdapter(adapter);
			}
			@Override
			public void onFailure(Throwable error)  { 
				Log.e(TAG, "failed to fetch all", error);
			}
    	});
    }

    /***
     * Test Kinvey client credentials
     */
    public static void kinveyPing(Client mKinveyClient) {
        mKinveyClient.ping(new KinveyPingCallback() {
        	@Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Kinvey Ping Failed", t);
            }
        	@Override
            public void onSuccess(Boolean b) {
                Log.d(TAG, "Kinvey Ping Success");
            }
        });
    }
    
    /***
     * Login to kinvey
     */
    public static void kinveyLogin(Client mKinveyClient) {
    	if (!mKinveyClient.user().isUserLoggedIn()) {
	        mKinveyClient.user().login(new KinveyUserCallback() {
	            @Override
	            public void onFailure(Throwable error) {
	                Log.e(TAG, "Login Failure", error);
	            }
	            @Override
	            public void onSuccess(User result) {
	                Log.i(TAG,"Logged in a new implicit user with id: " + result.getId());
	            }
	        });
    	} else {
    		Log.d(TAG, "user is already logged in");
    	}
    }

    /**
     * Launches the ContactAdder activity to add a new contact to the selected account.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(this, ContactAdder.class);
        startActivity(i);
    }
}
