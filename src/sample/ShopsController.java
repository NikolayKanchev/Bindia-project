package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DBWrapper;
import sample.model.Shop;

import java.net.URL;
import java.util.ResourceBundle;

public class ShopsController implements Initializable
{

    @FXML
    private TableView<Shop> table;

    @FXML
    private TableColumn<Integer, Shop> idColumn;

    @FXML
    private TableColumn<String, Shop> nameColumn, managerColumn, addressColumn;

    @FXML
    private Label redLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadShops();
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

    public void saveShop(ActionEvent actionEvent)
    {
        Shop selectedItem = table.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            redLabel.setText("Select a Shop !!!");

            redLabel.setVisible(true);

            return;
        }

        redLabel.setVisible(false);

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

    }
}
