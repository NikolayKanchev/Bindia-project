package sample.model;

import java.time.LocalDate;

public class Exception
{
    private int id;
    private String ingredientName;
    private int ingredientId;
    private double missing;
    private LocalDate date;

    public Exception(int id, double missing, LocalDate date, int ingredientId)
    {
        this.id = id;
        this.missing = missing;
        this.date = date;
        this.ingredientId = ingredientId;
    }

    public int getId()
    {
        return id;
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

    public int getIngredientId()
    {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId)
    {
        this.ingredientId = ingredientId;
    }

    public double getMissing()
    {
        return missing;
    }

    public void setMissing(double missing)
    {
        this.missing = missing;
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
