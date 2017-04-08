package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;

public class PersonWelcomeActivity extends AppCompatActivity {

    private TextView mTextViewSurpriseMessage;
    private Button mButtonReady;
    private ImageView mImageViewPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_welcome);

        mTextViewSurpriseMessage =  (TextView) findViewById(R.id.surprise_message);
        Typeface customFont = Typeface.createFromAsset(getAssets(), Constants.AMATIC_FONT);
        mTextViewSurpriseMessage.setTypeface(customFont);

        // TODO: Set the persons image in image view when you get it from server
        mImageViewPerson = (ImageView) findViewById(R.id.person_image);

        mButtonReady = (Button) findViewById(R.id.button_ready);
        mButtonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMemoryIntent = new Intent(PersonWelcomeActivity.this, ViewMemoryActivity.class);
                startActivity(viewMemoryIntent);
            }
        });
    }
}
