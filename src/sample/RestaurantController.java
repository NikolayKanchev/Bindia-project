package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import sample.db.DBWrapper;
import sample.model.Restaurant;

import java.net.URL;
import java.util.ResourceBundle;

public class RestaurantController implements Initializable
{

    @FXML
    private TableView<Restaurant> table;

    @FXML
    private TableColumn<Restaurant, Integer> idColumn;

    @FXML
    private TableColumn<Restaurant, String> nameColumn, managerColumn, addressColumn;

    @FXML
    private Label redLabel;

    @FXML
    private TextField nameField ,managerField ,addressField;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        nameField.setPromptText("name");
        managerField.setPromptText("manager");
        addressField.setPromptText("address");

        loadShops();

        table.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        managerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void loadShops()
    {
        ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        restaurants.setAll(DBWrapper.getAllRestaurants());

        table.setItems(restaurants);
    }

    public void deleteShop(ActionEvent actionEvent)
    {
        Restaurant selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            redLabel.setText("Select a Restaurant !!!");

            redLabel.setVisible(true);

            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteRestaurantById(selectedItem.getId());

        loadShops();
    }

    public void addNewShop(ActionEvent actionEvent)
    {
        if(nameField.getText().isEmpty() ||
           managerField.getText().isEmpty() ||
           addressField.getText().isEmpty())
        {
            redLabel.setText("Fill out all the fields !!!");
            return;
        }

        DBWrapper.addNewRestaurant(nameField.getText(), managerField.getText(), addressField.getText());

        loadShops();

        nameField.setText("");
        managerField.setText("");
        addressField.setText("");
    }

    public void saveNameChanges(TableColumn.CellEditEvent<Restaurant, String> shopStringCellEditEvent)
    {
        Restaurant restaurant = table.getSelectionModel().getSelectedItem();

        restaurant.setName(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveRestaurantChanges(restaurant);

        loadShops();
    }

    public void saveManagerChanges(TableColumn.CellEditEvent<Restaurant, String> shopStringCellEditEvent)
    {
        Restaurant restaurant = table.getSelectionModel().getSelectedItem();

        restaurant.setManager(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveRestaurantChanges(restaurant);

        loadShops();
    }

    public void saveAddressChanges(TableColumn.CellEditEvent<Restaurant, String> shopStringCellEditEvent)
    {
        Restaurant restaurant = table.getSelectionModel().getSelectedItem();

        restaurant.setAddress(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveRestaurantChanges(restaurant);

        loadShops();
    }
}
