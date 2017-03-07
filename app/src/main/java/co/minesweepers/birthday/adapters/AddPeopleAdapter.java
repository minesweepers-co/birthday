package co.minesweepers.birthday.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.Utils;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;

public class AddPeopleAdapter extends RecyclerView.Adapter<AddPeopleAdapter.ViewHolder> {

    private List<Person> mPeople;
    private Listener mListener;

    public AddPeopleAdapter(@NonNull List<Person> people, @NonNull Listener listener) {
        mPeople = people;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mPeople.size() == 0) {
            // when there are no people, do nothing
            return;
        }

        if (position == mPeople.size()) {
            // when we have displayed all people, just clear the last cell contents as it might have recycled
            holder.clearText();
            return;
        }

        holder.setPerson(mPeople.get(position));
    }

    @Override
    public int getItemCount() {
        return mPeople.size() + 1; // Always show one more than no. of people so user can add a new person
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText mEditTextName;

        ViewHolder(View view) {
            super(view);
            mEditTextName = (EditText) view.findViewById(R.id.edit_text_person_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview_add_person);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String name = mEditTextName.getText().toString();
            if (Utils.isEmpty(name)) {
                return;
            }

            Person person;
            String personId = (String) mEditTextName.getTag();
            if (Utils.isEmpty(personId)) {
                // When there is no tag set, it means user is adding a new person
                // In this case we create a person and add it to the memory and also to the internal list
                person = new Person();
                person.setName(name);
                Memory.getInstance().addPerson(person);
                mPeople.add(person);
                mEditTextName.setTag(person.getId());
            } else {
                person = Memory.getInstance().getPerson(personId);
                person.setName(name);
            }

            mListener.createOrEditMemory(person);
        }

        void setPerson(Person person) {
            mEditTextName.setText(person.getName());
            mEditTextName.setTag(person.getId());
        }

        void clearText() {
            mEditTextName.setText("");
            mEditTextName.setTag("");
        }
    }

    public interface Listener {
        void createOrEditMemory(Person person);
    }
}
