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
import sample.model.Shop;

import java.net.URL;
import java.util.ResourceBundle;

public class ShopsController implements Initializable
{

    @FXML
    private TableView<Shop> table;

    @FXML
    private TableColumn<Shop, Integer> idColumn;

    @FXML
    private TableColumn<Shop, String> nameColumn, managerColumn, addressColumn;

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
        ObservableList<Shop> shops = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        managerColumn.setCellValueFactory(new PropertyValueFactory<>("manager"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        shops.setAll(DBWrapper.getAllShops());

        table.setItems(shops);
    }

    public void deleteShop(ActionEvent actionEvent)
    {
        Shop selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            redLabel.setText("Select a Shop !!!");

            redLabel.setVisible(true);

            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteShopById(selectedItem.getId());

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

        DBWrapper.addNewShop(nameField.getText(), managerField.getText(), addressField.getText());

        loadShops();

        nameField.setText("");
        managerField.setText("");
        addressField.setText("");
    }

    public void saveNameChanges(TableColumn.CellEditEvent<Shop, String> shopStringCellEditEvent)
    {
        Shop shop = table.getSelectionModel().getSelectedItem();

        shop.setName(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveShopChanges(shop);

        loadShops();
    }

    public void saveManagerChanges(TableColumn.CellEditEvent<Shop, String> shopStringCellEditEvent)
    {
        Shop shop = table.getSelectionModel().getSelectedItem();

        shop.setManager(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveShopChanges(shop);

        loadShops();
    }

    public void saveAddressChanges(TableColumn.CellEditEvent<Shop, String> shopStringCellEditEvent)
    {
        Shop shop = table.getSelectionModel().getSelectedItem();

        shop.setAddress(shopStringCellEditEvent.getNewValue());

        DBWrapper.saveShopChanges(shop);

        loadShops();
    }
}
