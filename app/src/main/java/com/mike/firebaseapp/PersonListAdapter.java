package com.mike.firebaseapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.mike.firebaseapp.dummy.PersonContent;

public class PersonListAdapter extends FirebaseListAdapter<PersonContent.Person> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    private Context mContext;

    public PersonListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, PersonContent.Person.class, layout, activity);
        this.mUsername = mUsername;
        mContext = activity;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param person An instance representing the current state of a person object
     */
    @Override
    protected void populateView(View view, PersonContent.Person person) {
        // Map a Chat object to an entry in our listview
        TextView textName = (TextView) view.findViewById(R.id.name);
        textName.setText(mContext.getString(R.string.name, person.firstName, person.lastName));
    }
}
