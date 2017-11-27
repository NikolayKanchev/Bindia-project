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
import sample.model.Order;
import sample.model.OrderException;
import sample.model.Shop;

import java.net.URL;
import java.util.ResourceBundle;

public class ExceptionController implements Initializable
{
    @FXML
    private TableView<OrderException> table;

    @FXML
    private TableColumn<OrderException, Integer> idColumn, orderIDColumn;

    @FXML
    private TableColumn<OrderException, Double> missingColumn;

    @FXML
    private TableColumn<OrderException, String> ingredientColumn;

    @FXML
    private ChoiceBox shopChoiceBox, ordersChoiceBox;

    @FXML
    private Label redLabel;

    @FXML
    private TextField missingField;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        loadOrdersForSelectedShop();
        loadExceptionsForSelectedShop();

        shopChoiceBox.valueProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                loadOrdersForSelectedShop();
                loadExceptionsForSelectedShop();
            }
        });
    }

    private void loadExceptionsForSelectedShop()
    {
        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        ObservableList<OrderException> exceptions = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        missingColumn.setCellValueFactory(new PropertyValueFactory<>("missing"));

        exceptions.setAll(DBWrapper.getAllExceptionsByShopID(shop.getId()));

        table.setItems(exceptions);
    }

    private void loadOrdersForSelectedShop()
    {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        orders.setAll(DBWrapper.getAllOrdersByShopID(shop.getId()));

        ordersChoiceBox.setItems(orders);
    }

    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }

    public void createException(ActionEvent actionEvent)
    {
        Order selectedOrder = (Order)ordersChoiceBox.getSelectionModel().getSelectedItem();

        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        if(selectedOrder == null || missingField.getText().isEmpty())
        {
            redLabel.setText("Choose order and enter missing value !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.saveOrderException(selectedOrder.getId(), Double.parseDouble(missingField.getText()), shop.getId());

        missingField.setText("");

        ordersChoiceBox.setValue(null);

//        loadExceptionsForSelectedShop();
    }

    public void deleteException(ActionEvent actionEvent)
    {

    }

    public void saveChanges(TableColumn.CellEditEvent cellEditEvent)
    {

    }
}
