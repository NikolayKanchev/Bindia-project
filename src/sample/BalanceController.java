package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBWrapper;
import sample.model.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BalanceController implements Initializable
{
    @FXML
    private TableView<BalanceLineItem> table;

    @FXML
    private TableColumn<BalanceLineItem, Integer> idColumn;

    @FXML
    private TableColumn<BalanceLineItem, String> ingredientColumn;

    @FXML
    private TableColumn<BalanceLineItem, Double> orderedColumn, soldColumn, leftColumn, exceptionColumn;

    @FXML
    private ChoiceBox shopChoiceBox;

    @FXML
    private DatePicker fromDatePicker, toDatePicker;

    @FXML
    private Label redLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        fromDatePicker.setValue(LocalDate.now().minusDays(7));
        toDatePicker.setValue(LocalDate.now());
        loadBalanceLineItemsForPeriod();

        shopChoiceBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(fromDatePicker.getValue() == null || toDatePicker.getValue() == null)
            {
                return;
            }

            loadBalanceLineItemsForPeriod();
        });

        fromDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(toDatePicker.getValue() == null)
            {
                redLabel.setText("Choose to which date !!!");

                redLabel.setVisible(true);

                return;
            }

            redLabel.setVisible(false);

            loadBalanceLineItemsForPeriod();
        });

        toDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(fromDatePicker.getValue() == null)
            {
                redLabel.setText("Choose from which date !!!");

                redLabel.setVisible(true);

                return;
            }

            redLabel.setVisible(false);

            loadBalanceLineItemsForPeriod();
        });

    }

    private void loadBalanceLineItemsForPeriod()
    {
        Shop selectedShop = (Shop) shopChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        ObservableList<BalanceLineItem> balanceLineItems = FXCollections.observableArrayList();

        balanceLineItems.setAll(getBalanceItems(selectedShop.getId(), fromDate, toDate));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        orderedColumn.setCellValueFactory(new PropertyValueFactory<>("orderedAmount"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        exceptionColumn.setCellValueFactory(new PropertyValueFactory<>("exceptionAmount"));
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("leftAmount"));

        table.setItems(balanceLineItems);
    }

    private ArrayList<BalanceLineItem> getBalanceItems(int shopId, LocalDate fromDate, LocalDate toDate)
    {
        ArrayList<BalanceLineItem> balanceLineItems = new ArrayList<>();

        ArrayList<Order> orders = DBWrapper.getAllOrdersByShopID(shopId);

        ArrayList<OrderException> exceptions = DBWrapper.getAllExceptionsByShopID(shopId);

        ArrayList<Ingredient> ingredients = DBWrapper.getAllIngredients();

        ArrayList<Recipe> recipes = DBWrapper.getAllRecipes();

        for (Ingredient ing: ingredients)
        {
            int ingID = ing.getId();

            double sumOrders = 0;

            double soldOfThisIngredient = 0;

            for (Recipe r: recipes)
            {
                int soldPortions = 0;

                double ingInPortion = 0;

                ArrayList<RecipeIngredient> recipeIngredients = DBWrapper.getRecipeIngredients(r.getId());

                for (RecipeIngredient ri: recipeIngredients)
                {
                    if (ri.getIngredient().getId() == ingID )
                    {
                        ingInPortion = ri.getAmount();
                    }
                }

                soldPortions = DBWrapper.getSumSales(r.getId(), shopId, fromDate, toDate);

                soldOfThisIngredient += soldPortions*ingInPortion;
            }

            double exception = 0;

            for (Order o: orders)
            {
                if (o.getIngredient().getId() == ingID)
                {
                    for (OrderException oe: exceptions)
                    {
                        if (oe.getOrderId() == o.getId())
                        {
                            exception = oe.getMissing();
                        }
                    }
                }

                if(o.getIngredient().getId() == ingID && o.getShopId() == shopId)
                {
                    if(o.getDate().isAfter(fromDate.minusDays(1)) && o.getDate().isBefore(toDate.plusDays(1)))
                    {
                        sumOrders += o.getAmount()*ing.getQuantity();
                    }
                }
            }

            double left = sumOrders - soldOfThisIngredient - exception;

            balanceLineItems.add(new BalanceLineItem(ingID, shopId, ing.getName(), sumOrders, soldOfThisIngredient, left, exception));

        }

        return balanceLineItems;
    }


    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }
}
