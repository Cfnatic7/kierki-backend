package data;

import data.User;

import java.util.ArrayList;
import java.util.List;

public class UserAccounts {

    private final List<User> users = new ArrayList<>();

    public UserAccounts() {

    }

    public void initializeBasicUserAccounts() {
        users.add(new User("test", "test"));
        users.add(new User("Jan", "Testowy"));
        users.add(new User("Jan", "Nowak"));
        users.add(new User("Jan", "Nowak"));
        users.add(new User("Fryderyk", "Chopin"));
        users.add(new User("Fiodor", "Dostojewski"));
        users.add(new User("Fryderyk", "Nietzshe"));
    }


    public List<User> getUsers() {
        return users;
    }

    public synchronized void addUser(String login, String password) {
        users.add(new User(login, password));
    }
}
