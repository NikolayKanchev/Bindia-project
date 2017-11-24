package sample.db;

import sample.model.Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBWrapper
{

    private static Connection conn = DBCon.getConn();

    public static ArrayList<Shop> getAllShops()
    {
        ArrayList<Shop> shops = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`shops`";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                shops.add(
                        new Shop(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("manager"),
                                rs.getString("address")
                        )
                );
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return shops;
    }

    public static void deleteShopById(int id)
    {
        String sql = "DELETE FROM shops WHERE id = " + id;

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void addNewShop(String name, String manager, String address)
    {
        String sql = "INSERT INTO `bindia`.`shops` (`" +
                     "id`, `name`, `manager`, `address`)" +
                     "VALUES (NULL, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, manager);
            ps.setString(3, address);

            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveShopChanges(Shop shop)
    {
        String sql = "UPDATE `bindia`.`shops` SET " +
                "`name` = ?, " +
                "`manager` = ?, "+
                "`address` = ? WHERE `shops`.`id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, shop.getName());
            ps.setString(2, shop.getManager());
            ps.setString(3, shop.getAddress());
            ps.setInt(4, shop.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
