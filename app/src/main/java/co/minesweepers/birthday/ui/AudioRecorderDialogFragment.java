package co.minesweepers.birthday.ui;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.OperationCanceledException;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

import co.minesweepers.birthday.App;
import co.minesweepers.birthday.R;

public class AudioRecorderDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageButton mButton;
    private boolean mIsRecording = false;
    private static final String AUDIO_PATH = "audioPath";
    private MediaRecorder mediaRecorder;
    private static final String FILE_PREFIX = "audioFile";
    private static final String FILE_AUDIO_EXTENSION = ".3gp";
    private Uri mAudioUri;

    private static final String TAG = "RecorderDialogFragment";

    public interface AudioRecorderListener {
        void onComplete(Uri audioUri);

        void onFailure(Exception e);
    }

    public AudioRecorderDialogFragment() {

    }

    public static AudioRecorderDialogFragment newInstance() {
        AudioRecorderDialogFragment frag = new AudioRecorderDialogFragment();
        File file = new File(App.APP_STORAGE_DIRECTORY, FILE_PREFIX + String.valueOf(System.currentTimeMillis()) + FILE_AUDIO_EXTENSION);

        Bundle args = new Bundle();
        args.putParcelable(AUDIO_PATH, Uri.fromFile(file));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.audio_recorder_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAudioUri = getArguments().getParcelable(AUDIO_PATH);

        mButton = (ImageButton) view.findViewById(R.id.audio_recorder_button);
        mButton.setOnClickListener(this);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(mAudioUri.getPath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_recorder_button:
                AudioRecorderListener listener = (AudioRecorderListener) getActivity();
                if (mIsRecording) {
                    // already recording , stop recording and exit
                    mediaRecorder.stop();
                    dismiss();
                    listener.onComplete(mAudioUri);
                } else {
                    // start recording
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "Exception while preparing - " + e.getLocalizedMessage());
                        listener.onFailure(e);
                    }
                    mediaRecorder.start();
                    mButton.setImageResource(R.drawable.stop_recording);
                    mIsRecording = true;
                }
        }
    }
}
