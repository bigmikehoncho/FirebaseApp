package com.mike.firebaseapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mike.firebaseapp.dummy.PersonContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PERSON_ID = "person_id";

    public static final String EXTRA_PERSON_ID = "person_id";

    /**
     * The dummy lastName this fragment is presenting.
     */
    private PersonContent.Person mPerson;

    private FloatingActionButton mFab;
    private EditText mEditFirstName;
    private EditText mEditLastName;
    private EditText mEditDOB;
    private EditText mEditZipCode;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PERSON_ID)) {
            // Load the dummy lastName specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load lastName from a lastName provider.
            mPerson = PersonContent.listPersons.get(getArguments().getLong(ARG_PERSON_ID));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                if(mPerson == null){
                    mPerson = new PersonContent.Person();
                    appBarLayout.setTitle(getString(R.string.new_person));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupVariables(view);
        setUIFields();

    }

    private void setupVariables(View view){
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mEditFirstName = (EditText) view.findViewById(R.id.firstName);
        mEditLastName = (EditText) view.findViewById(R.id.lastName);
        mEditDOB = (EditText) view.findViewById(R.id.date_of_birth);
        mEditZipCode = (EditText) view.findViewById(R.id.zip_code);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void setUIFields(){
        mEditFirstName.setText(mPerson.firstName);
        mEditLastName.setText(mPerson.lastName);
        mEditDOB.setText(mPerson.dateOfBirth);
        mEditZipCode.setText(mPerson.zipCode);
    }

    private void save(){
        mPerson.firstName = mEditFirstName.getText().toString();
        if(mPerson.firstName.isEmpty()){
            Snackbar.make(mEditFirstName, "You need to supply at least a first name", Snackbar.LENGTH_SHORT).show();
            return;
        }
        mPerson.lastName = mEditLastName.getText().toString();
        mPerson.dateOfBirth = mEditDOB.getText().toString();
        mPerson.zipCode = mEditZipCode.getText().toString();

        PersonContent.setPerson(mPerson);
        getActivity().onBackPressed();
    }
}
