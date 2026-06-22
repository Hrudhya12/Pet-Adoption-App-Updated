package ui.fx;

import dao.PetDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Pet;
import model.User;
import javafx.scene.control.ButtonType;
import javafx.animation.PauseTransition;
import javafx.util.Duration;


import java.util.*;

public class GameScreen {

    private static User loggedInUser;

    private final Stage stage;
    private final PetDAO petDAO = new PetDAO();

    private List<Pet> interviewPets = new ArrayList<>();
    private Map<Pet, Integer> scores = new HashMap<>();

    private int currentQuestion = 1;

    private Label titleLabel;
    private Label introLabel;
    private Label subIntroLabel;
    private Label questionLabel;
    private Button option1;
    private Button option2;
    private Button option3;
    private Button option4;
    private Button backButton;

    private String selectedType;

    public GameScreen(Stage stage, User user, String type) {
        this.stage = stage;
        loggedInUser = user;
        this.selectedType = type;
    }

    public static void show(Stage stage, User user, String type) {
        GameScreen screen = new GameScreen(stage, user, type);
        screen.init();
    }

    public static void show(Stage stage, User user) {
        GameScreen screen = new GameScreen(stage, user, null);
        screen.init();
    }


    private static String askTypePopup(Stage parent) {
        Stage popup = new Stage();
        popup.setTitle("Choose Pet Type");

        Label label = new Label("Choose a pet type for the interview:");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button dog = new Button("Dog");
        Button cat = new Button("Cat");
        Button rabbit = new Button("Rabbit");
        Button bird = new Button("Bird");

        final String[] result = {null};

        dog.setOnAction(e -> { result[0] = "Dog"; popup.close(); });
        cat.setOnAction(e -> { result[0] = "Cat"; popup.close(); });
        rabbit.setOnAction(e -> { result[0] = "Rabbit"; popup.close(); });
        bird.setOnAction(e -> { result[0] = "Bird"; popup.close(); });

        VBox root = new VBox(15, label, dog, cat, rabbit, bird);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        popup.setScene(new Scene(root, 300, 250));
        popup.showAndWait();

        return result[0];
    }

    private void init() {

        // ✔ Always ask the user which pet type they want to interview
        String chosenType = askTypePopup(stage);
        if (chosenType == null) {
            HomeScreen.show(stage);
            return;
        }

        this.selectedType = chosenType;

        // ✔ Load pets of that type
        List<Pet> allOfType = petDAO.getPetsByType(chosenType);
        interviewPets.clear();
        for (Pet p : allOfType) {
            if (!p.isAdopted()) {
                interviewPets.add(p);
            }
        }

        // ✔ If no pets available
        if (interviewPets.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No pets available");
            alert.setHeaderText(null);
            alert.setContentText("No " + chosenType + "s available for interviews right now.");
            alert.showAndWait();
            HomeScreen.show(stage);
            return;
        }

        // ✔ Initialize scores
        scores.clear();
        for (Pet p : interviewPets) {
            scores.put(p, 0);
        }

        // ✔ Build UI
        buildUI(chosenType);

        // ✔ Show species intro BEFORE first question
        introLabel.setText(getSpeciesIntro(chosenType));

        // ✔ Start the interview
        showQuestion(chosenType);
    }


    private void buildUI(String type) {
        titleLabel = new Label("🐾 " + type + " Interview Panel 🐾");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Species intro (static per game)
        introLabel = new Label();
        introLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Pet-specific intro (changes per question)
        subIntroLabel = new Label();
        subIntroLabel.setStyle("-fx-font-size: 13px;");

        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        questionLabel.setMaxHeight(Double.MAX_VALUE);

        option1 = new Button();
        option2 = new Button();
        option3 = new Button();
        option4 = new Button();

        option1.setOnAction(e -> handleAnswer(1, type));
        option2.setOnAction(e -> handleAnswer(2, type));
        option3.setOnAction(e -> handleAnswer(3, type));
        option4.setOnAction(e -> handleAnswer(4, type));

        HBox row1 = new HBox(10, option1, option2);
        HBox row2 = new HBox(10, option3, option4);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> UserDashboard.show(stage, loggedInUser));

