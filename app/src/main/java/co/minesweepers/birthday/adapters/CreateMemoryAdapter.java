package co.minesweepers.birthday.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.model.Person;
import co.minesweepers.birthday.model.Question;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class CreateMemoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CreateMemoryAdapter";

    private static final int HEADER_IMAGE_OFFSET = 1;
    private static final int QUESTION_IMAGE_OFFSET = 1;
    private static final int AUDIO_IMAGE_OFFSET = 1;
    private static final int VIDEO_IMAGE_OFFSET = 1;
    private static final int NEW_QUESTION_OFFSET = 1;

    private Listener mListener;
    private Person mPerson;
    private QuestionsViewHolder mCurrentQuestionHolder;

    public CreateMemoryAdapter(@NonNull Person person, @NonNull Listener listener) {
        mListener = listener;
        mPerson = person;
    }

    @Override
    public @ViewType int getItemViewType(int position) {
        if (position == HEADER_IMAGE_OFFSET - 1) {
            return VIEW_TYPE_HEADER;
        }

        if (position == (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET) - 1) {
            return VIEW_TYPE_ADD_QUESTION;
        }

        int noOfQuestions = mPerson.getAllQuestions().size();
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

        Question question = mPerson.getAllQuestions().get(position - (HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET));
        questionsViewHolder.bind(question);
    }

    @Override
    public int getItemCount() {
        return HEADER_IMAGE_OFFSET + QUESTION_IMAGE_OFFSET + NEW_QUESTION_OFFSET + mPerson.getAllQuestions().size() + AUDIO_IMAGE_OFFSET + VIDEO_IMAGE_OFFSET;
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
                    mListener.addQuestion(mCurrentQuestionHolder.getQuestion());
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

    private class QuestionsViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener {

        private EditText mEditTextQuestion;
        private List<RadioButton> mRadioButtonsList;
        private List<EditText> mEditTextList;
        private Question mQuestion;

        QuestionsViewHolder(View view) {
            super(view);

            mEditTextQuestion = (EditText) view.findViewById(R.id.edit_text_question);
            mEditTextQuestion.setOnFocusChangeListener(this);

            RadioButton radioButtonOption1 = (RadioButton) view.findViewById(R.id.radio_option1);
            RadioButton radioButtonOption2 = (RadioButton) view.findViewById(R.id.radio_option2);
            RadioButton radioButtonOption3 = (RadioButton) view.findViewById(R.id.radio_option3);
            RadioButton radioButtonOption4 = (RadioButton) view.findViewById(R.id.radio_option4);
            mRadioButtonsList = new ArrayList<>(4);
            mRadioButtonsList.add(radioButtonOption1);
            mRadioButtonsList.add(radioButtonOption2);
            mRadioButtonsList.add(radioButtonOption3);
            mRadioButtonsList.add(radioButtonOption4);

            radioButtonOption1.setOnCheckedChangeListener(this);
            radioButtonOption2.setOnCheckedChangeListener(this);
            radioButtonOption3.setOnCheckedChangeListener(this);
            radioButtonOption4.setOnCheckedChangeListener(this);

            EditText editTextOption1 = (EditText) view.findViewById(R.id.edit_text_option1);
            editTextOption1.setOnFocusChangeListener(this);
            EditText editTextOption2 = (EditText) view.findViewById(R.id.edit_text_option2);
            editTextOption2.setOnFocusChangeListener(this);
            EditText editTextOption3 = (EditText) view.findViewById(R.id.edit_text_option3);
            editTextOption3.setOnFocusChangeListener(this);
            EditText editTextOption4 = (EditText) view.findViewById(R.id.edit_text_option4);
            editTextOption4.setOnFocusChangeListener(this);
            mEditTextList = new ArrayList<>(4);
            mEditTextList.add(editTextOption1);
            mEditTextList.add(editTextOption2);
            mEditTextList.add(editTextOption3);
            mEditTextList.add(editTextOption4);
        }

        void bind(Question question) {
            mQuestion = question;
            mEditTextQuestion.setText(question.getQuestion());
            mEditTextList.get(0).setText(question.getOption1());
            mEditTextList.get(1).setText(question.getOption2());
            mEditTextList.get(2).setText(question.getOption3());
            mEditTextList.get(3).setText(question.getOption4());
            setCheckedRadioButton(question.getCorrectOption());
        }

        void clear() {
            mQuestion = null;
            mEditTextQuestion.setText(null);
            mEditTextList.get(0).setText(null);
            mEditTextList.get(1).setText(null);
            mEditTextList.get(2).setText(null);
            mEditTextList.get(3).setText(null);
            setCheckedRadioButton(0);
        }

        Question getQuestion() {
            if (mQuestion != null) {
                return mQuestion;
            }

            mQuestion = new Question.Builder()
                    .setQuestion(mEditTextQuestion.getText().toString())
                    .setOption1(mEditTextList.get(0).getText().toString())
                    .setOption2(mEditTextList.get(1).getText().toString())
                    .setOption3(mEditTextList.get(2).getText().toString())
                    .setOption4(mEditTextList.get(3).getText().toString())
                    .setCorrectOption(getCorrectOption())
                    .question();

            return mQuestion;
        }

        private int getCorrectOption() {
            for (int i=0; i < mRadioButtonsList.size(); i++) {
                RadioButton radioButton = mRadioButtonsList.get(i);
                if (radioButton.isChecked()) {
                    return i;
                }
            }

            return -1;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_option1:
                    if (isChecked) {
                        setCheckedRadioButton(0);
                    }
                    break;
                case R.id.radio_option2:
                    if (isChecked) {
                        setCheckedRadioButton(1);
                    }
                    break;
                case R.id.radio_option3:
                    if (isChecked) {
                        setCheckedRadioButton(2);
                    }
                    break;
                case R.id.radio_option4:
                    if (isChecked) {
                        setCheckedRadioButton(3);
                    }
                    break;
                default:
                    throw new IllegalStateException("Invalid radio button id");
            }
        }

        private void setCheckedRadioButton(int checkedRadioButtonIndex) {
            for (int i=0; i < mRadioButtonsList.size(); i++) {
                RadioButton radioButton = mRadioButtonsList.get(i);
                if (i == checkedRadioButtonIndex) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }

            if (mQuestion != null) {
                new Question.Builder(mQuestion)
                        .setCorrectOption(getCorrectOption());
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus || mQuestion == null) {
                return;
            }

            Question.Builder builder = new Question.Builder(mQuestion);
            switch (v.getId()) {
                case R.id.edit_text_question:
                    builder.setQuestion(mEditTextQuestion.getText().toString());
                    break;
                case R.id.edit_text_option1:
                    builder.setOption1(mEditTextList.get(0).getText().toString());
                    break;

                case R.id.edit_text_option2:
                    builder.setOption2(mEditTextList.get(1).getText().toString());
                    break;

                case R.id.edit_text_option3:
                    builder.setOption3(mEditTextList.get(2).getText().toString());
                    break;

                case R.id.edit_text_option4:
                    builder.setOption4(mEditTextList.get(3).getText().toString());
                    break;
            }
        }
    }

    public interface Listener {
        void addQuestion(Question question);
        void addVideo();
        void addAudio();
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
