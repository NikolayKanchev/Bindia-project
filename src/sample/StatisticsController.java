package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import sample.db.DBWrapper;
import sample.model.Sale;
import sample.model.Shop;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable
{

    @FXML
    private BarChart<?, ?> chart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Label redLabel;

    @FXML
    private ChoiceBox<Shop> shopChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
        shopChoiceBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(startDatePicker.getValue() == null || endDatePicker.getValue() == null)
            {
                return;
            }

            loadSales();
        });

        startDatePicker.setValue(LocalDate.now().minusWeeks(1));
        endDatePicker.setValue(LocalDate.now());
        loadSales();

        startDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(endDatePicker.getValue().isBefore(startDatePicker.getValue()))
            {
                redLabel.setText("The end date can't be before the start date !!!");

                redLabel.setVisible(true);

                return;
            }

            redLabel.setVisible(false);

            loadSales();
        });

        endDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(startDatePicker.getValue().isAfter(endDatePicker.getValue()))
            {
                redLabel.setText("The start date can't be after the end date !!!");

                redLabel.setVisible(true);

                return;
            }

            redLabel.setVisible(false);

            loadSales();
        });
    }

    private void loadSales()
    {
        chart.getData().clear();

        Shop shop = (Shop)shopChoiceBox.getSelectionModel().getSelectedItem();

        XYChart.Series set1 = new XYChart.Series<>();

        ArrayList<Sale> sales = DBWrapper.getSalesByShopIdAndDates(startDatePicker.getValue(), endDatePicker.getValue());

        for (Sale sale : sales)
        {
            if(shop.getId() == sale.getShopId())
            {
                set1.getData().add(new XYChart.Data(sale.getRecipeName(), sale.getPortions()));
            }
        }

        chart.getData().addAll(set1);
    }

    private void loadShops()
    {
        ObservableList<Shop> shops = FXCollections.observableArrayList(DBWrapper.getAllShops());

        shopChoiceBox.setItems(shops);

        shopChoiceBox.setValue(shops.get(0));
    }

}
