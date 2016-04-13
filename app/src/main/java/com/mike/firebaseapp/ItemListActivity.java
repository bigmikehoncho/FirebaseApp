package com.mike.firebaseapp;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mike.firebaseapp.dummy.PersonContent;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item dateOfBirth. On tablets, the activity presents the list of items and
 * item dateOfBirth side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements PersonContent.Listener {
    private static final String TAG = ItemListActivity.class.getSimpleName();

    private static final int REQUEST_DETAILS = 100;

    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private PersonListAdapter mPersonListAdapter;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        PersonContent.setListener(this);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_PERSON_ID, (long)-1);
                    startActivity(intent);
                }
            }
        });

        mFirebaseRef = new Firebase(getString(R.string.firebase_url));

        View listView = findViewById(R.id.item_list);
        assert listView != null;
        setupRecyclerView((RecyclerView) listView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Tell our list adapter that we only want 50 messages at a time
        mPersonListAdapter = new PersonListAdapter(mFirebaseRef.limitToLast(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mPersonListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(ItemListActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ItemListActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mPersonListAdapter.cleanup();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new SimpleItemRecyclerViewAdapter(PersonContent.persons);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetailsUpdated(int position, PersonContent.Person person) {
        if (position >= 0) {
            mAdapter.updateList(position, person);
            Snackbar.make(mFab, person.firstName + " updated", Snackbar.LENGTH_LONG).show();
        } else {
            mAdapter.notifyItemInserted(0);
            Snackbar.make(mFab, person.firstName + " added", Snackbar.LENGTH_LONG).show();
        }

    }
    }
}
