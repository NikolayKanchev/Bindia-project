package sample.db;

import sample.model.Order;
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

    public static ArrayList<Order> getAllOrdersByShopID(int id)
    {
        ArrayList<Order> orders = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`orders` WHERE `shop_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                orders.add(
                        new Order(
                                rs.getInt("id"),
                                rs.getString("ingredient"),
                                rs.getDouble("amount"),
                                rs.getInt("shop_id")
                        )
                );
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return orders;
    }

    public static void saveOrder(String ingredient, String amount, int selectedShopId)
    {
        String sql = "INSERT INTO `bindia`.`orders` (`" +
                "id`, `ingredient`, `amount`, `shop_id`)" +
                "VALUES (NULL, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ingredient);
            ps.setDouble(2, Double.parseDouble(amount));
            ps.setInt(3, selectedShopId);

            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteOrderById(int id)
    {
        String sql = "DELETE FROM orders WHERE id = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, id);

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveOrderChanges(Order order)
    {
        String sql = "UPDATE `bindia`.`orders` SET " +
                "`ingredient` = ?, " +
                "`amount` = ? "+
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, order.getIngredient());
            ps.setDouble(2, order.getAmount());
            ps.setInt(3, order.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
