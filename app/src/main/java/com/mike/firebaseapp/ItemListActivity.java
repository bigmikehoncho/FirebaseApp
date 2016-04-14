package com.mike.firebaseapp;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;


import com.mike.firebaseapp.pojo.PersonContent;

import java.util.List;

/**
 * An activity representing a list of Persons. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of persons, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * Person. On tablets, the activity presents the list of persons and
 * Person side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements PersonContent.Listener {
    private static final String TAG = ItemListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private PersonRecyclerViewAdapter mAdapter;

    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setTitle(getTitle());

        PersonContent.setListener(this);
        PersonContent.getInstance();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        assert mFab != null;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new PersonRecyclerViewAdapter(PersonContent.persons);
        recyclerView.setAdapter(mAdapter);
    }

    /*BEGIN PersonContent callbacks*/
    @Override
    public void onPersonUpdated(int position, PersonContent.Person person) {
        mAdapter.updateList(position);
        Snackbar.make(mFab, getString(R.string.person_updated, person.firstName), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPersonAdded(PersonContent.Person person) {
        mAdapter.notifyItemInserted(0);
        Snackbar.make(mFab, getString(R.string.person_added, person.firstName), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPersonRemoved(int position, PersonContent.Person person) {
        mAdapter.notifyItemRemoved(position);
        Snackbar.make(mFab, getString(R.string.person_removed, person.firstName), Snackbar.LENGTH_LONG).show();
    }
    /*END PersonContent callbacks*/

    public class PersonRecyclerViewAdapter
            extends RecyclerView.Adapter<PersonRecyclerViewAdapter.ViewHolder> {

        private final List<PersonContent.Person> mValues;

        public PersonRecyclerViewAdapter(List<PersonContent.Person> items) {
            mValues = items;
        }

        public void updateList(int position) {
            notifyItemChanged(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mPerson = mValues.get(position);
            holder.mName.setText(getString(R.string.name, holder.mPerson.firstName, holder.mPerson.lastName));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_PERSON_ID, holder.mPerson.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(ItemListActivity.this, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_PERSON_ID, holder.mPerson.id);

                        startActivity(intent);
                    }
                }
            });
            holder.mBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonContent.removePerson(holder.mPerson);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mName;
            public final ImageView mBtnRemove;
            public PersonContent.Person mPerson;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mName = (TextView) view.findViewById(R.id.name);
                mBtnRemove = (ImageView) view.findViewById(R.id.button_remove);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }
    }
}
