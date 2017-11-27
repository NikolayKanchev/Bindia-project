package sample.model;

public class OrderException
{
    private int id;
    private int orderId;
    private double missing;

    private String ingredientName;

    public OrderException(int id, int orderId, double missing)
    {
        this.id = id;
        this.orderId = orderId;
        this.missing = missing;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public double getMissing()
    {
        return missing;
    }

    public void setMissing(double missing)
    {
        this.missing = missing;
    }

    public String getIngredientName()
    {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName)
    {
        this.ingredientName = ingredientName;
    }
}
