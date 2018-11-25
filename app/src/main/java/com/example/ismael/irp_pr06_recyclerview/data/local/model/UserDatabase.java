package com.example.ismael.irp_pr06_recyclerview.data.local.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserDatabase {

    private static UserDatabase instance;

    public static UserDatabase getInstance() {
        if (instance == null) {
            synchronized (UserDatabase.class) {
                if (instance == null) {
                    instance = new UserDatabase();
                }
            }
        }
        return instance;
    }

    private final AvatarDatabase avatarDatabase = AvatarDatabase.getInstance();

    private final ArrayList<User> users = new ArrayList<>(Arrays.asList(
            new User("Baldo", "baldo@mero.com", "666666666", "Calle de Baldomero", "www.baldomerillo.es", avatarDatabase.getDefaultAvatar()),
            new User("German Ginés", "german@mero.com", "776666666", "Carretera Germancillo", "www.marca.es", avatarDatabase.queryAvatar(2)),
            new User("Dolores Fuertes de Barriga", "dolores@fuertes.com", "776666666", "Calle de todos los santos", "www.google.com", avatarDatabase.queryAvatar(3))
    ));
    private final MutableLiveData <List<User>> usersLiveData = new MutableLiveData<>();

    public UserDatabase() {
        updateUsersLiveData();
    }

    private void updateUsersLiveData() {
        usersLiveData.setValue(new ArrayList<>(users));
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }

    public void deleteUser(User user) {
        users.remove(user);
        updateUsersLiveData();
    }

    public void editUser(User newUser) {
        for (int i=0; i<users.size(); i++){
            if (users.get(i).getId() == newUser.getId()){
                users.set(i, newUser);
                i = users.size();
            }
        }
        updateUsersLiveData();

    }

    public void addUser(User user) {
        users.add(user);
        updateUsersLiveData();
    }
}
