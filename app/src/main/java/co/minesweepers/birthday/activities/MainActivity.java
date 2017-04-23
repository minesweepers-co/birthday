package co.minesweepers.birthday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mTextViewCreate;
    private ImageView mTextViewView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mTextViewCreate = (ImageView) findViewById(R.id.button_create);
        mTextViewCreate.setOnClickListener(this);
        mTextViewView = (ImageView) findViewById(R.id.button_view);
        mTextViewView.setOnClickListener(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.EVENT_ID_APP_LAUNCH);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_create:
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.EVENT_ID_CREATE_MEMORY);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent createMemoryIntent = new Intent(this, AddPeopleActivity.class);
                startActivity(createMemoryIntent);
                break;
            case R.id.button_view:
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.EVENT_ID_VIEW_MEMORY);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                Intent welcomePersonIntent = new Intent(this, PersonWelcomeActivity.class);
                startActivity(welcomePersonIntent);
                break;
        }
    }
}
