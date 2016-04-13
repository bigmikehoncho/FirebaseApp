package com.mike.firebaseapp.dummy;

import android.util.Log;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample lastName for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PersonContent {
    private static final String TAG = PersonContent.class.getSimpleName();

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Person> persons = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final LongSparseArray<Person> listPersons = new LongSparseArray<>();

    private static long nextId = 0;

    private static final int MAX_COUNT = 25;

    private static Listener mListener;

    public interface Listener{
        void onDetailsUpdated(int position, Person person);
    }

    public static void setListener(Listener listener){
        mListener = listener;
    }

    public static void setPerson(Person person){
        Log.i(TAG, "setPerson: " + persons);
        int index = persons.indexOf(person);
        if(index >= 0){
            persons.set(index, person);
            listPersons.setValueAt(index, person);
        } else {
            person.id = getNextId();
            persons.add(0, person);
            listPersons.put(person.id, person);
        }
        Log.i(TAG, "after: " + persons);

        if(mListener != null){
            mListener.onDetailsUpdated(index, person);
        }
    }

    private static long getNextId() {
        return nextId++;
    }

    /**
     * A dummy item representing a piece of lastName.
     */
    public static class Person {
        public long id;
        public String firstName;
        public String lastName;
        public String dateOfBirth;
        public String zipCode;

        public Person() {
            id = -1;
        }

        public Person(long id, String firstName, String lastName, String dateOfBirth, String zipCode) {
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

            return id == personObject.id;
        }

        @Override
        public String toString() {
            return firstName;
        }
    }
}
