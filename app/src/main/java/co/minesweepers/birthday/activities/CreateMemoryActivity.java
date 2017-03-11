package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.CreateMemoryAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class CreateMemoryActivity extends AppCompatActivity implements CreateMemoryAdapter.Listener {

    private Person mPerson;
    private Uri videoPath;
    private RecyclerView mRecyclerView;
    private CreateMemoryAdapter mAdapter;
    private static final int PICK_VIDEO_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        mRecyclerView = (RecyclerView) findViewById(R.id.create_memory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CreateMemoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        String personId = getIntent().getStringExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID);
        mPerson = Memory.getInstance().getPerson(personId);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoPath = data.getData();
        }
    }
    
    @Override
    public void addQuestion() {
        Intent addQuestionIntent = new Intent(this, AddQuestionActivity.class);
        addQuestionIntent.putExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID, mPerson.getId());
        startActivity(addQuestionIntent);
    }

    @Override
    public void addVideo() {
        showFileChooser();
    }

    @Override
    public void addAudio() {

    }
}
