package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class AddPersonActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonAddQuestion;
    private Button mButtonAddVideo;
    private Button mButtonDone;
    private EditText mEditTextName;
    private Person mPerson;
    private Uri videoPath;
    private static final int PICK_VIDEO_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        mButtonAddQuestion = (Button) findViewById(R.id.button_add_question);
        mButtonAddQuestion.setOnClickListener(this);
        mButtonAddVideo = (Button) findViewById(R.id.button_add_video);
        mButtonAddVideo.setOnClickListener(this);
        mButtonDone = (Button) findViewById(R.id.button_done);
        mButtonDone.setOnClickListener(this);
        mEditTextName = (EditText) findViewById(R.id.edit_text_name);
        mPerson = new Person();
        Memory.getInstance().addPerson(mPerson);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_add_question:
                Intent addQuestionIntent = new Intent(this, AddQuestionActivity.class);
                addQuestionIntent.putExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID, mPerson.getId());
                startActivity(addQuestionIntent);
                break;
            case R.id.button_add_video:
                showFileChooser();
                break;
            case R.id.button_done:
                boolean personAdded = addPersonToMemory();
                if (personAdded) {
                    finish();
                }
                break;
        }
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

    private boolean addPersonToMemory() {
        // TODO: validate name and at least one question or video present
        mPerson.setName(mEditTextName.getText().toString());
        mPerson.addVideo(videoPath);
        Memory.getInstance().addPerson(mPerson);
        return true;
    }
}
