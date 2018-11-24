package com.example.ismael.irp_pr06_recyclerview.data.local.model;

import com.example.ismael.irp_pr06_recyclerview.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.VisibleForTesting;

public class AvatarDatabase {

        private static AvatarDatabase instance;

        private final ArrayList<Avatar> avatars = new ArrayList<>();
        private final Random random = new Random(1);
        private long count;

        private AvatarDatabase() {
            insertAvatar(new Avatar(R.drawable.cat1, "Tom"));
            insertAvatar(new Avatar(R.drawable.cat2, "Luna"));
            insertAvatar(new Avatar(R.drawable.cat3, "Simba"));
            insertAvatar(new Avatar(R.drawable.cat4, "Kitty"));
            insertAvatar(new Avatar(R.drawable.cat5, "Felix"));
            insertAvatar(new Avatar(R.drawable.cat6, "Nina"));
        }

        public static AvatarDatabase getInstance() {
            if (instance == null) {
                synchronized (AvatarDatabase.class) {
                    if (instance == null) {
                        instance = new AvatarDatabase();
                    }
                }
            }
            return instance;
        }

        @VisibleForTesting()
        private void insertAvatar(Avatar avatar) {
            long id = ++count;
            avatar.setId(id);
            avatars.add(avatar);
        }

        public Avatar getRandomAvatar() {
            if (avatars.size() == 0) return null;
            return avatars.get(random.nextInt(avatars.size()));
        }

        public Avatar getDefaultAvatar() {
            if (avatars.size() == 0) return null;
            return avatars.get(0);
        }

        public List<Avatar> queryAvatars() {
            return new ArrayList<>(avatars);
        }

        public Avatar queryAvatar(long id) {
            for (Avatar avatar: avatars) {
                if (avatar.getId() == id) {
                    return avatar;
                }
            }
            return null;
        }

        @VisibleForTesting
        public void setAvatars(List<Avatar> list) {
            count = 0;
            avatars.clear();
            avatars.addAll(list);
        }



}
