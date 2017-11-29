package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBWrapper;
import sample.model.BalanceLineItem;
import sample.model.Shop;

import java.net.URL;
import java.time.LocalDate;
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

        balanceLineItems.setAll(DBWrapper.getBalanceItems(selectedShop.getId(), fromDate, toDate));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        orderedColumn.setCellValueFactory(new PropertyValueFactory<>("orderedAmount"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("soldAmount"));
        exceptionColumn.setCellValueFactory(new PropertyValueFactory<>("exceptionAmount"));
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("leftAmount"));

        table.setItems(balanceLineItems);
    }


    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }
}
