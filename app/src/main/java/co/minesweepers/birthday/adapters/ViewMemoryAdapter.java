package co.minesweepers.birthday.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.model.Person;
import co.minesweepers.birthday.model.Question;

public class ViewMemoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NAME_CELL_OFFSET = 1;
    private static final int VIEW_TYPE_NAME = 0;
    private static final int VIEW_TYPE_QUESTION = 1;

    private Person mPerson;
    private Listener mListener;

    public ViewMemoryAdapter(@NonNull Listener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_NAME;
        }

        return VIEW_TYPE_QUESTION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NAME) {
            return new ViewMemoryAdapter.NameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_memory_name_cell, parent, false));
        }

        if (viewType == VIEW_TYPE_QUESTION) {
            return new ViewMemoryAdapter.QuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_memory_question_cell, parent, false));
        }

        throw new IllegalStateException("Invalid view type: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NAME) {
            ((NameViewHolder) holder).bind(mPerson.getName());
            return;
        }

        ((QuestionViewHolder) holder).bind(mPerson.getAllQuestions().get(position - NAME_CELL_OFFSET));
    }

    @Override
    public int getItemCount() {
        if (mPerson == null) {
            return 0;
        }

        return mPerson.getAllQuestions().size() + NAME_CELL_OFFSET;
    }

    public void setPerson(Person person) {
        mPerson = person;
        notifyDataSetChanged();
    }

    private class NameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;

        NameViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.text_view_name);
            ImageView imageViewPrevious = (ImageView) itemView.findViewById(R.id.image_view_previous);
            imageViewPrevious.setOnClickListener(this);

            ImageView imageViewNext = (ImageView) itemView.findViewById(R.id.image_view_next);
            imageViewNext.setOnClickListener(this);
        }

        void bind(String name) {
            mName.setText(name);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_view_previous:
                    mListener.onPreviousPersonClicked();
                    break;
                case R.id.image_view_next:
                    mListener.onNextPersonClicked();
                    break;
            }
        }
    }

    private class QuestionViewHolder extends RecyclerView.ViewHolder {

        private List<RadioButton> mRadioButtons;
        private RadioGroup mRadioGroup;
        private TextView mTextViewQuestion;

        private Question mQuestion;

        QuestionViewHolder(View itemView) {
            super(itemView);

            mTextViewQuestion = (TextView) itemView.findViewById(R.id.text_view_question);
            mRadioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            RadioButton radioButton1 = (RadioButton) itemView.findViewById(R.id.radio_option1);
            RadioButton radioButton2 = (RadioButton) itemView.findViewById(R.id.radio_option2);
            RadioButton radioButton3 = (RadioButton) itemView.findViewById(R.id.radio_option3);
            RadioButton radioButton4 = (RadioButton) itemView.findViewById(R.id.radio_option4);

            mRadioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3, radioButton4);
        }

        void bind(Question question) {
            mQuestion = question;
            mTextViewQuestion.setText(mQuestion.getQuestion());
            List<String> options = mQuestion.getOptions();
            for (int i=0; i < options.size(); i++) {
                String option = options.get(i);
                RadioButton radioButton = mRadioButtons.get(i);
                if (!Utils.isEmpty(option)) {
                    radioButton.setText(option);
                } else {
                    radioButton.setVisibility(View.GONE);
                }
            }
        }
    }

    public interface Listener {
        void onNextPersonClicked();
        void onPreviousPersonClicked();
    }
}
