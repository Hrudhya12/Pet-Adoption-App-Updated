package model;

import java.sql.Timestamp;

public class Pet {

    private int petId;
    private String name;
    private String type;
    private String breed;
    private int age;
    private String gender;
    private String size;
    private String personality;   // was temperament
    private String healthStatus;
    private boolean adopted;
    private String adopterUsername;
    private Timestamp adoptionDate;

    // Optional extra fields from your earlier version
    private String color;
    private String favoriteActivity;
    private double adoptionFee;
    private double weight;
    private String arrivalDate;
    private String uniqueTrait;

    // Constructor used by DAO (DB → object)
    public Pet(int petId, String name, String type, String breed, int age, String gender,
               String size, String personality, String healthStatus,
               boolean adopted, String adopterUsername, Timestamp adoptionDate) {
        this.petId = petId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.size = size;
        this.personality = personality;
        this.healthStatus = healthStatus;
        this.adopted = adopted;
        this.adopterUsername = adopterUsername;
        this.adoptionDate = adoptionDate;
    }

    // Optional extended constructor (if you still use it)
    public Pet(String name, int age, String gender, String personality,
               String breed, String color, String healthStatus,
               String favoriteActivity, double adoptionFee,
               double weight, String arrivalDate, String uniqueTrait) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.breed = breed;
        this.color = color;
        this.healthStatus = healthStatus;
        this.favoriteActivity = favoriteActivity;
        this.adoptionFee = adoptionFee;
        this.weight = weight;
        this.arrivalDate = arrivalDate;
        this.uniqueTrait = uniqueTrait;
    }

    // Getters for table / logic
    public int getPetId() { return petId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getBreed() { return breed; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getSize() { return size; }
    public String getPersonality() { return personality; }
    public String getHealthStatus() { return healthStatus; }
    public boolean isAdopted() { return adopted; }
    public String getAdopterUsername() { return adopterUsername; }
    public Timestamp getAdoptionDate() { return adoptionDate; }

    // Setters (only where needed)
    public void setAdopted(boolean adopted) { this.adopted = adopted; }
    public void setAdopterUsername(String adopterUsername) { this.adopterUsername = adopterUsername; }
    public void setAdoptionDate(Timestamp adoptionDate) { this.adoptionDate = adoptionDate; }

    // Convenience method used in adoption flows
    public void adopt(String username) {
        this.adopted = true;
        this.adopterUsername = username;
        this.adoptionDate = new Timestamp(System.currentTimeMillis());
    }

    // For console listing
    public String getListLine() {
        return name + " (" + type + ", " + breed + ", " + age + " yrs, " + size + ", " + personality + ")";
    }

    public String getFullDetails() {
        return "Name: " + name + "\n"
                + "Type: " + type + "\n"
                + "Breed: " + breed + "\n"
                + "Age: " + age + "\n"
                + "Gender: " + gender + "\n"
                + "Size: " + size + "\n"
                + "Personality: " + personality + "\n"
                + "Health: " + healthStatus + "\n"
                + "Adopted: " + (adopted ? "Yes" : "No");
    }

    // equals/hashCode if you want GameScreen scoring to be safe
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return petId == pet.petId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(petId);
    }
}
