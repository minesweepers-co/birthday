package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.AddPeopleAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class AddPeopleActivity extends AppCompatActivity implements AddPeopleAdapter.Listener {

    private RecyclerView mRecyclerView;
    private Button mButtonNext;
    private AddPeopleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        mRecyclerView = (RecyclerView) findViewById(R.id.add_person_recycler_view);
        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createCustomCredentialsIntent = new Intent(AddPeopleActivity.this, CreateCustomCredentialsActivity.class);
                startActivity(createCustomCredentialsIntent);
            }
        });

        mAdapter = new AddPeopleAdapter(Memory.getInstance().getAllPeople(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}