        // IMPORTANT: add both intros to the layout
        VBox root = new VBox(15, titleLabel, introLabel, subIntroLabel, questionLabel, row1, row2, backButton);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Pet Compatibility Interview");
        stage.show();
    }

    private String getSpeciesIntro(String type) {
        switch (type.toLowerCase()) {
            case "dog":
                return "The dogs gather around you, tails wagging… except the husky who looks too excited.";
            case "cat":
                return "The cats gather around you, judging you silently with flicking tails.";
            case "rabbit":
                return "The rabbits gather softly, ears twitching like a tiny council.";
            case "bird":
                return "The birds gather above you, chirping like a tiny committee.";
            default:
                return "The pets gather around you, trying to decide if you’re worth their time.";
        }
    }

    private String getSpeciesResultsIntro(String type) {
        switch (type.toLowerCase()) {
            case "dog":
                return "The dogs form a circle, whispering excitedly about you.";
            case "cat":
                return "The cats gather in a dramatic circle, tails flicking with judgment.";
            case "rabbit":
                return "The rabbits huddle together, whispering softly.";
            case "bird":
                return "The birds perch together, chirping like a tiny tribunal.";
            default:
                return "The pets gather again, quietly discussing their final verdict.";
        }
    }

    // -------------------------
    // QUESTIONS
    // -------------------------
    private void showQuestion(String type) {
        String species = type.toLowerCase();

        String qText;
        String o1, o2, o3, o4;

        if (currentQuestion == 1) {
            if (species.equals("dog")) {
                subIntroLabel.setText("Bruno (the bossiest one) stares at you.");
                qText = "\"Alright human, how long can you walk without whining?\"";
            } else if (species.equals("cat")) {
                subIntroLabel.setText("Simba flicks his tail dramatically.");
                qText = "\"Human… how much chaos can you tolerate?\"";
            } else if (species.equals("rabbit")) {
                subIntroLabel.setText("Coco twitches her nose nervously.");
                qText = "\"Are you gentle? I bruise emotionally and physically.\"";
            } else {
                subIntroLabel.setText("Rio ruffles his feathers proudly.");
                qText = "\"Can you handle chirping? I don’t come with a mute button.\"";
            }
            o1 = "Long / very active";
            o2 = "Moderate";
            o3 = "Short / low effort";
            o4 = "Basically none";
        }

        else if (currentQuestion == 2) {
            if (species.equals("dog")) {
                subIntroLabel.setText("Daisy rolls her eyes playfully.");
                qText = "\"Be honest. How chaotic are you?\"";
            } else if (species.equals("cat")) {
                subIntroLabel.setText("Nala stares with calculated chaos.");
                qText = "\"How dramatic are you?\"";
            } else if (species.equals("rabbit")) {
                subIntroLabel.setText("Thumper thumps lightly.");
                qText = "\"How calm is your energy?\"";
            } else {
                subIntroLabel.setText("Tweety chirps brightly.");
                qText = "\"How lively are you?\"";
            }
            o1 = "Very energetic";
            o2 = "Moderately energetic";
            o3 = "Calm";
            o4 = "Lazy / very low energy";
        }

        else if (currentQuestion == 3) {
            if (species.equals("dog")) {
                subIntroLabel.setText("Rocky flips his imaginary fur.");
                qText = "\"How often will you brush my fabulous coat?\"";
            } else if (species.equals("cat")) {
                subIntroLabel.setText("Mittens is already shedding on you.");
                qText = "\"How often will you deal with my fur?\"";
            } else if (species.equals("rabbit")) {
                subIntroLabel.setText("Snowball looks extra fluffy.");
                qText = "\"How often will you groom me?\"";
            } else {
                subIntroLabel.setText("Rio preens proudly.");
                qText = "\"How often will you help me stay clean?\"";
            }
            o1 = "Daily";
            o2 = "Weekly";
            o3 = "Monthly";
            o4 = "Never";
        }

        else if (currentQuestion == 4) {
            if (species.equals("dog")) {
                subIntroLabel.setText("Bruno barks once, loudly.");
                qText = "\"I bark. A LOT. Can you handle that?\"";
            } else if (species.equals("cat")) {
                subIntroLabel.setText("Luna meows softly but with menace.");
                qText = "\"Can you handle me yelling at 3 AM?\"";
            } else if (species.equals("rabbit")) {
                subIntroLabel.setText("Coco perks her ears.");
                qText = "\"Do you prefer a quiet home?\"";
            } else {
                subIntroLabel.setText("Tweety is already humming.");
                qText = "\"Can you handle singing and chirping?\"";
            }
            o1 = "Yes, bring the noise";
            o2 = "Sometimes okay";
            o3 = "Prefer quiet";
            o4 = "Absolutely not";
        }

        else if (currentQuestion == 5) {
            if (species.equals("dog")) {
                subIntroLabel.setText("Daisy yawns.");
                qText = "\"How often will you be home?\"";
            } else if (species.equals("cat")) {
                subIntroLabel.setText("Oreo stretches lazily.");
                qText = "\"How often will you be around for me to ignore you?\"";
            } else if (species.equals("rabbit")) {
                subIntroLabel.setText("Thumper softens his gaze.");
                qText = "\"How often will you check on me?\"";
            } else {
                subIntroLabel.setText("Rio tilts his head.");
                qText = "\"How often will you talk to me?\"";
            }
            o1 = "Home all day";
            o2 = "Evenings";
            o3 = "Mornings";
            o4 = "Rarely home";
        }

        else {
            showResults(type);
            return;
        }

        questionLabel.setText(qText);
        option1.setText("1. " + o1);
        option2.setText("2. " + o2);
        option3.setText("3. " + o3);
        option4.setText("4. " + o4);
    }

    // -------------------------
    // SIMPLE BEGINNER-FRIENDLY SCORING
    // -------------------------
    private void handleAnswer(int answer, String type) {

        for (Pet p : interviewPets) {
            int add = 0;

            String breed = p.getBreed().toLowerCase();
            String personality = p.getPersonality().toLowerCase();

            // Q1 — activity level
            if (currentQuestion == 1) {
                if (answer == 1 && (breed.contains("labrador") || breed.contains("retriever") || breed.contains("husky") || breed.contains("german"))) add = 30;
                else if (answer == 2) add = 20;
                else if (answer == 3) add = 10;
            }

            // Q2 — energy
            if (currentQuestion == 2) {
                if (answer == 1 && (personality.contains("energetic") || personality.contains("playful"))) add = 30;
                else if (answer == 2) add = 20;
                else if (answer == 3) add = 10;
            }

            // Q3 — grooming
            if (currentQuestion == 3) {
                if (answer == 1 && (breed.contains("poodle") || breed.contains("persian") || breed.contains("angora"))) add = 30;
                else if (answer == 2) add = 20;
                else if (answer == 3) add = 10;
            }

            // Q4 — noise tolerance
            if (currentQuestion == 4) {
                if (answer == 1 && (type.equalsIgnoreCase("dog") || type.equalsIgnoreCase("bird"))) add = 30;
                else if (answer == 2) add = 20;
                else if (answer == 3) add = 10;
            }

            // Q5 — time at home
            if (currentQuestion == 5) {
                if (answer == 1 && (personality.contains("affectionate") || personality.contains("friendly"))) add = 30;
                else if (answer == 2) add = 20;
                else if (answer == 3) add = 10;
            }

            scores.put(p, scores.get(p) + add);
        }

        currentQuestion++;
        showQuestion(type);
    }
    // -------------------------
    // RESULTS
    // -------------------------
    private void showResults(String type) {

        List<Map.Entry<Pet, Integer>> sorted = new ArrayList<>(scores.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        introLabel.setText(getSpeciesResultsIntro(type));

        StringBuilder sb = new StringBuilder();
        sb.append("🐾 Interview Results (Top 5)\n\n");

        int limit = Math.min(5, sorted.size());
        for (int i = 0; i < limit; i++) {
            Pet p = sorted.get(i).getKey();
            int score = sorted.get(i).getValue();
            double percent = (score / 150.0) * 100;
            sb.append((i + 1)).append(". ").append(p.getName())
                    .append(" — ").append(String.format("%.1f", percent)).append("%\n");
        }

        Pet topPet = sorted.get(0).getKey();
        sb.append("\n").append(topPet.getName()).append(": \"")
                .append(getTopPetFinalLine(topPet)).append("\"");

        questionLabel.setText(sb.toString());

        // ⭐ NEW BUTTON TEXTS
        option1.setText("Adopt " + topPet.getName());
        option2.setText("Adopt someone else from Top 5");
        option3.setText("View all pets");
        option4.setText("Back to Dashboard");

        // ⭐ 1. Adopt highest compatible pet
        option1.setOnAction(e -> {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Adoption");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to adopt " + topPet.getName() +
                    "?\nOur staff will contact you within 48 hours.");

            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("Cancel");
            confirm.getButtonTypes().setAll(yes, no);

            confirm.showAndWait().ifPresent(response -> {
                if (response == yes) {

                    topPet.adopt(loggedInUser.getUsername());
                    petDAO.updatePet(topPet);

                    questionLabel.setText(questionLabel.getText() +
                            "\n\n" + topPet.getName() + ": \"" +
                            getAdoptReaction(topPet, true) + "\"");

                    // ⭐ Delay so reaction is visible
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(ev -> UserDashboard.show(stage, loggedInUser));
                    pause.play();
                }
            });
        });
        // ⭐ 2. Adopt someone else from Top 5
        option2.setOnAction(e -> {

            Alert choose = new Alert(Alert.AlertType.CONFIRMATION);
            choose.setTitle("Choose a Pet");
            choose.setHeaderText("Choose someone else to adopt:");

            // Create buttons for pets 2–5
            List<ButtonType> petButtons = new ArrayList<>();
            List<Pet> otherPets = new ArrayList<>();

            for (int i = 1; i < limit; i++) {
                Pet p = sorted.get(i).getKey();
                otherPets.add(p);
                petButtons.add(new ButtonType(p.getName()));
            }

            ButtonType cancel = new ButtonType("Cancel");
            choose.getButtonTypes().setAll(petButtons);
            choose.getButtonTypes().add(cancel);

            choose.showAndWait().ifPresent(chosenButton -> {

                if (chosenButton == cancel) return;

                // Find which pet was chosen
                Pet chosenPet = null;
                for (int i = 0; i < petButtons.size(); i++) {
                    if (chosenButton == petButtons.get(i)) {
                        chosenPet = otherPets.get(i);
                    }
                }

                if (chosenPet == null) return;

                // Confirm adoption
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Adoption");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to adopt " + chosenPet.getName() +
                        "?\nOur staff will contact you within 48 hours.");

                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("Cancel");
                confirm.getButtonTypes().setAll(yes, no);

                Pet finalChosenPet = chosenPet;

                confirm.showAndWait().ifPresent(resp -> {
                    if (resp == yes) {

                        finalChosenPet.adopt(loggedInUser.getUsername());
                        petDAO.updatePet(finalChosenPet);

                        questionLabel.setText(questionLabel.getText() +
                                "\n\n" + finalChosenPet.getName() + ": \"" +
                                getAdoptReaction(finalChosenPet, true) + "\"" +
                                "\n" + topPet.getName() + ": \"" +
                                getAdoptReaction(topPet, false) + "\"");

                        // ⭐ Delay so reactions are visible
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(ev -> UserDashboard.show(stage, loggedInUser));
                        pause.play();
                    }
                });
            });
        });

        // ⭐ 3. View all pets
        option3.setOnAction(e -> {
            questionLabel.setText(questionLabel.getText() +
                    "\n\n" + topPet.getName() + ": \"" +
                    getAdoptReaction(topPet, false) + "\"");

            ViewPetsScreen.show(stage, loggedInUser);
        });

        // ⭐ 4. Back to Dashboard
        option4.setOnAction(e -> {
            questionLabel.setText(questionLabel.getText() +
                    "\n\n" + topPet.getName() + ": \"" +
                    getAdoptReaction(topPet, false) + "\"");

            UserDashboard.show(stage, loggedInUser);
        });
    }




    // -------------------------
    // FINAL REACTION LINES (species + personality)
    // -------------------------
    private String getTopPetFinalLine(Pet pet) {
        String type = pet.getType().toLowerCase();
        String personality = safe(pet.getPersonality()).toLowerCase();

        if (type.equals("cat")) {
            if (personality.contains("playful") || personality.contains("energetic"))
                return "Alright human, I’m willing to give you a chance.";
            if (personality.contains("independent") || personality.contains("calm"))
                return "I suppose you’re acceptable. Don’t ruin it.";
            if (personality.contains("affectionate") || personality.contains("gentle"))
                return "You seem nice. I think we’ll get along.";
            return "Fine. I choose you. Try not to disappoint me.";
        }

        if (type.equals("dog")) {
            if (personality.contains("playful") || personality.contains("energetic"))
                return "You seem fun! I pick you, let’s make chaos together.";
            if (personality.contains("protective") || personality.contains("alert"))
                return "I’ll look after you, human. Don’t make me regret this.";
            if (personality.contains("friendly") || personality.contains("affectionate"))
                return "I choose you! We’re going to be best friends.";
            if (personality.contains("lazy") || personality.contains("calm"))
                return "You look comfy. I’ll adopt you for naps and snacks.";
            return "I think we’ll make a good team.";
        }

        if (type.equals("rabbit")) {
            if (personality.contains("shy") || personality.contains("calm"))
                return "I trust you… please be gentle with me.";
            if (personality.contains("playful") || personality.contains("curious"))
                return "You seem fun. I’ll hop into your life.";
            if (personality.contains("gentle"))
                return "You feel safe. I choose you softly.";
            return "I think you might be a good human.";
        }

        if (type.equals("bird")) {
            if (personality.contains("cheerful") || personality.contains("playful"))
                return "You look like someone I can sing to all day.";
            if (personality.contains("social"))
                return "You talk, I talk. Perfect match.";
            if (personality.contains("intelligent"))
                return "You seem smart enough for my commentary.";
            if (personality.contains("energetic"))
                return "You look like you can keep up with my noise.";
            return "I’ll keep you entertained. You’re welcome.";
        }

        return "Alright human, I choose you. Don’t make me regret it.";
    }

    private String getAdoptReaction(Pet pet, boolean adoptedTop) {
        String type = pet.getType().toLowerCase();
        String personality = safe(pet.getPersonality()).toLowerCase();

        // -------------------------
        // USER ADOPTED TOP PET
        // -------------------------
        if (adoptedTop) {
            if (type.equals("cat")) {
                if (personality.contains("playful") || personality.contains("energetic"))
                    return "Alright human, I choose to adopt you. Don’t make me regret it.";
                if (personality.contains("independent") || personality.contains("calm"))
                    return "I suppose I’ll adopt you. Try to keep things dignified.";
                if (personality.contains("affectionate") || personality.contains("gentle"))
                    return "I’m glad you picked me. I’ll pretend I’m not excited.";
                return "Fine. I’ll adopt you. You’re not the worst.";
            }

            if (type.equals("dog")) {
                if (personality.contains("playful") || personality.contains("energetic"))
                    return "Yes! I adopt you! Let’s start our adventures right now.";
                if (personality.contains("friendly") || personality.contains("affectionate"))
                    return "I’m so happy you chose me. We’re going to be best friends.";
                if (personality.contains("protective") || personality.contains("alert"))
                    return "I’ll watch over you now. You’re my human.";
                if (personality.contains("lazy") || personality.contains("calm"))
                    return "I adopt you for naps, snacks, and quiet evenings.";
                return "You’re mine now, human. Let’s see how this goes.";
            }

            if (type.equals("rabbit")) {
                if (personality.contains("shy") || personality.contains("calm"))
                    return "I’m a little nervous, but I’m glad you chose me.";
                if (personality.contains("playful") || personality.contains("curious"))
                    return "Yay! I adopt you. Let’s explore everything together.";
                if (personality.contains("gentle"))
                    return "I feel safe with you. I’m happy to adopt you.";
                return "I’ll hop into your life and try my best.";
            }

            if (type.equals("bird")) {
                if (personality.contains("cheerful") || personality.contains("playful"))
                    return "I adopt you! Get ready for songs and chaos.";
                if (personality.contains("social"))
                    return "You’re my human now. We’re going to talk a lot.";
                if (personality.contains("intelligent"))
                    return "I’ve decided you’re worthy of my commentary.";
                if (personality.contains("energetic"))
                    return "I adopt you. Try to keep up with my energy.";
                return "I’ll keep you company. You’re welcome.";
            }

            return "Alright human, I choose to adopt you.";
        }

        // -------------------------
        // USER DID NOT ADOPT TOP PET
        // -------------------------
        if (type.equals("cat")) {
            if (personality.contains("playful") || personality.contains("energetic"))
                return "Seriously? You chose someone else when I was ready to give you a chance?";
            if (personality.contains("independent") || personality.contains("calm"))
                return "Hmph. Your loss. I’ll find a better human.";
            if (personality.contains("affectionate") || personality.contains("gentle"))
                return "Oh… I actually liked you. But I hope you’re happy.";
            return "I was willing to tolerate you. Remember that.";
        }

        if (type.equals("dog")) {
            if (personality.contains("playful") || personality.contains("energetic"))
                return "Aww, I thought we’d have fun together… but okay.";
            if (personality.contains("friendly") || personality.contains("affectionate"))
                return "I’m a little sad, but I hope you find a good friend.";
            if (personality.contains("protective") || personality.contains("alert"))
                return "I was ready to guard you, but I guess you chose another path.";
            if (personality.contains("lazy") || personality.contains("calm"))
                return "I was fine with a quiet life together… but I’ll nap anyway.";
            return "I thought we could’ve been a good team. Maybe next time.";
        }

        if (type.equals("rabbit")) {
            if (personality.contains("shy") || personality.contains("calm"))
                return "Oh… you chose someone else. That’s okay… I’ll be alright.";
            if (personality.contains("playful") || personality.contains("curious"))
                return "I was excited to hop around with you… but I hope you’re happy.";
            if (personality.contains("gentle"))
                return "I really thought you were kind… I hope your new friend treats you well.";
            return "I’ll just nibble my snacks and move on.";
        }

        if (type.equals("bird")) {
            if (personality.contains("cheerful") || personality.contains("playful"))
                return "I was ready to sing for you… but I’ll find another audience.";
            if (personality.contains("social"))
                return "I thought we’d talk all day, but I guess you chose someone else.";
            if (personality.contains("intelligent"))
                return "I literally speak better than some humans, but sure, pick someone else.";
            if (personality.contains("energetic"))
                return "I had so much energy for you… but I’ll flap it off.";
            return "I’ll just keep chirping for someone else then.";
        }

        return "You really chose someone else when I was ready to adopt you? Wow.";
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}

