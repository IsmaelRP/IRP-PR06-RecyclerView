package com.example.ismael.irp_pr06_recyclerview.ui.user;

import com.example.ismael.irp_pr06_recyclerview.data.local.model.Avatar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class UserActivityViewModel extends ViewModel {


    public UserActivityViewModel() {

    }

    private MutableLiveData<Avatar> avatar;

    public LiveData<Avatar> getAvatar() {
        if (avatar == null) {
            avatar = new MutableLiveData<>();
        }
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar.setValue(avatar);
    }

}
