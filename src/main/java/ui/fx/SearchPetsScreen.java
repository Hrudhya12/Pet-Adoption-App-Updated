package ui.fx;

import dao.PetDAO;
import model.Pet;
import model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class SearchPetsScreen {

    public static void show(Stage stage, User user) {

        PetDAO petDAO = new PetDAO();

        TextField typeField = new TextField();
        typeField.setPromptText("Type (Dog, Cat...)");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Size (Small, Medium, Large)");

        TextField personalityField = new TextField();
        personalityField.setPromptText("Personality (Calm, Active...)");

        Button searchBtn = new Button("Search");
        Button backBtn = new Button("Back");

        TableView<Pet> table = new TableView<>();

        TableColumn<Pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pet, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Pet, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<Pet, String> personalityCol = new TableColumn<>("Personality");
        personalityCol.setCellValueFactory(new PropertyValueFactory<>("personality"));

        table.getColumns().addAll(nameCol, typeCol, sizeCol, personalityCol);

        searchBtn.setOnAction(e -> {
            String type = typeField.getText().trim();
            String size = sizeField.getText().trim();
            String personality = personalityField.getText().trim();

            List<Pet> pets = petDAO.searchPets(type, size, personality)
                    .stream()
                    .filter(p -> !p.isAdopted())
                    .toList();

            table.setItems(FXCollections.observableArrayList(pets));
        });

        backBtn.setOnAction(e -> UserDashboard.show(stage, user));

        HBox filters = new HBox(10, typeField, sizeField, personalityField, searchBtn, backBtn);
        filters.setAlignment(Pos.CENTER);
        filters.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(filters);
        root.setCenter(table);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.show();
    }
}
