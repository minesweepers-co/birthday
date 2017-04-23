package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.AddPeopleAdapter;
import co.minesweepers.birthday.adapters.helpers.ItemTouchHelperCallback;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class AddPeopleActivity extends AppCompatActivity implements AddPeopleAdapter.Listener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ImageButton mButtonGenerateCode;
    private ImageButton mButtonReorderPeople;
    private AddPeopleAdapter mAdapter;
    private ItemTouchHelper mTouchHelper;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        mRecyclerView = (RecyclerView) findViewById(R.id.add_person_recycler_view);
        mButtonGenerateCode = (ImageButton) findViewById(R.id.button_next);
        mButtonGenerateCode.setOnClickListener(this);

        mAdapter = new AddPeopleAdapter(Memory.getInstance().getAllPeople(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        mTouchHelper = new ItemTouchHelper(callback);

        mButtonReorderPeople = (ImageButton) findViewById(R.id.button_reorder);
        mButtonReorderPeople.setOnClickListener(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, this.getLocalClassName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void createOrEditMemory(Person person) {
        Intent addPersonIntent = new Intent(this, CreateMemoryActivity.class);
        addPersonIntent.putExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID, person.getId());
        startActivity(addPersonIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next:
                Intent createCustomCredentialsIntent = new Intent(AddPeopleActivity.this, CreateCustomCredentialsActivity.class);
                startActivity(createCustomCredentialsIntent);
                break;
            case R.id.button_reorder:
                if (mAdapter.isInEditMode()) {
                    mAdapter.setEditMode(false);
                    mTouchHelper.attachToRecyclerView(null);
                } else {
                    mAdapter.setEditMode(true);
                    mTouchHelper.attachToRecyclerView(mRecyclerView);
                }
                break;
        }
    }
}
