package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import sample.db.DBWrapper;
import sample.model.Recipe;
import sample.model.Shop;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class SalesController implements Initializable
{

    @FXML
    private ChoiceBox shopCheckBox, weekCheckBox, recipesCheckBox;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        loadWeeksNumbers();
        loadRecipes();
    }

    private void loadRecipes()
    {
        ObservableList<Recipe> recipes = FXCollections.observableArrayList(DBWrapper.getAllRecipes());
        recipesCheckBox.setItems(recipes);
        recipesCheckBox.setValue(recipes.get(0));
    }

    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());
        shopCheckBox.setItems(shops);
        shopCheckBox.setValue(shops.get(0));
    }

    private void loadWeeksNumbers()
    {
        ObservableList<Integer> weeksNumbers = FXCollections.observableArrayList();

        for (int i = 1; i<=52; i++)
        {
            weeksNumbers.add(i);
        }

        weekCheckBox.setItems(weeksNumbers);

        Calendar now = Calendar.getInstance();
        int weekNum = now.get(Calendar.WEEK_OF_YEAR);

        weekCheckBox.setValue(weeksNumbers.get(weekNum));
    }

    public void saveSale(ActionEvent actionEvent)
    {

    }
}
