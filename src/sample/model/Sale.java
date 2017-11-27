package sample.model;

import java.time.LocalDate;

public class Sale
{
    private int id;
    private int shopId;
    private int recipeId;
    private String recipeName;
    private int portions;
    private LocalDate date;

    public Sale(int id,int shopId, int portions, LocalDate date)
    {
        this.id = id;
        this.portions = portions;
        this.date = date;
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

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }

    public int getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public void setRecipeName(String recipeName)
    {
        this.recipeName = recipeName;
    }

    public int getPortions()
    {
        return portions;
    }

    public void setPortions(int portions)
    {
        this.portions = portions;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}
