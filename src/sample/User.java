package sample;

public class User {
    private String username;
    private String password;
    private static User savedUser;

    public static User getSavedUser() {
        return savedUser;
    }

    public static void setSavedUser(User savedUser) {
        User.savedUser = savedUser;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
