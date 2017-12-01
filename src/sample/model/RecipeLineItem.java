package sample.model;

public class RecipeLineItem
{
    private int id;
    private int recipeId;
    private Ingredient ingredient;
    private String ingName;
    private double amount;

    public RecipeLineItem(int id, int recipeId, double amount)
    {
        this.id = id;
        this.recipeId = recipeId;
        this.amount = amount;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getRecipeId()
    {
        return recipeId;
    }

    public void setRecipeId(int recipeId)
    {
        this.recipeId = recipeId;
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient)
    {
        this.ingredient = ingredient;
    }

    public String getIngName()
    {
        return ingName;
    }

    public void setIngName(String ingName)
    {
        this.ingName = ingName;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }
}
