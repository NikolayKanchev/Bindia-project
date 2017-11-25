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
import sample.model.Order;
import sample.model.Shop;

import java.net.URL;
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
    private ChoiceBox shopCheckBox;

    @FXML
    private TextField ingredientField, amountField;

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
    }

    private void loadOrdersForSelectedShop()
    {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        orders.setAll(DBWrapper.getAllOrdersByShopID(selectedShopId));

        table.setItems(orders);
    }


    public void createOrder(ActionEvent actionEvent)
    {
        if(ingredientField.getText().isEmpty() || amountField.getText().isEmpty())
        {
            redLabel.setText("Fill out all the fields !!!");
            redLabel.setVisible(true);

            return;
        }

        DBWrapper.saveOrder(ingredientField.getText(),
                            amountField.getText(),
                            selectedShopId);

        loadOrdersForSelectedShop();

        ingredientField.setText("");
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

        order.setIngredient(orderStringCellEditEvent.getNewValue());

        DBWrapper.saveOrderChanges(order);
    }

    public void saveAmountChanges(TableColumn.CellEditEvent<Order, Double> orderDoubleCellEditEvent)
    {
        Order order = table.getSelectionModel().getSelectedItem();

        order.setAmount(orderDoubleCellEditEvent.getNewValue());

        DBWrapper.saveOrderChanges(order);
    }
}
