package sample.model;

public class Sale
{
    private int id;
    private int shopId;
    private int recipeId;
    private String recipeName;
    private int portions;
    private int weekNumber;

    public Sale(int id,int shopId, int portions, int weekNumber)
    {
        this.id = id;
        this.portions = portions;
        this.weekNumber = weekNumber;
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

    public int getWeekNumber()
    {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber)
    {
        this.weekNumber = weekNumber;
    }
}
