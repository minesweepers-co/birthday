package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.services.FirebaseDBService;

public class PersonWelcomeActivity extends AppCompatActivity {

    private TextView mTextViewSurpriseMessage;
    private Button mButtonReady;
    private ImageView mImageViewPerson;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_welcome);
        handleIntent();

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if(Utils.isEmpty(action)) {
            return;
        }

        if (action.equals(Intent.ACTION_VIEW)) {
            Uri data = intent.getData();
            final String memoryId = data.getQueryParameter(Constants.MEMORY_ID_QUERY_PARAM);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.EVENT_ID_LAUNCH_APP_VIA_DEEPLINK);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
            //TODO: Show spinner till memory is fetched
            FirebaseDBService.getMemoryForId(memoryId, new FirebaseDBService.MemoryDownloadListener() {
                @Override
                public void onSuccess(Memory memory) {
                    Toast.makeText(PersonWelcomeActivity.this, "Memory " + memory.getId() + " fetched", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(PersonWelcomeActivity.this, "Fetching memory failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
