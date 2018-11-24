package com.example.ismael.irp_pr06_recyclerview.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.ismael.irp_pr06_recyclerview.R;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.AvatarDatabase;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.User;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.UserDatabase;
import com.example.ismael.irp_pr06_recyclerview.databinding.ActivityListBinding;
import com.example.ismael.irp_pr06_recyclerview.ui.user.UserActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

public class ListActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "EXTRA_USER";
    private static final int RC_EDIT = 11;
    private static final int RC_ADD = 111;
    private ListActivityViewModel viewModel;
    private ListActivityAdapter listAdapter;
    private ActivityListBinding b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_list);
        viewModel = ViewModelProviders.of(this, new ListActivityViewModelFactory(new UserDatabase())).get(ListActivityViewModel.class);
        setupRecyclerView();
        observeStudents();
    }

    private void observeStudents() {
        viewModel.getUsers(false).observe(this, users -> {
            listAdapter.submitList(users);
            b.lblEmptyView.setVisibility(users.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void setupRecyclerView() {

        View.OnClickListener listenerAdd;

        listAdapter = new ListActivityAdapter(this::edit, position -> viewModel.deleteUser(listAdapter.getItem(position)));

        b.rvList.setHasFixedSize(true);
        b.rvList.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.rvList_columns)));

        b.rvList.setItemAnimator(new DefaultItemAnimator());
        b.rvList.setAdapter(listAdapter);

        listenerAdd =  v -> addUser();
        b.fabAdd.setOnClickListener(listenerAdd);
        b.lblEmptyView.setOnClickListener(listenerAdd);

    }

    private void addUser() {
        UserActivity.startForResultList(this, RC_ADD, User.getDefaultUser());
    }

    private void edit(int position) {
        UserActivity.startForResultList(ListActivity.this, RC_EDIT, listAdapter.getItem(position));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user;
        if (resultCode == RESULT_OK && requestCode == RC_EDIT) {
            if (data != null && data.hasExtra(UserActivity.EXTRA_USER)) {
                user = data.getParcelableExtra(UserActivity.EXTRA_USER);
                viewModel.editUser(user);
            }
        }else if (resultCode == RESULT_OK && requestCode == RC_ADD){
            if (data != null && data.hasExtra(UserActivity.EXTRA_USER)){
                user = data.getParcelableExtra(UserActivity.EXTRA_USER);
                viewModel.addUser(user);
            }
        }
    }

}
