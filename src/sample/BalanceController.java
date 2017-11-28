package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        fromDatePicker.setValue(LocalDate.now().minusDays(7));
        toDatePicker.setValue(LocalDate.now());
        loadBalanceLineItemsForPeriod();

//        shopChoiceBox.valueProperty().addListener(new ChangeListener()
//        {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue)
//            {
//                loadBalanceLineItemsForPeriod();
//            }
//        });

        shopChoiceBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            loadBalanceLineItemsForPeriod();
        });

        fromDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            loadBalanceLineItemsForPeriod();
        });

        toDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            loadBalanceLineItemsForPeriod();
        });

    }

    private void loadBalanceLineItemsForPeriod()
    {
        Shop selectedShop = (Shop) shopChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        DBWrapper.getBalanceItems(selectedShop.getId(), fromDate, toDate);
    }


    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }
}
