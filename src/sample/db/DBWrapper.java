package sample.db;

import sample.model.*;
import sample.model.Exception;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBWrapper
{

    private static Connection conn = DBCon.getConn();

    public static ArrayList<Restaurant> getAllRestaurants()
    {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`restaurants`";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                restaurants.add(
                        new Restaurant(
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

        return restaurants;
    }

    public static void deleteRestaurantById(int id)
    {
        String sql = "DELETE FROM restaurants WHERE id = " + id;

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

    public static void addNewRestaurant(String name, String manager, String address)
    {
        String sql = "INSERT INTO `bindia`.`restaurants` (`" +
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

    public static void saveRestaurantChanges(Restaurant restaurant)
    {
        String sql = "UPDATE `bindia`.`restaurants` SET " +
                "`name` = ?, " +
                "`manager` = ?, "+
                "`address` = ? WHERE `restaurants`.`id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getManager());
            ps.setString(3, restaurant.getAddress());
            ps.setInt(4, restaurant.getId());

            ps.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Order> getAllOrdersByResID(int id)
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

    public static void saveOrder(Ingredient ingredient, String amount, int selectedRestaurantId)
    {
        String sql = "INSERT INTO `bindia`.`orders` (`" +
                "id`, `ingredient_id`, `amount`, `shop_id`, `date`)" +
                "VALUES (NULL, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ingredient.getId());
            ps.setDouble(2, Double.parseDouble(amount));
            ps.setInt(3, selectedRestaurantId);
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

    public static ArrayList<RecipeLineItem> getRecipeIngredients(int recipeId)
    {
        ArrayList<RecipeLineItem> ingredients = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`recipe_ingredients` WHERE `recipes_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                RecipeLineItem recipeLineItem = new RecipeLineItem(
                        rs.getInt("id"),
                        rs.getInt("recipes_id"),
                        rs.getDouble("amount"));

                Ingredient ingredient = getIngredientById(rs.getInt("ingredients_id"));

                recipeLineItem.setIngredient(ingredient);

                recipeLineItem.setIngName(ingredient.getName());

                ingredients.add(recipeLineItem);
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

    public static void saveRecipeIngredient(RecipeLineItem recipeLineItem)
    {
        String sql = "UPDATE `bindia`.`recipe_ingredients` SET " +
                "`ingredients_id` = ?, " +
                "`amount` = ?, " +
                "`recipes_id` = ? "+
                "WHERE `id` = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, recipeLineItem.getIngredient().getId());
            ps.setDouble(2, recipeLineItem.getAmount());
            ps.setInt(3, recipeLineItem.getRecipeId());
            ps.setInt(4, recipeLineItem.getId());

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

    public static void saveSale(int restaurantId, int recipeId, int soldPortions)
    {
        String sql = "INSERT INTO `bindia`.`sales` (`" +
                "id`, `shop_id`, `recipe_id`, `sold_portions`, `date`)" +
                "VALUES (NULL, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, restaurantId);
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

    public static ArrayList<Sale> getAllSalesByResID(int id)
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

    public static ArrayList<Exception> getAllExceptionsByRestaurantID(int id)
    {
        ArrayList<Exception> exceptions = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM `bindia`.`exceptions` WHERE `shop_id` = ?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Exception exception = new Exception(
                        rs.getInt("id"),
                        rs.getDouble("missing"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("ingredient_id")
                );

                exception.setIngredientName(getIngredientName(rs.getInt("ingredient_id")));

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

    public static void saveException(int ingId, double missing, int shopId)
    {
        String sql = "INSERT INTO `bindia`.`exceptions` (`id`, `date`, `missing`, `shop_id`, `ingredient_id`) VALUES (NULL, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setDouble(2, missing);
            ps.setInt(3, shopId);
            ps.setInt(4, ingId);

            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteException(int id)
    {
        String sql = "DELETE FROM `exceptions` WHERE id = ?";

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

    public static void saveExceptionChanges(Exception exception)
    {
        String sql = "UPDATE `bindia`.`exceptions` SET " +
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

    public static ArrayList<BalanceLineItem> getBalanceItems(int restaurantId, LocalDate fromDate, LocalDate toDate)
    {
        ArrayList<BalanceLineItem> balanceLineItems = new ArrayList<>();

        String sql = "SELECT DISTINCT\n" +
                "  ingredients.id,\n" +
                "  balance_logs.shop_id,\n" +
                "  ingredients.name,\n" +
                "  (SELECT SUM(amount) FROM balance_logs WHERE operation = \"ordered\" AND ingredients.id = ingredient_id) AS ordered_sum,\n" +
                "  (SELECT SUM(amount) FROM balance_logs WHERE operation = \"sold\" AND ingredients.id = ingredient_id) AS sold_sum,\n" +
                "  (SELECT SUM(exceptions.missing) FROM exceptions WHERE ingredients.id = exceptions.ingredient_id) AS exceptions_sum,\n" +
                "  (SELECT  coalesce(ordered_sum, 0)) AS ordered_amount,\n" +
                "  (SELECT  coalesce(sold_sum, 0)) AS sold_amount,\n" +
                "  (SELECT  coalesce(exceptions_sum, 0)) AS exception_amount,\n" +
                "  (SELECT ordered_amount - sold_amount - exception_amount) AS left_amount\n" +
                "FROM  ingredients, balance_logs\n" +
                "WHERE ingredients.id = ingredient_id AND shop_id = ?\n" +
                "                                              AND date BETWEEN ? AND ?";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, restaurantId);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                BalanceLineItem balanceLineItem = new BalanceLineItem(
                                                        rs.getInt("id"),
                                                        rs.getInt("shop_id"),
                                                        rs.getString("name"),
                                                        rs.getDouble("ordered_amount"),
                                                        rs.getDouble("sold_amount"),
                                                        rs.getDouble("left_amount"),
                                                        rs.getDouble("exception_amount"));

                balanceLineItems.add(balanceLineItem);
            }

            ps.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return balanceLineItems;
    }

    public static ArrayList<Sale> getSalesByResIdAndDates(LocalDate startDate, LocalDate endDate)
    {
        ArrayList<Sale> sales = new ArrayList<>();

        try
        {
            String sql = "SELECT DISTINCT\n" +
                    "  restaurants.id AS shop_id,recipes.id,recipes.name AS recipe_name,\n" +
                    "(SELECT SUM(sales.sold_portions) FROM sales WHERE recipes.id = sales.recipe_id and sales.shop_id = restaurants.id\n" +
                    "                                                  AND sales.date BETWEEN ? AND ?) AS portions_sum\n" +
                    "FROM sales, recipes, restaurants\n" +
                    "WHERE sales.shop_id = restaurants.id AND sales.recipe_id = recipes.id";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Sale sale = new Sale(
                        rs.getInt("shop_id"),
                        rs.getInt("id"),
                        rs.getString("recipe_name"),
                        rs.getInt("portions_sum"));

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

    public static void saveBalanceLog(int restaurantId, int ingredientId, double amount, String operation)
    {
        String sql = "INSERT INTO `bindia`.`balance_logs` (`" +
                "id`, `shop_id`, `ingredient_id`, `amount`,  `operation`, `date`)" +
                "VALUES (NULL, ?, ?, ?, ?, ?)";

        try
        {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, restaurantId);
            ps.setInt(2, ingredientId);
            ps.setDouble(3, amount);
            ps.setString(4, operation);
            ps.setDate(5, Date.valueOf(LocalDate.now()));

            ps.execute();

            ps.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteBalanceLog(Order selectedOrder)
    {
        String sql = "DELETE FROM `balance_logs` WHERE `shop_id` = ? AND `ingredient_id` = ? AND `date` = ?, `operation` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, selectedOrder.getShopId());
            statement.setInt(2, selectedOrder.getIngredient().getId());
            statement.setDate(3, Date.valueOf(selectedOrder.getDate()));
            statement.setString(4, "ordered");

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveBalanceLogChanges(Order order)
    {
        String sql = "UPDATE `bindia`.`balance_logs` SET `amount` = ? WHERE `shop_id` = ? AND `ingredient_id` = ? AND `date` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDouble(1, order.getAmount()*order.getIngredient().getQuantity());
            statement.setInt(2, order.getShopId());
            statement.setInt(3, order.getIngredient().getId());
            statement.setDate(4, Date.valueOf(order.getDate()));

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteSale(int saleId)
    {
        String sql = "DELETE FROM `sales` WHERE `id` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, saleId);

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteBalanceLogs(Sale sale)
    {
        String sql = "DELETE FROM `balance_logs` WHERE `shop_id` = ? AND `date` = ? AND `operation` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, sale.getShopId());
            statement.setDate(2, Date.valueOf(sale.getDate()));
            statement.setString(3, "sold");

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveSaleChanges(Sale sale)
    {
        String sql = "UPDATE `bindia`.`sales` SET `sold_portions` = ? WHERE `id` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setInt(1, sale.getPortions());
            statement.setInt(2, sale.getId());

            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveBalanceLogSalesChanges(Sale sale, double recipeLineItemAmount,int ingredientId)
    {
        String sql = "UPDATE `bindia`.`balance_logs` SET `amount` = ? WHERE `shop_id` = ? " +
                     "AND `date` = ? AND `operation` = ? AND `ingredient_id` = ?";

        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDouble(1, sale.getPortions()*recipeLineItemAmount);
            statement.setInt(2, sale.getShopId());
            statement.setDate(3, Date.valueOf(sale.getDate()));
            statement.setString(4, "sold");
            statement.setInt(5, ingredientId);


            statement.executeUpdate();

            statement.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
