package hu.bme.aut.shoppinglist.touch;

/**
 * Created by User on 27-Sep-17.
 */

public interface ShoppingItemTouchHelperAdapter
{
    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}
