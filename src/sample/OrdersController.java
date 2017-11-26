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
import sample.model.Order;
import sample.model.Shop;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class OrdersController implements Initializable
{
    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> idColumn;

    @FXML
    private TableColumn<Order, String> ingredientColumn;

    @FXML
    private TableColumn<Order, Double> amountColumn;

    @FXML
    private TableColumn<Order, LocalDate> dateColumn;

    @FXML
    private ChoiceBox shopCheckBox, ingredientChoiceBox;

    @FXML
    private TextField amountField;

    @FXML
    private Label redLabel;

    private int selectedShopId;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());
        shopCheckBox.setItems(shops);
        shopCheckBox.setValue(shops.get(0));
        selectedShopId = shops.get(0).getId();

        loadOrdersForSelectedShop();

        ingredientColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        table.setEditable(true);

        shopCheckBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                String selectedItem = shopCheckBox.getSelectionModel().getSelectedItem().toString();

                String [] str = selectedItem.split("\t");

                selectedShopId = Integer.parseInt(str[0]);

                loadOrdersForSelectedShop();
            }
        });

        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        ingredients.setAll(DBWrapper.getAllIngredients());

        ingredientChoiceBox.setItems(ingredients);

        ingredientChoiceBox.setValue(ingredients.get(0));
    }

    private void loadOrdersForSelectedShop()
    {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        orders.setAll(DBWrapper.getAllOrdersByShopID(selectedShopId));

        table.setItems(orders);
    }


    public void createOrder(ActionEvent actionEvent)
    {
        if(amountField.getText().isEmpty())
        {
            redLabel.setText("Enter amount !!!");
            redLabel.setVisible(true);

            return;
        }

        Ingredient selectedIngredient = (Ingredient) ingredientChoiceBox.getSelectionModel().getSelectedItem();

        DBWrapper.saveOrder(selectedIngredient,
                            amountField.getText(),
                            selectedShopId);

        loadOrdersForSelectedShop();

        amountField.setText("");
    }

    public void deleteOrder(ActionEvent actionEvent)
    {
        Order selectedOrder = table.getSelectionModel().getSelectedItem();

        if(selectedOrder == null)
        {
            redLabel.setText("Select an order first");
            redLabel.setVisible(true);

            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteOrderById(selectedOrder.getId());

        loadOrdersForSelectedShop();
    }

    public void saveIngChanges(TableColumn.CellEditEvent<Order, String> orderStringCellEditEvent)
    {
        Order order = table.getSelectionModel().getSelectedItem();

        order.setIngredientName(orderStringCellEditEvent.getNewValue());

        DBWrapper.saveOrderChanges(order);
    }

    public void saveAmountChanges(TableColumn.CellEditEvent<Order, Double> orderDoubleCellEditEvent)
    {
        Order order = table.getSelectionModel().getSelectedItem();

        order.setAmount(orderDoubleCellEditEvent.getNewValue());

        DBWrapper.saveOrderChanges(order);
    }

}
