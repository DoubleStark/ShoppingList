package hu.bme.aut.shoppinglist.data;

import io.realm.RealmObject ;
import io.realm.annotations.PrimaryKey;

public class ShoppingItem extends RealmObject
{
    private String name;
    private boolean status;
    private String description;
    private int price;

    @PrimaryKey
    private String itemID;


    public ShoppingItem()
    {}

    public ShoppingItem(String name, boolean status)
    {
        this.name = name;
        this.status = status;
    }

    public boolean getStatus()
    {
        return status;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public void setName(String name)
    {
        this.name = name;

    }

    public String getName()
    {
        return name;
    }

    public String getItemID()
    {
        return itemID;
    }

}
