package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class SalesController implements Initializable
{

    @FXML
    private ChoiceBox shopCheckBox, recipesCheckBox;

    @FXML
    private TextField soldPortionsField;

    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<Sale, Integer> idColumn, portionsColumn;

    @FXML
    private TableColumn<Sale, LocalDate> dateColumn;

    @FXML
    private TableColumn<Sale, String> recipeColumn;

    @FXML
    private Label redLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        loadRecipes();
        loadSalesForSelectedShop();

        shopCheckBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                loadSalesForSelectedShop();
            }
        });
    }

    private void loadSalesForSelectedShop()
    {
        Restaurant selectedRestaurant = (Restaurant) shopCheckBox.getSelectionModel().getSelectedItem();

        ObservableList<Sale> sales = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        recipeColumn.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
        portionsColumn.setCellValueFactory(new PropertyValueFactory<>("portions"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        sales.setAll(DBWrapper.getAllSalesByResID(selectedRestaurant.getId()));

        table.setItems(sales);
    }

    private void loadRecipes()
    {
        ObservableList<Recipe> recipes = FXCollections.observableArrayList(DBWrapper.getAllRecipes());
        recipesCheckBox.setItems(recipes);
        recipesCheckBox.setValue(recipes.get(0));
    }

    private void loadShops()
    {
        ObservableList<Restaurant> restaurants = FXCollections.observableArrayList(DBWrapper.getAllRestaurants());
        shopCheckBox.setItems(restaurants);
        shopCheckBox.setValue(restaurants.get(0));
    }

    public void saveSale(ActionEvent actionEvent)
    {
        if (soldPortionsField.getText().isEmpty())
        {
            redLabel.setText("Enter number of sold portions !!!");

            redLabel.setVisible(true);

            return;
        }

        redLabel.setVisible(false);

        Restaurant restaurant = (Restaurant)shopCheckBox.getSelectionModel().getSelectedItem();
        Recipe recipe = (Recipe) recipesCheckBox.getSelectionModel().getSelectedItem();
        int soldPortions = Integer.parseInt(soldPortionsField.getText());

        DBWrapper.saveSale(restaurant.getId(), recipe.getId(), soldPortions);

        new Thread(() -> {

            ArrayList<RecipeLineItem> ingredients = DBWrapper.getRecipeIngredients(recipe.getId());

            for (RecipeLineItem ingredient: ingredients)
            {
                DBWrapper.saveBalanceLog(restaurant.getId(), ingredient.getIngredient().getId(), soldPortions*ingredient.getAmount(), "sold");
            }

        }){{start();}};

        soldPortionsField.setText("");

        loadSalesForSelectedShop();
    }
}
