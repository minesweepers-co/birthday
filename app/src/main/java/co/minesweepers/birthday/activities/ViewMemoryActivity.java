package co.minesweepers.birthday.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.util.List;

import co.minesweepers.birthday.R;
import co.minesweepers.birthday.adapters.ViewMemoryAdapter;
import co.minesweepers.birthday.model.Memory;
import co.minesweepers.birthday.model.Person;
import it.sephiroth.android.library.tooltip.Tooltip;

public class ViewMemoryActivity extends AppCompatActivity implements ViewMemoryAdapter.Listener {

    private static final String TAG = "ViewMemoryActivity";
    
    private List<Person> mPeople;
    private RecyclerView mRecyclerView;
    private ViewMemoryAdapter mAdapter;
    private int mCurrentPersonIndex = -1;
    private Tooltip.TooltipView mTooltipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memory);

        mRecyclerView = (RecyclerView) findViewById(R.id.view_memory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ViewMemoryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        populatePeople();
        if (mPeople != null && mPeople.size() > 0) {
            mCurrentPersonIndex = 0;
            Person person = mPeople.get(mCurrentPersonIndex);
            mAdapter.setPerson(person);
        } else {
            Log.e(TAG, "No people retrieved from server");
        }
    }

    private void populatePeople() {
        Memory memory = Memory.getInstance();
        mPeople = memory.getAllPeople();
        Log.d(TAG, "Memory " + memory.getId() + " successfully created from JSON");
    }

    @Override
    public void onNextPersonClicked() {
        if (mCurrentPersonIndex == mPeople.size() - 1) {
            return;
        }

        mCurrentPersonIndex++;
        mAdapter.setPerson(mPeople.get(mCurrentPersonIndex));
    }

    @Override
    public void onPreviousPersonClicked() {
        if (mCurrentPersonIndex == 0) {
            return;
        }

        mCurrentPersonIndex--;
        mAdapter.setPerson(mPeople.get(mCurrentPersonIndex));
    }

    @Override
    public void onIncorrectAnswer(View view) {
        if (mTooltipView != null) {
            mTooltipView.remove();
            mTooltipView = null;
        }

        mTooltipView = Tooltip.make(this, new Tooltip.Builder(101)
                        .anchor(view, Tooltip.Gravity.RIGHT)
                        .closePolicy(Tooltip.ClosePolicy.TOUCH_ANYWHERE_NO_CONSUME, 3000)
                        .activateDelay(800)
                        .showDelay(100)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .text(getResources().getString(R.string.incorrect_option_selected))
                        .maxWidth(600)
                        .withArrow(true)
                        .withOverlay(false)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build());
        mTooltipView.show();
    }
}
