package co.minesweepers.birthday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewCreate;
    private TextView mTextViewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCreate = (TextView) findViewById(R.id.create);
        mTextViewCreate.setOnClickListener(this);
        mTextViewView = (TextView) findViewById(R.id.view);
        mTextViewView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.create:
                Intent createMemoryIntent = new Intent(this, CreateMemoryActivity.class);
                startActivity(createMemoryIntent);
                break;
            case R.id.view:
                Intent viewMemoryIntent = new Intent(this, ViewMemoryActivity.class);
                startActivity(viewMemoryIntent);
                break;
        }
    }
}
