package sample.model;

public class Order
{
    private int id;
    private String ingredient;
    private double amount;
    private int shopId;

    public Order(int id, String ingredient, double amount, int shopId)
    {
        this.id = id;
        this.ingredient = ingredient;
        this.amount = amount;
        this.shopId = shopId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(String ingredient)
    {
        this.ingredient = ingredient;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
}
