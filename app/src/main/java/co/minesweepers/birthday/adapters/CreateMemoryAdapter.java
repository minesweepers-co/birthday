package co.minesweepers.birthday.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Question;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class CreateMemoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_IMAGE_OFFSET = 1;
    private static final int QUESTION_IMAGE_OFFSET = 1;
    private static final int AUDIO_IMAGE_OFFSET = 1;
    private static final int VIDEO_IMAGE_OFFSET = 1;
    private static final int NEW_QUESTION_OFFSET = 1;

    private Listener mListener;
    private List<Question> mQuestions;
    private QuestionsViewHolder mCurrentQuestionHolder;

    public CreateMemoryAdapter(@NonNull Listener listener) {
        mListener = listener;
        mQuestions = new ArrayList<>();
    }

    @Override
    public @ViewType int getItemViewType(int position) {
        if (position == HEADER_IMAGE_OFFSET - 1) {
            return VIEW_TYPE_HEADER;
        }

        if (position == (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET) - 1) {
            return VIEW_TYPE_ADD_QUESTION;
        }

        int noOfQuestions = mQuestions.size();
        if (position <= (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET  + NEW_QUESTION_OFFSET + noOfQuestions) - 1) {
            return VIEW_TYPE_QUESTION;
        }

        if (position == (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET + noOfQuestions + AUDIO_IMAGE_OFFSET) - 1) {
            return VIEW_TYPE_ADD_AUDIO;
        }

        if (position == (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET + noOfQuestions + AUDIO_IMAGE_OFFSET + VIDEO_IMAGE_OFFSET) - 1) {
            return VIEW_TYPE_ADD_VIDEO;
        }

        throw new IllegalStateException("Invalid position: " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER
                || viewType == VIEW_TYPE_ADD_QUESTION
                || viewType == VIEW_TYPE_ADD_AUDIO
                || viewType == VIEW_TYPE_ADD_VIDEO) {
            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_memory_image_cell, parent, false));
        }

        if (viewType == VIEW_TYPE_QUESTION) {
            return new QuestionsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.create_memory_question_cell, parent, false));
        }

        throw new IllegalStateException("Invalid view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_HEADER) {
            ((ImageViewHolder) holder).bind(R.drawable.question_audio_video, viewType);
            return;
        }

        if (viewType == VIEW_TYPE_ADD_QUESTION) {
            ((ImageViewHolder) holder).bind(R.drawable.add_question, viewType);
            return;
        }

        if (viewType == VIEW_TYPE_ADD_AUDIO) {
            ((ImageViewHolder) holder).bind(R.drawable.add_audio, viewType);
            return;
        }

        if (viewType == VIEW_TYPE_ADD_VIDEO) {
            ((ImageViewHolder) holder).bind(R.drawable.add_video, viewType);
            return;
        }

        // view type VIEW_TYPE_QUESTION
        QuestionsViewHolder questionsViewHolder = (QuestionsViewHolder) holder;
        if (position == (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET)) {
            // new question
            mCurrentQuestionHolder = questionsViewHolder;
            questionsViewHolder.clear();
            return;
        }

        Question question = mQuestions.get(position - (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET));
        questionsViewHolder.bind(question);
    }

    @Override
    public int getItemCount() {
        return HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET + mQuestions.size() + AUDIO_IMAGE_OFFSET + VIDEO_IMAGE_OFFSET;
    }

    private void addQuestion() {
        mQuestions.add(0, mCurrentQuestionHolder.getQuestion());
        notifyDataSetChanged();
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        @ViewType
        int mType;

        ImageViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.imageview_cell);
            mImageView.setOnClickListener(this);
        }

        void bind(int imageResourceId, @ViewType int type) {
            mImageView.setImageResource(imageResourceId);
            mType = type;
        }

        @SuppressLint("SwitchIntDef")
        @Override
        public void onClick(View v) {
            switch (mType) {
                case VIEW_TYPE_HEADER:
                    break;
                case VIEW_TYPE_ADD_QUESTION:
                    addQuestion();
                    break;
                case VIEW_TYPE_ADD_AUDIO:
                    mListener.addAudio();
                    break;
                case VIEW_TYPE_ADD_VIDEO:
                    mListener.addVideo();
                    break;
            }
        }
    }

    private class QuestionsViewHolder extends RecyclerView.ViewHolder {

        private EditText mEditTextQuestion;
        private List<RadioButton> mRadioButtonsList;
        private Question mQuestion;
        private RadioGroup mRadioGroup;

        QuestionsViewHolder(View view) {
            super(view);

            mEditTextQuestion = (EditText) view.findViewById(R.id.edit_text_question);
            RadioButton radioButtonOption1 = (RadioButton) view.findViewById(R.id.radio_option1);
            RadioButton radioButtonOption2 = (RadioButton) view.findViewById(R.id.radio_option2);
            RadioButton radioButtonOption3 = (RadioButton) view.findViewById(R.id.radio_option3);
            RadioButton radioButtonOption4 = (RadioButton) view.findViewById(R.id.radio_option4);
            mRadioButtonsList = new ArrayList<>(4);
            mRadioButtonsList.add(radioButtonOption1);
            mRadioButtonsList.add(radioButtonOption2);
            mRadioButtonsList.add(radioButtonOption3);
            mRadioButtonsList.add(radioButtonOption4);

            mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        }

        void bind(Question question) {
            mQuestion = question;
            mEditTextQuestion.setText(question.getQuestion());
            mRadioButtonsList.get(0).setText(question.getOption1());
            mRadioButtonsList.get(1).setText(question.getOption2());
            mRadioButtonsList.get(2).setText(question.getOption3());
            mRadioButtonsList.get(3).setText(question.getOption4());
        }

        void clear() {
            mQuestion = null;
            mEditTextQuestion.setText(null);
            mRadioButtonsList.get(0).setText(null);
            mRadioButtonsList.get(1).setText(null);
            mRadioButtonsList.get(2).setText(null);
            mRadioButtonsList.get(3).setText(null);
        }

        Question getQuestion() {
            if (mQuestion != null) {
                return mQuestion;
            }

            Question.Builder builder = new Question.Builder()
                    .setQuestion(mEditTextQuestion.getText().toString())
                    .setOption1(mRadioButtonsList.get(0).getText().toString())
                    .setOption2(mRadioButtonsList.get(1).getText().toString())
                    .setOption3(mRadioButtonsList.get(2).getText().toString())
                    .setOption4(mRadioButtonsList.get(3).getText().toString())
                    .setCorrectOption(getCorrectOption());

            mQuestion = builder.question();
            return mQuestion;
        }

        private @Question.QuestionOption int getCorrectOption() {
            int radioButtonId = mRadioGroup.getCheckedRadioButtonId();
            switch (radioButtonId) {
                case R.id.radio_option1:
                    return Question.QUESTION_OPTION_1;
                case R.id.radio_option2:
                    return Question.QUESTION_OPTION_2;
                case R.id.radio_option3:
                    return Question.QUESTION_OPTION_3;
                case R.id.radio_option4:
                    return Question.QUESTION_OPTION_4;
                default:
                    throw new IllegalStateException("Invalid radio button id");
            }
        }
    }

    public interface Listener {
        void addVideo();
        void addAudio();
        void save(List<Question> questions);
    }

    @Retention(SOURCE)
    @IntDef({VIEW_TYPE_HEADER, VIEW_TYPE_ADD_QUESTION, VIEW_TYPE_ADD_AUDIO, VIEW_TYPE_ADD_VIDEO, VIEW_TYPE_QUESTION})
    @interface ViewType {}
    static final int VIEW_TYPE_HEADER = 0;
    static final int VIEW_TYPE_ADD_QUESTION = 1;
    static final int VIEW_TYPE_ADD_AUDIO = 2;
    static final int VIEW_TYPE_ADD_VIDEO = 3;
    static final int VIEW_TYPE_QUESTION = 4;
}
