package com.mike.firebaseapp.pojo;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PersonContent {
    private static final String TAG = PersonContent.class.getSimpleName();

    private static PersonContent sInstance;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Person> persons = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Person> listPersons = new HashMap<>();

    private static Listener mListener;

    public interface Listener {
        void onPersonUpdated(int position, Person person);
        void onPeopleUpdated(List<Person> people);
    }

    private static Firebase mFirebaseRef;

    private PersonContent() {
        mFirebaseRef = new Firebase(com.mike.firebaseapp.Constants.FIREBASE_URL).child(com.mike.firebaseapp.Constants.FIREBASE_URL_PEOPLE);
        mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "dataSnapshot: " + dataSnapshot);

                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    HashMap map = (HashMap) snapShot.getValue();
                    Person person = new Person((String) map.get("id"),
                            (String) map.get("firstName"),
                            (String) map.get("lastName"),
                            (String) map.get("dateOfBirth"),
                            (String) map.get("zipCode"));
                    persons.add(person);
                    listPersons.put(person.id, person);
                }

                if(mListener != null){
                    mListener.onPeopleUpdated(persons);
                }

                Log.i(TAG, "persons: " + persons);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static PersonContent getInstance() {
        if (sInstance == null) {
            sInstance = new PersonContent();
        }
        return sInstance;
    }

    public static void setListener(Listener listener) {
        mListener = listener;
    }

    public static void setPerson(Person person) {
        Log.i(TAG, "setPerson: " + persons);
        int index = persons.indexOf(person);
        if (index >= 0) {
            persons.set(index, person);
        } else {
            persons.add(0, person);
        }
        Log.i(TAG, "after: " + persons);
        if (person.id == null) {
            person.id = mFirebaseRef.push().getKey();
        }
        mFirebaseRef.child(person.id).setValue(person);
        listPersons.put(person.id, person);

        if (mListener != null) {
            mListener.onPersonUpdated(index, person);
        }
    }

    public static void removePerson(Person person) {
        mFirebaseRef.child(person.id).removeValue();
        persons.remove(person);
        listPersons.remove(person.id);
    }

    /**
     * A dummy item representing a piece of lastName.
     */
    public static class Person {
        public String id;
        public String firstName;
        public String lastName;
        public String dateOfBirth;
        public String zipCode;

        public Person() {
        }

        public Person(String id, String firstName, String lastName, String dateOfBirth, String zipCode) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.zipCode = zipCode;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (getClass() != object.getClass()) {
                return false;
            }
            final Person personObject = (Person) object;

            return id != null && id.equals(personObject.id);
        }

        @Override
        public String toString() {
            return firstName;
        }
    }
}
