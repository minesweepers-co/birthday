package co.minesweepers.birthday.activities;

import android.content.Intent;
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
            case R.id.button_done:
                boolean personAdded = addPersonToMemory();
                if (personAdded) {
                    finish();
                }
                break;
        }
    }

    private boolean addPersonToMemory() {
        // TODO: validate name and at least one question or video present
        mPerson.setName(mEditTextName.getText().toString());
        Memory.getInstance().addPerson(mPerson);
        return true;
    }
}
