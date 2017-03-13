package co.minesweepers.birthday.adapters.helpers;

public interface ItemTouchEventsListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
