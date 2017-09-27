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

public class ShoppingListRecycleAdapter extends RecyclerView.Adapter<ShoppingListRecycleAdapter.ViewHolder>
{

    private List<ShoppingItem> shoppingItemList;

    private Context context;

    public ShoppingListRecycleAdapter(Context context)
    {
        this.context = context;

        shoppingItemList = new ArrayList<ShoppingItem>();

        for (int i = 0; i < 20; i++)
        {
            shoppingItemList.add(new ShoppingItem("Shopping Item "+i, false));
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

        holder.cbStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingItemList.get(holder.getAdapterPosition()).setStatus(holder.cbStatus.isChecked());
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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbStatus;
        private TextView tvItem;

        public ViewHolder(View itemView) {
            super(itemView);

            cbStatus = (CheckBox) itemView.findViewById(R.id.cbStatus);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
        }
    }
}
