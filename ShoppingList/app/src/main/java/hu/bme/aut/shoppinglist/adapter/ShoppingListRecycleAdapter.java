package hu.bme.aut.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import hu.bme.aut.shoppinglist.MainActivity;
import hu.bme.aut.shoppinglist.R;
import hu.bme.aut.shoppinglist.data.ShoppingItem;
import hu.bme.aut.shoppinglist.touch.ShoppingItemTouchHelperAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ShoppingListRecycleAdapter extends RecyclerView.Adapter<ShoppingListRecycleAdapter.ViewHolder> implements ShoppingItemTouchHelperAdapter
{

    private List<ShoppingItem> shoppingItemList;

    private Context context;

    private Realm realmItem;

    public ShoppingListRecycleAdapter(Context context, Realm realmItem)
    {
        this.context = context;
        this.realmItem = realmItem;

        RealmResults<ShoppingItem> itemResult = realmItem.where(ShoppingItem.class).findAll().sort("name", Sort.ASCENDING);

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
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.tvItem.setText(shoppingItemList.get(position).getName());
        holder.cbStatus.setChecked(shoppingItemList.get(position).getStatus());
        holder.tvDescription.setText(shoppingItemList.get(position).getDescription());
        holder.tvAmount.setText(String.valueOf(shoppingItemList.get(position).getAmount()));
        holder.tvPrice.setText(String.valueOf(shoppingItemList.get(position).getPrice()) +" HUF");

        switch (shoppingItemList.get(position).getCategory())
        {
            case "Food":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_room_service_black_24dp);
                break;
            }
            case "Technology":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_devices_other_black_24dp);
                break;
            }
            case "Furniture":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_weekend_24dp);
                break;
            }
            case "Clothes":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_local_offer_24dp);
                break;
            }
            case "Accessories":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_toys_24dp);
                break;
            }
            case "Personal care":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_face_24dp);
                break;
            }
            case "Beverages":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_local_drink_24dp);
                break;
            }
            case "Other":
            {
                holder.ivCategoryImage.setImageResource(R.drawable.ic_help_black_24dp);
                break;
            }

        }

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

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)context).openEditActivity(holder.getAdapterPosition(),
                        shoppingItemList.get(holder.getAdapterPosition()).getItemID()
                );
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return shoppingItemList.size();
    }

    public void addShoppingItem(String itemName, String itemCategory, int itemCategoryId, int itemPrice, int itemAmount, String itemDescription)
    {
        realmItem.beginTransaction();
        ShoppingItem newShoppingItem = realmItem.createObject(ShoppingItem.class, UUID.randomUUID().toString());
        newShoppingItem.setName(itemName);
        newShoppingItem.setCategory(itemCategory);
        newShoppingItem.setPrice(itemPrice);
        newShoppingItem.setDescription(itemDescription);
        newShoppingItem.setStatus(false);
        newShoppingItem.setCategoryID(itemCategoryId);
        newShoppingItem.setAmount(itemAmount);
        realmItem.commitTransaction();

        shoppingItemList.add(0, newShoppingItem);

        notifyItemInserted(0);
    }

    public void updateShoppingItem(String itemID, int positionToEdit)
    {
        ShoppingItem item = realmItem.where(ShoppingItem.class)
                .equalTo("itemID", itemID)
                .findFirst();

        shoppingItemList.set(positionToEdit, item);

        notifyItemChanged(positionToEdit);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private CheckBox cbStatus;
        private TextView tvItem;
        private ImageView ivCategoryImage;
        private TextView tvPrice;
        private TextView tvAmount;
        private TextView tvDescription;



        public ViewHolder(View itemView)
        {
            super(itemView);

            cbStatus = (CheckBox) itemView.findViewById(R.id.cbStatus);
            ivCategoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

        }
    }

    public void deleteShoppingList()
    {
        while(shoppingItemList.size() != 0)
        {

            realmItem.beginTransaction();
            shoppingItemList.get(0).deleteFromRealm();
            realmItem.commitTransaction();

            shoppingItemList.remove(0);
        }

        /*
        for(int i=0; i < shoppingItemList.size(); )
        {
            realmItem.beginTransaction();
            shoppingItemList.get(i).deleteFromRealm();
            realmItem.commitTransaction();

            shoppingItemList.remove(i);
        }
        */
        notifyDataSetChanged();
    }

    public int totalCost()
    {
        int cost=0;

        for(int i=0; i < shoppingItemList.size(); i++)
        {
            if(shoppingItemList.get(i).getStatus() != true)
            {
                cost = cost + shoppingItemList.get(i).getCost();
            }
        }

        return cost;
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

}
