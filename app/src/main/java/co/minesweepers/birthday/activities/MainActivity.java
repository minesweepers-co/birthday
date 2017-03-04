package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import co.minesweepers.birthday.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mTextViewCreate;
    private ImageView mTextViewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCreate = (ImageView) findViewById(R.id.button_create);
        mTextViewCreate.setOnClickListener(this);
        mTextViewView = (ImageView) findViewById(R.id.button_view);
        mTextViewView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_create:
                Intent createMemoryIntent = new Intent(this, CreateMemoryActivity.class);
                startActivity(createMemoryIntent);
                break;
            case R.id.button_view:
                Intent viewMemoryIntent = new Intent(this, ViewMemoryActivity.class);
                startActivity(viewMemoryIntent);
                break;
        }
    }
}
