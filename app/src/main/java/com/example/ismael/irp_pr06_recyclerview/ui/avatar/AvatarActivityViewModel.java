package com.example.ismael.irp_pr06_recyclerview.ui.avatar;

import com.example.ismael.irp_pr06_recyclerview.data.local.model.Avatar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class AvatarActivityViewModel extends ViewModel {

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
