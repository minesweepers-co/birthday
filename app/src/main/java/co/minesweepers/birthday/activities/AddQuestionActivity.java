package co.minesweepers.birthday.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import co.minesweepers.birthday.R;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText mEditTextQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        mEditTextQuestion = (EditText) findViewById(R.id.question);
    }
}
