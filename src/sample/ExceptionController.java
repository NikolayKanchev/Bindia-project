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
import sample.model.OrderException;
import sample.model.Shop;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ExceptionController implements Initializable
{
    @FXML
    private TableView<OrderException> table;

    @FXML
    private TableColumn<OrderException, Integer> idColumn;

    @FXML
    private TableColumn<OrderException, Double> missingColumn;

    @FXML
    private TableColumn<OrderException, String> ingredientColumn;

    @FXML
    private TableColumn<OrderException, LocalDate> dateColumn;

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
        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        ObservableList<OrderException> exceptions = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        missingColumn.setCellValueFactory(new PropertyValueFactory<>("missing"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        exceptions.setAll(DBWrapper.getAllExceptionsByShopID(shop.getId()));

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
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }

    public void createException(ActionEvent actionEvent)
    {
        Ingredient selectedIngredient = (Ingredient) ingredientsChoiceBox.getSelectionModel().getSelectedItem();

        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        if(selectedIngredient == null || missingField.getText().isEmpty())
        {
            redLabel.setText("Choose ingredient and enter missing value !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.saveException(selectedIngredient.getId(), Double.parseDouble(missingField.getText()), shop.getId());

        missingField.setText("");

        ingredientsChoiceBox.setValue(null);

        loadExceptionsForSelectedShop();
    }

    public void deleteException(ActionEvent actionEvent)
    {
        OrderException exception = table.getSelectionModel().getSelectedItem();

        if(exception == null)
        {
            redLabel.setText("Select Item First !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteOrderException(exception.getId());

        loadExceptionsForSelectedShop();

    }

    public void saveChanges(TableColumn.CellEditEvent<OrderException, Double> orderDoubleCellEditEvent)
    {
        OrderException exception = table.getSelectionModel().getSelectedItem();

        exception.setMissing(orderDoubleCellEditEvent.getNewValue());

        DBWrapper.saveOrderExceptionChanges(exception);
    }
}
