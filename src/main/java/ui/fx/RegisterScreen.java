package ui.fx;

import dao.UserDAO;
import model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen {

    public static void show(Stage stage) {

        Label title = new Label("Create Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        TextField typeField = new TextField();
        typeField.setPromptText("Preferred Pet Type (Dog/Cat/Rabbit/Bird)");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Preferred Size (Small/Medium/Large)");

        TextField personalityField = new TextField();
        personalityField.setPromptText("Preferred Personality (Calm/Friendly/Playful)");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back");

        registerBtn.setPrefWidth(200);
        backBtn.setPrefWidth(200);

        registerBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String type = typeField.getText().trim();
                String size = sizeField.getText().trim();
                String personality = personalityField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    errorLabel.setText("Please fill all required fields.");
                    return;
                }

                UserDAO userDAO = new UserDAO();

                if (userDAO.emailExists(email)) {
                    errorLabel.setText("Email already exists. Please login.");
                    return;
                }

                User newUser = new User(name, email, password, age, type, size, personality);

                if (userDAO.registerUser(newUser)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Account created successfully! Please login.");
                    alert.showAndWait();

                    LoginScreen.show(stage);
                } else {
                    errorLabel.setText("Registration failed. Try again.");
                }

            } catch (Exception ex) {
                errorLabel.setText("Invalid input. Check your details.");
            }
        });

        backBtn.setOnAction(e -> HomeScreen.show(stage));

        VBox layout = new VBox(12, title, nameField, emailField, passwordField,
                ageField, typeField, sizeField, personalityField,
                registerBtn, backBtn, errorLabel);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 450, 500);
        stage.setScene(scene);
        stage.show();
    }
}
