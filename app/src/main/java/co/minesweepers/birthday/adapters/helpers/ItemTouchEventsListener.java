package co.minesweepers.birthday.adapters.helpers;

public interface ItemTouchEventsListener {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
