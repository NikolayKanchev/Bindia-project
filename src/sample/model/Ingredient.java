package sample.model;

public class Ingredient
{
    private int id;
    private String name;
    private double quantity;
    private String measure;

    public Ingredient(int id, String name, double quantity, String measure)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public String getMeasure()
    {
        return measure;
    }

    public void setMeasure(String measure)
    {
        this.measure = measure;
    }

    @Override
    public String toString()
    {
        return this.getId() + "\t" + this.name;
    }
}
