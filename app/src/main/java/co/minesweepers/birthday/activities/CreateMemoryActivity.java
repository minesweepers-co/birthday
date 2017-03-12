package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.CreateMemoryAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;
import co.minesweepers.birthday.model.Question;

public class CreateMemoryActivity extends AppCompatActivity implements CreateMemoryAdapter.Listener {

    private Person mPerson;
    private RecyclerView mRecyclerView;
    private CreateMemoryAdapter mAdapter;
    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_AUDIO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        String personId = getIntent().getStringExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID);
        mPerson = Memory.getInstance().getPerson(personId);

        mRecyclerView = (RecyclerView) findViewById(R.id.create_memory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CreateMemoryAdapter(mPerson, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void showVideoFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    private void showAudioFileChooser() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_VIDEO_REQUEST:
                    mPerson.addVideo(data.getData());
                    break;
                case PICK_AUDIO_REQUEST:
                    mPerson.addAudio(data.getData());
                    break;
            }
        }
    }

    @Override
    public void addQuestion(Question question) {
        mPerson.pushQuestion(question);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addVideo() {
        showVideoFileChooser();
    }

    @Override
    public void addAudio() {
        showAudioFileChooser();
    }

    @Override
    public void save(List<Question> questions) {

    }
}
