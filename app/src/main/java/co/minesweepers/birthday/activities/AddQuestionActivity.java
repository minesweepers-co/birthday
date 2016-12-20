package co.minesweepers.birthday.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;
import co.minesweepers.birthday.model.Question;

public class AddQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextQuestion;
    private RadioGroup mRadioGroup;
    private List<RadioButton> mRadioButtonsList;
    private Uri audioPath;
    private Button mButtonAddOption;
    private Button mButtonAddAudio;
    private Button mButtonDone;
    private String mPersonId;
    private static final int PICK_AUDIO_REQUEST = 234;

    private int optionsAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        mPersonId = getIntent().getStringExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID);
        mEditTextQuestion = (EditText) findViewById(R.id.edit_text_question);
        mButtonAddOption = (Button) findViewById(R.id.button_add_option);
        mButtonAddOption.setOnClickListener(this);
        mButtonDone = (Button) findViewById(R.id.button_done);
        mButtonDone.setOnClickListener(this);
        mButtonAddAudio = (Button) findViewById(R.id.button_add_audio);
        mButtonAddAudio.setOnClickListener(this);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RadioButton radioButtonOption1 = (RadioButton) findViewById(R.id.radio_option1);
        RadioButton radioButtonOption2 = (RadioButton) findViewById(R.id.radio_option2);
        RadioButton radioButtonOption3 = (RadioButton) findViewById(R.id.radio_option3);
        RadioButton radioButtonOption4 = (RadioButton) findViewById(R.id.radio_option4);
        mRadioButtonsList = new ArrayList<>(4);
        mRadioButtonsList.add(radioButtonOption1);
        mRadioButtonsList.add(radioButtonOption2);
        mRadioButtonsList.add(radioButtonOption3);
        mRadioButtonsList.add(radioButtonOption4);
        optionsAdded = 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_add_option:
                if (optionsAdded >= 4) {
                    Toast.makeText(this, "Only 4 options can be added", Toast.LENGTH_SHORT).show();
                    return;
                }
                showAddOptionDialog();
                break;
            case R.id.button_done:
                boolean questionAdded = addQuestionToPerson();
                if (questionAdded) {
                    finish();
                }
                break;
            case R.id.button_add_audio:
                showFileChooser();
                break;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        // keep image for now for easy debugging
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio"), PICK_AUDIO_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            audioPath = data.getData();
        }
    }

    private boolean addQuestionToPerson() {
        //TODO: Validate question and at least 2 options present
        Person person = Memory.getInstance().getPerson(mPersonId);
        Question question = new Question.Builder()
                .setQuestion(mEditTextQuestion.getText().toString())
                .setOption1(mRadioButtonsList.get(0).getText().toString())
                .setOption2(mRadioButtonsList.get(1).getText().toString())
                .setOption3(mRadioButtonsList.get(2).getText().toString())
                .setOption4(mRadioButtonsList.get(3).getText().toString())
                .setCorrectOption(getCorrectOption())
                .setAudio(audioPath)
                .question();

        person.addQuestion(question);
        return true;
    }

    private int getCorrectOption() {
        int radioButtonId = mRadioGroup.getCheckedRadioButtonId();
        switch (radioButtonId) {
            case R.id.radio_option1:
                return 1;
            case R.id.radio_option2:
                return 2;
            case R.id.radio_option3:
                return 3;
            case R.id.radio_option4:
                return 4;
        }

        return 0;
    }

    private void showAddOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_option);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOption(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void addOption(String text) {
        RadioButton radioButton = mRadioButtonsList.get(optionsAdded);
        radioButton.setText(text);
        optionsAdded++;
    }
}
