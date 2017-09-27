package hu.bme.aut.shoppinglist.data;

/**
 * Created by User on 26-Sep-17.
 */

public class ShoppingItem
{
    private String name;
    private boolean status;
    private String description;
    private int price;

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

}
