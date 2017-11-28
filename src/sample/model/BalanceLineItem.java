package sample.model;

public class BalanceLineItem
{
    private int id;
    private int shopId;
    private String ingredientName;
    private double orderedAmount;
    private double soldAmount;
    private double leftAmount;
    private double exceptionAmount;

    public BalanceLineItem(int id, int shopId, String ingredientName, double orderedAmount,
                           double soldAmount, double leftAmount, double exceptionAmount)
    {
        this.id = id;
        this.shopId = shopId;
        this.ingredientName = ingredientName;
        this.orderedAmount = orderedAmount;
        this.soldAmount = soldAmount;
        this.leftAmount = leftAmount;
        this.exceptionAmount = exceptionAmount;
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

    public String getIngredientName()
    {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName)
    {
        this.ingredientName = ingredientName;
    }

    public double getOrderedAmount()
    {
        return orderedAmount;
    }

    public void setOrderedAmount(double orderedAmount)
    {
        this.orderedAmount = orderedAmount;
    }

    public double getSoldAmount()
    {
        return soldAmount;
    }

    public void setSoldAmount(double soldAmount)
    {
        this.soldAmount = soldAmount;
    }

    public double getLeftAmount()
    {
        return leftAmount;
    }

    public void setLeftAmount(double leftAmount)
    {
        this.leftAmount = leftAmount;
    }

    public double getExceptionAmount()
    {
        return exceptionAmount;
    }

    public void setExceptionAmount(double exceptionAmount)
    {
        this.exceptionAmount = exceptionAmount;
    }
}
