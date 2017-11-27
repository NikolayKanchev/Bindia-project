package sample.model;

import java.time.LocalDate;

public class Order
{
    private int id;
    private String ingredientName;
    private double amount;
    private int shopId;
    private Ingredient ingredient;
    private LocalDate date;

    public Order(int id, double amount, int shopId, LocalDate date)
    {
        this.id = id;
        this.amount = amount;
        this.shopId = shopId;
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient)
    {
        this.ingredient = ingredient;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getIngredientName()
    {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName)
    {
        this.ingredientName = ingredientName;
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

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return this.id + "\t" + this.date;
    }
}
