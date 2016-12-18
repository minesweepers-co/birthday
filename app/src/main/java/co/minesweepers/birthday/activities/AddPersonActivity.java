package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.minesweepers.birthday.R;

public class AddPersonActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonAddQuestion;
    private Button mButtonAddVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        mButtonAddQuestion = (Button) findViewById(R.id.add_question);
        mButtonAddQuestion.setOnClickListener(this);
        mButtonAddVideo = (Button) findViewById(R.id.add_video);
        mButtonAddVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_question:
                Intent addQuestionIntent = new Intent(this, AddQuestionActivity.class);
                startActivity(addQuestionIntent);
                break;
            case R.id.add_video:
                break;
        }
    }
}
