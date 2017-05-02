package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.services.SharedPreferenceService;

import static co.minesweepers.birthday.Utils.logEvent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mTextViewCreate = (ImageView) findViewById(R.id.button_create);
        mTextViewCreate.setOnClickListener(this);
        ImageView mTextViewView = (ImageView) findViewById(R.id.button_view);
        mTextViewView.setOnClickListener(this);

        logEvent(FirebaseAnalytics.Event.APP_OPEN, Constants.EVENT_ID_APP_LAUNCH);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_create:
                logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Constants.EVENT_ID_CREATE_MEMORY);
                restore(true);
                Intent getRecipientActivity = new Intent(this, GetRecipientActivity.class);
                startActivity(getRecipientActivity);
                break;
            case R.id.button_view:
                logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, Constants.EVENT_ID_VIEW_MEMORY);
                restore(false);
                Intent welcomePersonIntent = new Intent(this, PersonWelcomeActivity.class);
                startActivity(welcomePersonIntent);
                break;
        }
    }

    private void restore(boolean isCreateMode) {
        try {
            SharedPreferenceService.restore(getApplicationContext(), isCreateMode);
        } catch (JSONException e) {
            Log.e(TAG, "Memory stored on device is corrupted");
        }
    }
}
