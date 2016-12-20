package co.minesweepers.birthday.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Memory;

public class CreateCustomCredentialsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonDoMagic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_credentials);
        mButtonDoMagic = (Button) findViewById(R.id.button_do_magic);
        mButtonDoMagic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_do_magic:
                Memory.getInstance().upload();
                break;
        }
    }
}
