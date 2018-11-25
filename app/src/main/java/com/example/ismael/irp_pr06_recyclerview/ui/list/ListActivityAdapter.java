package com.example.ismael.irp_pr06_recyclerview.ui.list;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ismael.irp_pr06_recyclerview.R;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.User;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivityAdapter extends ListAdapter<User, ListActivityAdapter.ViewHolder> {

    private final OnEditListener onEditListener;
    private final OnDeleteListener onDeleteListener;

    public ListActivityAdapter(OnEditListener onEditListener, OnDeleteListener onDeleteListener) {
        super(new DiffUtil.ItemCallback<User>() {

            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                boolean same = false;
                if (TextUtils.equals(oldItem.getName(), newItem.getName()) &&
                        TextUtils.equals(oldItem.getEmail(), newItem.getEmail()) &&
                        TextUtils.equals(oldItem.getPhone(), newItem.getPhone()) &&
                        oldItem.getAvatar().getId() == newItem.getAvatar().getId()){
                    same = true;
                }

                return same;
            }
        });
        this.onEditListener = onEditListener;
        this.onDeleteListener = onDeleteListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_item, parent, false), onEditListener, onDeleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView lblName;
        private TextView lblEmail;
        private TextView lblPhone;
        private ImageView imgAvatar;
        private Button btnEdit;
        private Button btnDelete;

        ViewHolder(View itemView, OnEditListener onEditListener, OnDeleteListener onDeleteListener) {
            super(itemView);
            setupViews(itemView);

            if (onDeleteListener != null) {
                btnDelete.setOnClickListener(v1 -> onDeleteListener.onDelete(getAdapterPosition()));
            }

            if (onEditListener != null) {
                btnEdit.setOnClickListener(v2 -> onEditListener.onEdit(getAdapterPosition()));
            }
        }

        private void setupViews(View itemView) {
            lblName = ViewCompat.requireViewById(itemView, R.id.txtName);
            lblEmail = ViewCompat.requireViewById(itemView, R.id.lblEmail);
            lblPhone = ViewCompat.requireViewById(itemView, R.id.lblPhonenumber);
            imgAvatar = ViewCompat.requireViewById(itemView, R.id.imgUser);
            btnEdit = ViewCompat.requireViewById(itemView, R.id.btnEdit);
            btnDelete = ViewCompat.requireViewById(itemView, R.id.btnDelete);
        }

        void bind(User user) {
            lblName.setText(user.getName());
            lblEmail.setText(user.getEmail());
            lblPhone.setText(String.valueOf(user.getPhone()));
            imgAvatar.setImageResource(user.getAvatar().getImageResId());
        }
    }
}
