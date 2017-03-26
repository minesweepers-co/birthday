package co.minesweepers.birthday.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;

import co.minesweepers.birthday.Constants;
import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.CreateMemoryAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;
import co.minesweepers.birthday.model.Question;

public class CreateMemoryActivity extends AppCompatActivity implements CreateMemoryAdapter.Listener {

    private Person mPerson;
    private RecyclerView mRecyclerView;
    private CreateMemoryAdapter mAdapter;
    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int CAPTURE_VIDEO_REQUEST = 2;
    private static final int PICK_AUDIO_REQUEST = 3;
    private static final int VIDEO_QUALITY_HIGH = 1;
    private static final String FILE_PREFIX = "test_fd_leak";
    private static final String FILE_VIDEO_EXTENSION = ".mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memory);

        String personId = getIntent().getStringExtra(Constants.INTENT_EXTRA_KEY_PERSON_ID);
        mPerson = Memory.getInstance().getPerson(personId);

        mRecyclerView = (RecyclerView) findViewById(R.id.create_memory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CreateMemoryAdapter(mPerson, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        Question question = mAdapter.getCurrentQuestion();
        if (question.isValid()) {
            mPerson.pushQuestion(question);
        }

        super.onBackPressed();
    }

    private void showVideoChoicesAlert() {
        // TODO add style to dialog suitable for theme
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.video_upload_src_question);

        builder.setPositiveButton(R.string.video_upload_record_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakeVideoIntent();
                    }
                });

        builder.setNegativeButton(R.string.video_upload_from_gallery_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showVideoFileChooser();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showVideoFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.video_chooser_intent_title)), PICK_VIDEO_REQUEST);
    }

    private void dispatchTakeVideoIntent() {
        File file = new File(getExternalFilesDir(null), FILE_PREFIX + String.valueOf(System.currentTimeMillis()) + FILE_VIDEO_EXTENSION);

        Intent captureVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        captureVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        captureVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, VIDEO_QUALITY_HIGH);
        if (captureVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(captureVideoIntent, CAPTURE_VIDEO_REQUEST);
        }
    }

    private void showAudioFileChooser() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.audio_chooser_intent_title)), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_VIDEO_REQUEST:
                case CAPTURE_VIDEO_REQUEST:
                    mPerson.addVideo(data.getData());
                    break;
                case PICK_AUDIO_REQUEST:
                    mPerson.addAudio(data.getData());
                    break;
            }
        }
    }

    @Override
    public void addQuestion(Question question) {
        mPerson.pushQuestion(question);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addVideo() {
        showVideoChoicesAlert();
    }

    @Override
    public void addAudio() {
        showAudioFileChooser();
    }
}
