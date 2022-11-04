package data;

import enums.RoomNumber;

import java.util.Objects;

public class User {

    private String login;

    private String password;

    private boolean isLoggedIn;

    private RoomNumber roomNumber;

    public User(String login, String password) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        this.login = login;
        this.password = password;
        this.isLoggedIn = false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return user.login.equals(this.login) && user.password.equals(this.password);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
    }
}
