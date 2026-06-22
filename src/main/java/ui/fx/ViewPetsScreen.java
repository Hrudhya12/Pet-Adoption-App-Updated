package ui.fx;

import dao.PetDAO;
import model.Pet;
import model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ViewPetsScreen {

    public static void show(Stage stage, User user) {

        PetDAO petDAO = new PetDAO();
        List<Pet> pets = petDAO.getAllPets()
                .stream()
                .filter(p -> !p.isAdopted())
                .toList();

        TableView<Pet> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(pets));

        TableColumn<Pet, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pet, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Pet, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));

        TableColumn<Pet, String> personalityCol = new TableColumn<>("Personality");
        personalityCol.setCellValueFactory(new PropertyValueFactory<>("personality"));

        table.getColumns().addAll(nameCol, typeCol, sizeCol, personalityCol);

        // ⭐ View Details button
        Button detailsBtn = new Button("View Details");
        detailsBtn.setOnAction(e -> {
            Pet selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showAlert("No pet selected", "Please select a pet to view details.");
                return;
            }

            showPetDetailsPopup(selected, user, petDAO, table);
        });

        // ⭐ Back button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> UserDashboard.show(stage, user));

        HBox buttons = new HBox(10, detailsBtn, backBtn);
        buttons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(buttons);

        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.show();
    }

    // ⭐ Popup window for pet details
    private static void showPetDetailsPopup(Pet pet, User user, PetDAO petDAO, TableView<Pet> table) {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Pet Details");

        Label name = new Label("Name: " + pet.getName());
        Label type = new Label("Type: " + pet.getType());
        Label breed = new Label("Breed: " + pet.getBreed());
        Label age = new Label("Age: " + pet.getAge());
        Label gender = new Label("Gender: " + pet.getGender());
        Label size = new Label("Size: " + pet.getSize());
        Label personality = new Label("Personality: " + pet.getPersonality());
        Label health = new Label("Health Status: " + pet.getHealthStatus());
        Label adopted = new Label("Adopted: " + (pet.isAdopted() ? "Yes" : "No"));

        VBox infoBox = new VBox(8, name, type, breed, age, gender, size, personality, health, adopted);
        infoBox.setPadding(new Insets(10));

        // ⭐ Adopt button inside popup
        Button adoptBtn = new Button("Proceed to Adopt");

        if (pet.isAdopted()) {
            adoptBtn.setDisable(true);
            adoptBtn.setText("Already Adopted");
        }

        adoptBtn.setOnAction(e -> {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Adoption");
            confirm.setHeaderText("Adopt " + pet.getName() + "?");
            confirm.setContentText("Are you sure you want to adopt " + pet.getName() + "?\n"
                    + "Our staff will contact you within 48 hours.");

            ButtonType yes = new ButtonType("Yes, adopt");
            ButtonType no = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirm.getButtonTypes().setAll(yes, no);

            confirm.showAndWait().ifPresent(response -> {
                if (response == yes) {

                    pet.adopt(user.getUsername());
                    petDAO.updatePet(pet);

                    showAlert("Adoption Successful",
                            "You have adopted " + pet.getName() + "!\n\n"
                                    + "Our staff will contact you within 48 hours.");

                    table.setItems(FXCollections.observableArrayList(petDAO.getAllPets()));
                    dialog.close();
                }
            });
        });

        dialog.getDialogPane().setContent(new VBox(10, infoBox, adoptBtn));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
