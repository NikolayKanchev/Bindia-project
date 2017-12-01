package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;
import sample.db.DBWrapper;
import sample.model.Ingredient;
import sample.model.Recipe;
import sample.model.RecipeLineItem;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipesController implements Initializable
{

    @FXML
    private TableView<Ingredient> ingTable;

    @FXML
    private TableView<Recipe> recipesTable;

    @FXML
    private TableView<RecipeLineItem> recipeIngTable;

    @FXML
    private TableColumn<Ingredient, Integer> idINGColumn;

    @FXML
    private TableColumn<Ingredient, String> nameINGColumn;

    @FXML
    private TableColumn<RecipeLineItem, Integer> idRecipeIngColumn;

    @FXML
    private TableColumn<RecipeLineItem, String> ingRecipeIngColumn;

    @FXML
    private TableColumn<RecipeLineItem, Double> amountRecipeIngColumn;

    @FXML
    private TableColumn<Recipe, Integer> idRecipesColumn;

    @FXML
    private TableColumn<Recipe, String> nameRecipesColumn;

    @FXML
    private TextField recipeNameField, newAmountField;

    @FXML
    private Label redLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadIngredients();
        loadRecipes();

        recipeIngTable.setEditable(true);
        recipesTable.setEditable(true);

        nameRecipesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        amountRecipeIngColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    }

    public void deleteRecipe(ActionEvent actionEvent)
    {
        Recipe recipe = recipesTable.getSelectionModel().getSelectedItem();

        if(recipe == null)
        {
            redLabel.setText("Choose recipe first !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        DBWrapper.deleteRecipe(recipe.getId());

        loadRecipes();

        recipeIngTable.setItems(null);
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
        ObservableList<RecipeLineItem> ingredients = FXCollections.observableArrayList();

        idRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ingRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("ingName"));
        amountRecipeIngColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        ingredients.setAll(DBWrapper.getRecipeIngredients(recipeId));

        recipeIngTable.setItems(ingredients);

    }

    public void createRecipe(ActionEvent actionEvent)
    {
        recipeIngTable.setItems(null);

        DBWrapper.createRecipe(recipeNameField.getText());

        loadRecipes();

        recipeNameField.setText("");
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
                    Recipe selectedRecipe = recipesTable.getSelectionModel().getSelectedItem();

                    if(selectedRecipe == null)
                    {
                        redLabel.setText("Select a Recipe First !!!");
                        redLabel.setVisible(true);

                        return;
                    }

                    redLabel.setVisible(false);

                    Ingredient ingredient =  ingTable.getSelectionModel().getSelectedItem();

                    if(ingredient == null)
                    {
                        return;
                    }

                    DBWrapper.addIngredientToRecipe(selectedRecipe.getId(), ingredient.getId());

                    loadRecipeIngredients(selectedRecipe.getId());
                }
            }
        });
    }

    public void removeIngredient(MouseEvent mouseEvent)
    {
        recipeIngTable.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {

                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2)
                {
                    RecipeLineItem selectedIng = recipeIngTable.getSelectionModel().getSelectedItem();

                    if(selectedIng == null)
                    {
                        redLabel.setText("Select a Ingredient First !!!");
                        redLabel.setVisible(true);

                        return;
                    }

                    redLabel.setVisible(false);

                    DBWrapper.deleteRecipeIngredient(selectedIng.getId());

                    loadRecipeIngredients(recipesTable.getSelectionModel().getSelectedItem().getId());
                }
            }
        });
    }

    public void editRecipe(MouseEvent mouseEvent)
    {

        recipesTable.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {

                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 1)
                {
                    Recipe recipe =  recipesTable.getSelectionModel().getSelectedItem();

                    loadRecipeIngredients(recipe.getId());
                }
            }
        });


    }

    public void saveRecipeNameChanges(TableColumn.CellEditEvent<Recipe, String> recipeStringCellEditEvent)
    {
        Recipe recipe = recipesTable.getSelectionModel().getSelectedItem();

        recipe.setName(recipeStringCellEditEvent.getNewValue());

        DBWrapper.saveRecipe(recipe);
    }

    public void updateAmount(ActionEvent actionEvent)
    {
        RecipeLineItem recipeLineItem = recipeIngTable.getSelectionModel().getSelectedItem();

        if(recipeLineItem == null)
        {
            redLabel.setText("Select Recipe Ingredient !!!");
            redLabel.setVisible(true);
            return;
        }

        if(newAmountField.getText().isEmpty())
        {
            redLabel.setText("Enter the new amount !!!");
            redLabel.setVisible(true);
            return;
        }

        redLabel.setVisible(false);

        recipeLineItem.setAmount(Double.parseDouble(newAmountField.getText()));

        DBWrapper.saveRecipeIngredient(recipeLineItem);

        newAmountField.setText("");

        loadRecipeIngredients(recipesTable.getSelectionModel().getSelectedItem().getId());

    }
}
