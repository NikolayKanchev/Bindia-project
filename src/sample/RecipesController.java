package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.db.DBWrapper;
import sample.model.Ingredient;
import sample.model.Recipe;
import sample.model.RecipeIngredient;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipesController implements Initializable
{

    @FXML
    private TableView<Ingredient> ingTable;

    @FXML
    private TableView<Recipe> recipesTable;

    @FXML
    private TableView<RecipeIngredient> recipeIngTable;

    @FXML
    private TableColumn<Ingredient, Integer> idINGColumn;

    @FXML
    private TableColumn<Ingredient, String> nameINGColumn;

    @FXML
    private TableColumn<RecipeIngredient, Integer> idRecipeIngColumn;

    @FXML
    private TableColumn<RecipeIngredient, String> ingRecipeIngColumn;

    @FXML
    private TableColumn<RecipeIngredient, Double> amountRecipeIngColumn;

    @FXML
    private TableColumn<Recipe, Integer> idRecipesColumn;

    @FXML
    private TableColumn<Recipe, String> nameRecipesColumn;

    @FXML
    private TextField dishNameField;

    @FXML
    private Label redLabel, recipeLabel;

    @FXML
    private Button saveBtn, createBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        createBtn.setVisible(false);
        loadIngredients();
        loadRecipes();
    }

    public void saveRecipe(ActionEvent actionEvent)
    {

    }

    public void deleteRecipe(ActionEvent actionEvent)
    {

    }

    public void editRecipe(ActionEvent actionEvent)
    {
        Recipe selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();

        if(selectedRecipe == null)
        {
            redLabel.setText("Select Recipe first !!!");

            redLabel.setVisible(true);

            return;
        }

        recipeLabel.setText("Edit Recipe");
        saveBtn.setText("Save Changes");

        loadRecipeIngredients(selectedRecipe.getId());

        dishNameField.setText(selectedRecipe.getName());
        createBtn.setVisible(true);
    }

    private void loadIngredients()
    {
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        idINGColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameINGColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        ingredients.setAll(DBWrapper.getAllIngredients());

        ingTable.setItems(ingredients);

    }

    private void loadRecipes()
    {
        ObservableList<Recipe> recipes = FXCollections.observableArrayList();

        idRecipesColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameRecipesColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        recipes.setAll(DBWrapper.getAllRecipes());

        recipesTable.setItems(recipes);
    }

    private void loadRecipeIngredients(int recipeId)
    {
        ObservableList<RecipeIngredient> ingredients = FXCollections.observableArrayList();

        idRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("ingName"));
        amountRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        ingredients.setAll(DBWrapper.getRecipeIngredients(recipeId));

        recipeIngTable.setItems(ingredients);

    }

    public void createRecipe(ActionEvent actionEvent)
    {
        dishNameField.setText("");
        createBtn.setVisible(false);
        recipeLabel.setText("Create Recipe");
        saveBtn.setText("Save");
        recipeIngTable.setItems(null);
    }

    public void addIngredient(MouseEvent mouseEvent)
    {
        ingTable.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {

                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2)
                {
                    Ingredient ingredient =  ingTable.getSelectionModel().getSelectedItem();

//                    DBWrapper.createEmptyRecipe()
                }
            }
        });
    }

    public void removeIngredient(MouseEvent mouseEvent)
    {

    }
}
