package com.mike.firebaseapp.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample lastName for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PersonContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Person> ITEMS = new ArrayList<Person>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Person> ITEM_MAP = new HashMap<String, Person>();

    private static final int MAX_COUNT = 25;

    private static void addItem(Person item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.firstName, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore dateOfBirth information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of lastName.
     */
    public static class Person {
        public String firstName;
        public String lastName;
        public String dateOfBirth;
        public String zipCode;

        public Person(){}

        public Person(String firstName, String lastName, String dateOfBirth, String zipCode) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.zipCode = zipCode;
        }

        @Override
        public String toString() {
            return lastName;
        }
    }
}
