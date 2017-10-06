package hu.bme.aut.shoppinglist.data;

import io.realm.RealmObject ;
import io.realm.annotations.PrimaryKey;

public class ShoppingItem extends RealmObject
{
    private String name;
    private boolean status;
    private String description;
    private int price;
    private String category;
    private int categoryID;
    private int amount;

    @PrimaryKey
    private String itemID;


    public ShoppingItem()
    {}

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

    public void setPrice(int price)
    {
        this.price = price;
    }

    public int getPrice()
    {
        return price;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategoryID(int id)
    {
        this.categoryID =id;
    }

    public int getCategoryID()
    {
        return categoryID;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public int getCost()
    {
        return amount*price;
    }

    public String getItemID()
    {
        return itemID;
    }

}
