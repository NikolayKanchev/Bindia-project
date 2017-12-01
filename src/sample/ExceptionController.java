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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import sample.db.DBWrapper;
import sample.model.Ingredient;
import sample.model.Exception;
import sample.model.Restaurant;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ExceptionController implements Initializable
{
    @FXML
    private TableView<Exception> table;

    @FXML
    private TableColumn<Exception, Integer> idColumn;

    @FXML
    private TableColumn<Exception, Double> missingColumn;

    @FXML
    private TableColumn<Exception, String> ingredientColumn;

    @FXML
    private TableColumn<Exception, LocalDate> dateColumn;

    @FXML
    private ChoiceBox shopChoiceBox, ingredientsChoiceBox;

    @FXML
    private Label redLabel;

    @FXML
    private TextField missingField;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        loadIngredients();
        loadExceptionsForSelectedShop();

        shopChoiceBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                loadExceptionsForSelectedShop();
            }
        });

        table.setEditable(true);

        missingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    }

    private void loadExceptionsForSelectedShop()
    {
        Restaurant restaurant = (Restaurant)shopChoiceBox.getSelectionModel().getSelectedItem();

        ObservableList<Exception> exceptions = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        missingColumn.setCellValueFactory(new PropertyValueFactory<>("missing"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        exceptions.setAll(DBWrapper.getAllExceptionsByRestaurantID(restaurant.getId()));

        table.setItems(exceptions);
    }

    private void loadIngredients()
    {
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        ingredients.setAll(DBWrapper.getAllIngredients());

        ingredientsChoiceBox.setItems(ingredients);
    }

    private void loadShops()
    {
        ObservableList<Restaurant> restaurants = FXCollections.observableArrayList(DBWrapper.getAllRestaurants());

        shopChoiceBox.setItems(restaurants);

        shopChoiceBox.setValue(restaurants.get(0));
    }

    public void createException(ActionEvent actionEvent)
    {
        Ingredient selectedIngredient = (Ingredient) ingredientsChoiceBox.getSelectionModel().getSelectedItem();

        Restaurant restaurant = (Restaurant)shopChoiceBox.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null || missingField.getText().isEmpty())
        {
            redLabel.setText("Choose ingredient and enter missing value !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.saveException(selectedIngredient.getId(), Double.parseDouble(missingField.getText()), restaurant.getId());

        loadExceptionsForSelectedShop();

        missingField.setText("");

        ingredientsChoiceBox.setValue(null);
    }

    public void deleteException(ActionEvent actionEvent)
    {
        Exception exception = table.getSelectionModel().getSelectedItem();

        if(exception == null)
        {
            redLabel.setText("Select Item First !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteException(exception.getId());

        loadExceptionsForSelectedShop();

    }

    public void saveChanges(TableColumn.CellEditEvent<Exception, Double> orderDoubleCellEditEvent)
    {
        Exception exception = table.getSelectionModel().getSelectedItem();

        exception.setMissing(orderDoubleCellEditEvent.getNewValue());

        DBWrapper.saveExceptionChanges(exception);
    }
}
