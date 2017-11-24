package sample.model;

public class Shop
{
    private int id;
    private String name;
    private String manager;
    private String address;

    public Shop(int id, String name, String manager, String address)
    {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.address = address;
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

    public String getManager()
    {
        return manager;
    }

    public void setManager(String manager)
    {
        this.manager = manager;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
