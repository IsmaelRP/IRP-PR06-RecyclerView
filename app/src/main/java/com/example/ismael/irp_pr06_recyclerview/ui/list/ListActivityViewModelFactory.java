package com.example.ismael.irp_pr06_recyclerview.ui.list;

import com.example.ismael.irp_pr06_recyclerview.data.local.model.UserDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class ListActivityViewModelFactory implements ViewModelProvider.Factory {

    private final UserDatabase database;

    public ListActivityViewModelFactory(UserDatabase database) {
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ListActivityViewModel(database);
    }
}
