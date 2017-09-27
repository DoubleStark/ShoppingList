package hu.bme.aut.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.bme.aut.shoppinglist.R;
import hu.bme.aut.shoppinglist.data.ShoppingItem;
import hu.bme.aut.shoppinglist.touch.ShoppingItemTouchHelperAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShoppingListRecycleAdapter extends RecyclerView.Adapter<ShoppingListRecycleAdapter.ViewHolder> implements ShoppingItemTouchHelperAdapter
{

    private List<ShoppingItem> shoppingItemList;

    private Context context;

    private Realm realmItem;

    public ShoppingListRecycleAdapter(Context context)
    {
        this.context = context;

        realmItem = Realm.getDefaultInstance();
        RealmResults<ShoppingItem> itemResult = realmItem.where(ShoppingItem.class).findAll();

        shoppingItemList = new ArrayList<ShoppingItem>();

        for (int i = 0; i < itemResult.size(); i++)
        {
            shoppingItemList.add(itemResult.get(i));

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingitem_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvItem.setText(shoppingItemList.get(position).getName());
        holder.cbStatus.setChecked(shoppingItemList.get(position).getStatus());

        holder.cbStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                realmItem.beginTransaction();
                shoppingItemList.get(holder.getAdapterPosition()).setStatus(holder.cbStatus.isChecked());
                realmItem.commitTransaction();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return shoppingItemList.size();
    }

    public void addShoppingItem(ShoppingItem item)
    {
        shoppingItemList.add(0, item);
        notifyItemInserted(0);
    }

    public void addShoppingItem(String itemName) {
        realmItem.beginTransaction();
        ShoppingItem newShoppingItem = realmItem.createObject(ShoppingItem.class);
        newShoppingItem.setName(itemName);
        newShoppingItem.setStatus(false);
        realmItem.commitTransaction();

        shoppingItemList.add(0, newShoppingItem);

        notifyItemInserted(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbStatus;
        private TextView tvItem;

        public ViewHolder(View itemView) {
            super(itemView);

            cbStatus = (CheckBox) itemView.findViewById(R.id.cbStatus);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
        }
    }

    @Override
    public void onItemDismiss(int position)
    {
        realmItem.beginTransaction();
        shoppingItemList.get(position).deleteFromRealm();
        realmItem.commitTransaction();


        shoppingItemList.remove(position);

        // refreshes the whole list
        //notifyDataSetChanged();
        // refreshes just the relevant part that has been deleted
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /*todoList.add(toPosition, todoList.get(fromPosition));
        todoList.remove(fromPosition);*/

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(shoppingItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(shoppingItemList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);
    }

    public void closeRealm()
    {
        realmItem.close();
    }

}
