package co.minesweepers.birthday.adapters;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.annotation.Retention;

import co.minesweepers.birthday.R;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class CreateMemoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int HEADER_IMAGE_POSITION = 0;
    private static final int ADD_QUESTION_IMAGE_POSITION = 1;
    private static final int ADD_AUDIO_IMAGE_POSITION = 2;
    private static final int ADD_VIDEO_IMAGE_POSITION = 3;

    private Listener mListener;

    public CreateMemoryAdapter(Listener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE) {
            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_memory_image_cell, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == HEADER_IMAGE_POSITION) {
            ((ImageViewHolder) holder).bind(R.drawable.question_audio_video, IMAGE_VIEW_TYPE_HEADER);
            return;
        }

        if (position == ADD_QUESTION_IMAGE_POSITION) {
            ((ImageViewHolder) holder).bind(R.drawable.add_question, IMAGE_VIEW_TYPE_ADD_QUESTION);
            return;
        }

        if (position == ADD_AUDIO_IMAGE_POSITION) {
            ((ImageViewHolder) holder).bind(R.drawable.add_audio, IMAGE_VIEW_TYPE_ADD_AUDIO);
            return;
        }

        if (position == ADD_VIDEO_IMAGE_POSITION) {
            ((ImageViewHolder) holder).bind(R.drawable.add_video, IMAGE_VIEW_TYPE_ADD_VIDEO);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        @ImageViewType int mType;

        ImageViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageview_cell);
            mImageView.setOnClickListener(this);
        }

        void bind(int imageResourceId, @ImageViewType int type) {
            mImageView.setImageResource(imageResourceId);
            mType = type;
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                return;
            }

            switch (mType) {
                case IMAGE_VIEW_TYPE_HEADER:
                    break;
                case IMAGE_VIEW_TYPE_ADD_QUESTION:
                    mListener.addQuestion();
                    break;
                case IMAGE_VIEW_TYPE_ADD_AUDIO:
                    mListener.addAudio();
                    break;
                case IMAGE_VIEW_TYPE_ADD_VIDEO:
                    mListener.addVideo();
                    break;
            }
        }
    }

    public interface Listener {
        void addQuestion();
        void addVideo();
        void addAudio();
    }

    @Retention(SOURCE)
    @IntDef({IMAGE_VIEW_TYPE_HEADER, IMAGE_VIEW_TYPE_ADD_QUESTION, IMAGE_VIEW_TYPE_ADD_AUDIO, IMAGE_VIEW_TYPE_ADD_VIDEO})
    @interface ImageViewType {}
    static final int IMAGE_VIEW_TYPE_HEADER = 0;
    static final int IMAGE_VIEW_TYPE_ADD_QUESTION = 1;
    static final int IMAGE_VIEW_TYPE_ADD_AUDIO = 2;
    static final int IMAGE_VIEW_TYPE_ADD_VIDEO = 3;
}
