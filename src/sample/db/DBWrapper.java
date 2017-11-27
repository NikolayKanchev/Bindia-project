package sample.db;

import sample.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBWrapper
{

    private static Connection conn = DBCon.getConn();
    private static Ingredient allIngredients;

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
                Order newOrder = new Order(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getInt("shop_id"),
                        rs.getDate("date").toLocalDate());

                Ingredient ingredient = DBWrapper.getIngredientById(rs.getInt("ingredient_id"));

                newOrder.setIngredient(ingredient);

                newOrder.setIngredientName(ingredient.getName());

                orders.add(newOrder);
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return orders;
    }

    public static void saveOrder(Ingredient ingredient, String amount, int selectedShopId)
    {
        String sql = "INSERT INTO `bindia`.`orders` (`" +
                "id`, `ingredient_id`, `amount`, `shop_id`, `date`)" +
                "VALUES (NULL, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ingredient.getId());
            ps.setDouble(2, Double.parseDouble(amount));
            ps.setInt(3, selectedShopId);
            ps.setDate(4, Date.valueOf(LocalDate.now()));

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
                "`ingredient_id` = ?, " +
                "`amount` = ? "+
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, order.getIngredient().getId());
            ps.setDouble(2, order.getAmount());
            ps.setInt(3, order.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Ingredient> getAllIngredients()
    {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`ingredients`";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                ingredients.add(
                        new Ingredient(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("quantity"),
                                rs.getString("measure")
                        )
                );
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return ingredients;
    }

    public static ArrayList<Recipe> getAllRecipes()
    {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`recipes`";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                recipes.add(
                        new Recipe(
                                rs.getInt("id"),
                                rs.getString("name")
                        )
                );
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return recipes;
    }

    public static ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId)
    {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`recipe_ingredients` WHERE `recipes_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                RecipeIngredient recipeIngredient = new RecipeIngredient(
                        rs.getInt("id"),
                        rs.getInt("recipes_id"),
                        rs.getDouble("amount"));

                Ingredient ingredient = getIngredientById(rs.getInt("ingredients_id"));

                recipeIngredient.setIngredient(ingredient);

                recipeIngredient.setIngName(ingredient.getName());

                ingredients.add(recipeIngredient);
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return ingredients;
    }

    private static Ingredient getIngredientById(int ingredients_id)
    {
        Ingredient ingredient = null;

        try
        {
            String sql = "SELECT * FROM `bindia`.`ingredients` WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ingredients_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
               ingredient = new Ingredient(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getDouble("quantity"),
                       rs.getString("measure"));
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return ingredient;
    }

    public static void addIngredientToRecipe(int recipeId, int ingredientId)
    {

        String sql = "INSERT INTO `bindia`.`recipe_ingredients` (`" +
                "id`, `ingredients_id`, `amount`, `recipes_id`)" +
                "VALUES (NULL, ?, NULL, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ingredientId);
            ps.setInt(2, recipeId);
            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteRecipeIngredient(int recipeIngId)
    {
        String sql = "DELETE FROM `recipe_ingredients` WHERE id = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, recipeIngId);

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveRecipe(Recipe recipe)
    {
        String sql = "UPDATE `bindia`.`recipes` SET " +
                "`name` = ? " +
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, recipe.getName());
            ps.setInt(2, recipe.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveRecipeIngredient(RecipeIngredient recipeIngredient)
    {
        String sql = "UPDATE `bindia`.`recipe_ingredients` SET " +
                "`ingredients_id` = ?, " +
                "`amount` = ?, " +
                "`recipes_id` = ? "+
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, recipeIngredient.getIngredient().getId());
            ps.setDouble(2, recipeIngredient.getAmount());
            ps.setInt(3, recipeIngredient.getRecipeId());
            ps.setInt(4, recipeIngredient.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void createRecipe(String recipeName)
    {
        String sql = "INSERT INTO `bindia`.`recipes` (`" +
                "id`, `name`)" +
                "VALUES (NULL, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, recipeName);
            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteRecipe(int recipeId)
    {
        String sql = "DELETE FROM `recipes` WHERE id = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, recipeId);

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveSale(int shopId, int recipeId, int soldPortions)
    {
        String sql = "INSERT INTO `bindia`.`sales` (`" +
                "id`, `shop_id`, `recipe_id`, `sold_portions`, `date`)" +
                "VALUES (NULL, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, shopId);
            ps.setInt(2, recipeId);
            ps.setInt(3, soldPortions);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Sale> getAllSalesByShopID(int id)
    {
        ArrayList<Sale> sales = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`sales` WHERE `shop_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("id"),
                        rs.getInt("shop_id"),
                        rs.getInt("sold_portions"),
                        rs.getDate("date").toLocalDate());

                Recipe recipe = DBWrapper.getRecipeById(rs.getInt("recipe_id"));
                sale.setRecipeId(recipe.getId());
                sale.setRecipeName(recipe.getName());

                sales.add(sale);
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return sales;
    }

    private static Recipe getRecipeById(int recipe_id)
    {
        Recipe recipe = null;

        try
        {
            String sql = "SELECT * FROM `bindia`.`recipes` WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, recipe_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"));
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return recipe;
    }

    public static ArrayList<OrderException> getAllExceptionsByShopID(int id)
    {
        ArrayList<OrderException> exceptions = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`orders_exceptions` WHERE `shop_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                OrderException exception = new OrderException(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getDouble("missing"));

                int ingId = DBWrapper.getIngIdFromOrder(rs.getInt("order_id"));

                exception.setIngredientName(getIngredientName(ingId));

                exceptions.add(exception);
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return exceptions;
    }

    private static String getIngredientName(int ingId)
    {
        String name = "";

        try
        {
            String sql = "SELECT `name` FROM `bindia`.`ingredients` WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ingId);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                name = rs.getString("name");
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return name;

    }

    private static int getIngIdFromOrder(int orderId)
    {
        int ingId = 0;

        try
        {
            String sql = "SELECT * FROM `bindia`.`orders` WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                ingId = rs.getInt("ingredient_id");
            }
            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return ingId;
    }

    public static void saveOrderException(int selectedOrderId, double missing, int shopId)
    {
        String sql = "INSERT INTO `bindia`.`orders_exceptions` (`" +
                "id`, `order_id`, `missing`, `shop_id`)" +
                "VALUES (NULL, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, selectedOrderId);
            ps.setDouble(2, missing);
            ps.setInt(3, shopId);

            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteOrderException(int id)
    {
        String sql = "DELETE FROM `orders_exceptions` WHERE id = ?";

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

    public static void saveOrderExceptionChanges(OrderException exception)
    {
        String sql = "UPDATE `bindia`.`orders_exceptions` SET " +
                "`missing` = ? " +
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, exception.getMissing());
            ps.setInt(2, exception.getId());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
