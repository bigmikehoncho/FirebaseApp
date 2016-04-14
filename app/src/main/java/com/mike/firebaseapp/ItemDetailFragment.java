package com.mike.firebaseapp;

import android.app.DatePickerDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.mike.firebaseapp.pojo.PersonContent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements View.OnClickListener{
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
    private TextView mEditDOB;
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

        if (getArguments() != null && getArguments().containsKey(ARG_PERSON_ID)) {
            mPerson = PersonContent.listPersons.get(getArguments().getString(ARG_PERSON_ID));
        } else {
            mPerson = new PersonContent.Person();
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

    private void setupVariables(View view) {
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mEditFirstName = (EditText) view.findViewById(R.id.firstName);
        mEditLastName = (EditText) view.findViewById(R.id.lastName);
        mEditDOB = (TextView) view.findViewById(R.id.date_of_birth);
        mEditZipCode = (EditText) view.findViewById(R.id.zip_code);

        mFab.setOnClickListener(this);
        mEditDOB.setOnClickListener(this);
    }

    private void setUIFields() {
        mEditFirstName.setText(mPerson.firstName);
        mEditLastName.setText(mPerson.lastName);
        mEditDOB.setText(mPerson.dateOfBirth);
        mEditZipCode.setText(mPerson.zipCode);
    }

    private void save() {
        mPerson.firstName = mEditFirstName.getText().toString();
        if (mPerson.firstName.isEmpty()) {
            Snackbar.make(mEditFirstName, R.string.invalid_person, Snackbar.LENGTH_SHORT).show();
            return;
        }
        mPerson.lastName = mEditLastName.getText().toString();
        mPerson.dateOfBirth = mEditDOB.getText().toString();
        mPerson.zipCode = mEditZipCode.getText().toString();

        PersonContent.setPerson(mPerson);

        getActivity().onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                save();
                break;
            case R.id.date_of_birth:
                Calendar calendar = Calendar.getInstance();
                if(mPerson.dateOfBirth != null) {
                    try {
                        calendar.setTime(Constants.DATE_FORMATTER.parse(mPerson.dateOfBirth));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        mEditDOB.setText(Constants.DATE_FORMATTER.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
        }
    }
}
