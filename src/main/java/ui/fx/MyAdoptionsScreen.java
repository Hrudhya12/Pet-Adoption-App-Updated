package ui.fx;

import dao.PetDAO;
import model.Pet;
import model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class MyAdoptionsScreen {

    public static void show(Stage stage, User user) {

        PetDAO petDAO = new PetDAO();
        List<Pet> adoptedPets = petDAO.getAdoptedPets(user.getUsername());

        TableView<Pet> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(adoptedPets));

        TableColumn<Pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pet, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Pet, String> breedCol = new TableColumn<>("Breed");
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));

        TableColumn<Pet, String> dateCol = new TableColumn<>("Adoption Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("adoptionDate"));

        table.getColumns().addAll(nameCol, typeCol, breedCol, dateCol);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> UserDashboard.show(stage, user));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(backBtn);
        BorderPane.setMargin(backBtn, new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
