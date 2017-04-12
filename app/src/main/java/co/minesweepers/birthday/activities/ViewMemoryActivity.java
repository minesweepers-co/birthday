package co.minesweepers.birthday.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.ViewMemoryAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class ViewMemoryActivity extends AppCompatActivity implements ViewMemoryAdapter.Listener {

    private static final String TAG = "ViewMemoryActivity";

    private static final String DUMMY_JSON = "{\"id\" : \"memory_id\", \"people\":[ {\"id\":\"person_id1\", \"name\":\"Harsh\", \"videoUri\":\"harsh_video\", \"questions\":[ { \"question\":\"What game can you play all day long(And always lose to Bhai)?\", \"option1\":\"Game Of Life\", \"option2\":\"Teen Patti\", \"option3\":\"Bukharo\", \"correctOption\":\"3\", \"audioUri\":\"harsh_q3_audio\" }, { \"question\":\"When and where were you most surprised?\", \"option1\":\"At home, around Diwali 2015 post midnight\", \"option2\":\"Outside FC around christmas 2014\", \"option3\":\"At home on your birthday\", \"correctOption\":\"1\", \"audioUri\":\"harsh_q4_audio\" } ] }, {\"id\":\"person_id2\", \"name\":\"Ajinkya\", \"videoUri\":\"harsh_video\", \"questions\":[ { \"question\":\"How old do you think you really are?\", \"option1\":\"15\", \"option2\":\"16\", \"option3\":\"17\", \"correctOption\":\"2\", \"audioUri\":\"harsh_q1_audio\" }, { \"question\":\"Most memorable thing that happened in Subrata Roy Sahara stadium?\", \"option1\":\"Watching the cricket game\", \"option2\":\"Someone drinking free alcohol\", \"option3\":\"Waiting in line in the ccar for more than a hour and listening to songs\", \"correctOption\":\"2\", \"audioUri\":\"harsh_q2_audio\" } ] } ] }";

    private List<Person> mPeople;
    private RecyclerView mRecyclerView;
    private ViewMemoryAdapter mAdapter;
    private int mCurrentPersonIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        mRecyclerView = (RecyclerView) findViewById(R.id.view_memory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ViewMemoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        populatePeople();
        if (mPeople != null && mPeople.size() > 0) {
            mCurrentPersonIndex = 0;
            Person person = mPeople.get(mCurrentPersonIndex);
            mAdapter.setPerson(person);
        } else {
            Log.e(TAG, "No people retrieved from server");
        }
    }

    private void populatePeople() {
        try {
            Memory memory = Memory.fromJson(DUMMY_JSON);
            mPeople = memory.getAllPeople();
            Log.d(TAG, "Memory " + memory.getId() + " successfully created from JSON");
        } catch (JSONException e) {
            Log.e(TAG, "JSON ERROR: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onNextPersonClicked() {
        if (mCurrentPersonIndex == mPeople.size() - 1) {
            return;
        }

        mCurrentPersonIndex++;
        mAdapter.setPerson(mPeople.get(mCurrentPersonIndex));
    }

    @Override
    public void onPreviousPersonClicked() {
        if (mCurrentPersonIndex == 0) {
            return;
        }

        mCurrentPersonIndex--;
        mAdapter.setPerson(mPeople.get(mCurrentPersonIndex));
    }
}
