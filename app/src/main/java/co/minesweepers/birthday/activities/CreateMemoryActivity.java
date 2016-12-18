package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.minesweepers.birthday.R;

public class CreateMemoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonAddPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);
        mButtonAddPerson = (Button) findViewById(R.id.add_person);
        mButtonAddPerson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_person:
                Intent addPersonIntent = new Intent(this, AddPersonActivity.class);
                startActivity(addPersonIntent);
                break;
        }
    }
}
