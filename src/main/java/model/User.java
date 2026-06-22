package model;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private int age;
    private String preferredPetType;
    private String preferredSize;
    private String preferredPersonality;
    private String username;
    private boolean staff;


    // Constructor for registration
    public User(String name, String email, String password, int age,
                String preferredPetType, String preferredSize, String preferredPersonality) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.preferredPetType = preferredPetType;
        this.preferredSize = preferredSize;
        this.preferredPersonality = preferredPersonality;
    }

    // Empty constructor for login
    public User() {}

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getPreferredPetType() {
        return preferredPetType;
    }

    public String getPreferredSize() {
        return preferredSize;
    }

    public String getPreferredPersonality() {
        return preferredPersonality;
    }
    public String getUsername() {
        return username;
    }

    public boolean isStaff() {
        return staff;
    }


    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPreferredPetType(String preferredPetType) {
        this.preferredPetType = preferredPetType;
    }

    public void setPreferredSize(String preferredSize) {
        this.preferredSize = preferredSize;
    }

    public void setPreferredPersonality(String preferredPersonality) {
        this.preferredPersonality = preferredPersonality;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

}
