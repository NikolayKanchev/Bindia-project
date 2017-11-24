package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable
{

    @FXML
    private ChoiceBox exitOptions;

    @FXML
    private AnchorPane anchor;

    @FXML
    public void manageOrders(ActionEvent actionEvent)
    {
        changeAnchor("../sample/view/ordersScreen.fxml");
    }

    public void manageSales(ActionEvent actionEvent)
    {
        changeAnchor("../sample/view/salesScreen.fxml");
    }

    public void manageRecipes(ActionEvent actionEvent)
    {
        changeAnchor("../sample/view/recipesScreen.fxml");
    }

    public void manageShops(ActionEvent actionEvent)
    {
        changeAnchor("../sample/view/shopsScreen.fxml");
    }

    public void seeStatistics(ActionEvent actionEvent)
    {
        changeAnchor("../sample/view/statisticsScreen.fxml");
    }

    private void changeAnchor(String path)
    {
        try{
            AnchorPane tempAnchor = FXMLLoader.load(getClass().getResource(path));

            anchor.getChildren().setAll(tempAnchor);

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        exitOptions.setItems(FXCollections.observableArrayList("Exit"));
    }


    public void exit(MouseEvent mouseEvent)
    {
        System.exit(0);
    }
}
