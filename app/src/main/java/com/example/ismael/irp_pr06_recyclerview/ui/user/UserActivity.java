package com.example.ismael.irp_pr06_recyclerview.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismael.irp_pr06_recyclerview.R;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.Avatar;
import com.example.ismael.irp_pr06_recyclerview.data.local.model.User;
import com.example.ismael.irp_pr06_recyclerview.ui.avatar.AvatarActivity;
import com.example.ismael.irp_pr06_recyclerview.ui.list.ListActivity;
import com.example.ismael.irp_pr06_recyclerview.utils.KeyboardUtils;
import com.example.ismael.irp_pr06_recyclerview.utils.SnackbarUtils;
import com.example.ismael.irp_pr06_recyclerview.utils.ValidationUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "EXTRA_USER";
    private static final int RC_EDIT = 11;
    private UserActivityViewModel viewModel;

    private static final String EXTRA_AVATAR = "EXTRA_AVATAR";
    private static final int RC_AVATAR = 1;
    private Avatar avatar;

    private static final String EXTRA_DISABLED = "EXTRA_DISABLED";

    private GenericTextWatcher nameWatcher;
    private GenericTextWatcher emailWatcher;
    private GenericTextWatcher phoneWatcher;
    private GenericTextWatcher addressWatcher;
    private GenericTextWatcher webWatcher;

    private boolean[] fieldsDisableds;
    private User user;

    private TextView lblAvatar;
    private TextView lblName;
    private TextView lblEmail;
    private TextView lblPhonenumber;
    private TextView lblAddress;
    private TextView lblWeb;

    private ImageView imgAvatar;
    private ImageView imgEmail;
    private ImageView imgPhonenumber;
    private ImageView imgAddress;
    private ImageView imgWeb;

    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPhonenumber;
    private EditText txtAddress;
    private EditText txtWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //b = DataBindingUtil.setContentView(this, R.layout.activity_user);
        setContentView(R.layout.activity_user);
        getIntentData();
        initViews();
        viewModel = ViewModelProviders.of(this).get(UserActivityViewModel.class);
        final Observer<Avatar> observerAvatar = this::restoreStateAvatar;
        viewModel.getAvatar().observe(this, observerAvatar);

        if (savedInstanceState != null) {
            restoreDisableds(Objects.requireNonNull(savedInstanceState.getBooleanArray(EXTRA_DISABLED)));
        }

        checkDisableds();
        fillFields();
    }

    public static void startForResultList(ListActivity activity, int rcUser, User user) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        activity.startActivityForResult(intent, rcUser);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_USER)) {
                user = intent.getParcelableExtra(EXTRA_USER);
                avatar = user.getAvatar();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray(EXTRA_DISABLED, fieldsDisableds);
        super.onSaveInstanceState(outState);
    }

    private void checkDisableds() {
        fieldsDisableds[0] = !lblName.isEnabled();
        fieldsDisableds[1] = !lblEmail.isEnabled();
        fieldsDisableds[2] = !lblPhonenumber.isEnabled();
        fieldsDisableds[3] = !lblAddress.isEnabled();
        fieldsDisableds[4] = !lblWeb.isEnabled();
    }

    private void restoreDisableds(boolean[] disableds) {

        lblName.setEnabled(!disableds[0]);

        lblEmail.setEnabled(!disableds[1]);
        imgEmail.setEnabled(!disableds[1]);

        lblPhonenumber.setEnabled(!disableds[2]);
        imgPhonenumber.setEnabled(!disableds[2]);

        lblAddress.setEnabled(!disableds[3]);
        imgAddress.setEnabled(!disableds[3]);

        lblWeb.setEnabled(!disableds[4]);
        imgWeb.setEnabled(!disableds[4]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtName.addTextChangedListener(nameWatcher);
        txtEmail.addTextChangedListener(emailWatcher);
        txtPhonenumber.addTextChangedListener(phoneWatcher);
        txtAddress.addTextChangedListener(addressWatcher);
        txtWeb.addTextChangedListener(webWatcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        txtName.removeTextChangedListener(nameWatcher);
        txtEmail.removeTextChangedListener(emailWatcher);
        txtPhonenumber.removeTextChangedListener(phoneWatcher);
        txtAddress.removeTextChangedListener(addressWatcher);
        txtWeb.removeTextChangedListener(webWatcher);
    }

    private void initViews() {
        imgAvatar = ActivityCompat.requireViewById(this, R.id.imgAvatar);
        imgAvatar.setOnClickListener(v -> startAvatarActivity());

        lblAvatar = ActivityCompat.requireViewById(this, R.id.lblAvatar);
        lblAvatar.setOnClickListener(v -> startAvatarActivity());

        lblName = ActivityCompat.requireViewById(this, R.id.lblName);
        txtName = ActivityCompat.requireViewById(this, R.id.txtName);
        txtName.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(lblName));

        lblEmail = ActivityCompat.requireViewById(this, R.id.lblEmail);
        txtEmail = ActivityCompat.requireViewById(this, R.id.txtEmail);
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(lblEmail));

        lblPhonenumber = ActivityCompat.requireViewById(this, R.id.lblPhonenumber);
        txtPhonenumber = ActivityCompat.requireViewById(this, R.id.txtPhonenumber);
        txtPhonenumber.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(lblPhonenumber));

        lblAddress = ActivityCompat.requireViewById(this, R.id.lblAddress);
        txtAddress = ActivityCompat.requireViewById(this, R.id.txtAddress);
        txtAddress.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(lblAddress));

        lblWeb = ActivityCompat.requireViewById(this, R.id.lblWeb);
        txtWeb = ActivityCompat.requireViewById(this, R.id.txtWeb);
        txtWeb.setOnFocusChangeListener((v, hasFocus) -> txtSwapBold(lblWeb));

        txtWeb.setOnEditorActionListener((v, actionId, event) -> {
            save();
            return true;
        });

        imgEmail = ActivityCompat.requireViewById(this, R.id.imgEmail);
        imgEmail.setOnClickListener(v -> sendEmail());

        imgPhonenumber = ActivityCompat.requireViewById(this, R.id.imgPhonenumber);
        imgPhonenumber.setOnClickListener(v -> dial());

        imgAddress = ActivityCompat.requireViewById(this, R.id.imgAddress);
        imgAddress.setOnClickListener(v -> maps());

        imgWeb = ActivityCompat.requireViewById(this, R.id.imgWeb);
        imgWeb.setOnClickListener(v -> searchWeb());

        nameWatcher = new GenericTextWatcher(txtName);
        emailWatcher = new GenericTextWatcher(txtEmail);
        phoneWatcher = new GenericTextWatcher(txtPhonenumber);
        addressWatcher = new GenericTextWatcher(txtAddress);
        webWatcher = new GenericTextWatcher(txtWeb);

        fieldsDisableds = new boolean[5];
    }

    private void showLabel(String text) {
        lblAvatar.setText(text);
    }

    private void restoreStateAvatar(Avatar newAvatar) {
        showAvatar(newAvatar);
        showLabel(newAvatar.getName());
    }

    private void startAvatarActivity() {
        AvatarActivity.startForResult(UserActivity.this, RC_AVATAR, avatar);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RC_AVATAR) {
            if (data != null && data.hasExtra(AvatarActivity.EXTRA_AVATAR)) {
                avatar = data.getParcelableExtra(EXTRA_AVATAR);

                showAvatar(avatar);
                showLabel(avatar.getName());
                viewModel.setAvatar(avatar);
            }
        } else if (resultCode == RESULT_OK && requestCode == RC_EDIT) {
            if (data != null && data.hasExtra(ListActivity.EXTRA_USER)) {
                user = data.getParcelableExtra(EXTRA_USER);

            }
        }
    }

    private void fillFields() {
        if (user != null) {
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());
            txtPhonenumber.setText(user.getPhone());
            txtAddress.setText(user.getAddress());
            txtWeb.setText(user.getWeb());
            imgAvatar.setImageResource(user.getAvatar().getImageResId());
        }

    }

    private void showAvatar(Avatar newAvatar) {
        imgAvatar.setImageResource(newAvatar.getImageResId());
        imgAvatar.setTag(newAvatar.getImageResId());
        this.avatar = newAvatar;
    }

    private void sendEmail() {
        Intent intent;
        String address = txtEmail.getText().toString();

        if (!isWrongEmail()) {
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + address));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(this);
                SnackbarUtils.snackbar(imgEmail, getString(R.string.error_email), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorEmail(isWrongEmail());
        }
    }

    private void dial() {
        Intent intent;
        String phoneNumber = txtPhonenumber.getText().toString();

        if (!isWrongPhonenumber()) {
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(this);
                SnackbarUtils.snackbar(imgPhonenumber, getString(R.string.error_phonenumber), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorPhonenumber(isWrongPhonenumber());
        }
    }

    private void maps() {
        Intent intent;
        String address = txtAddress.getText().toString();

        if (!isWrongAddress()) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + address));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(this);
                SnackbarUtils.snackbar(imgAddress, getString(R.string.error_address), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorAddress(isWrongAddress());
        }
    }

    private void searchWeb() {
        Intent intent;
        String web = txtWeb.getText().toString();

        if (!isWrongWeb()) {
            if (web.substring(0, 8).matches("https://") || web.substring(0, 7).matches("http://")) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(web));
            } else {
                intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, web);
            }

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                KeyboardUtils.hideSoftKeyboard(this);
                SnackbarUtils.snackbar(imgWeb, getString(R.string.error_web), Snackbar.LENGTH_SHORT);
            }
        } else {
            setErrorWeb(isWrongWeb());
        }
    }


    private void txtSwapBold(TextView txt) {
        if (txt.getTypeface().isBold()) {
            txt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        } else {
            txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtName:
                    setErrorName(isWrongName());
                    break;
                case R.id.txtEmail:
                    setErrorEmail(isWrongEmail());
                    break;
                case R.id.txtPhonenumber:
                    setErrorPhonenumber(isWrongPhonenumber());
                    break;
                case R.id.txtAddress:
                    setErrorAddress(isWrongAddress());
                    break;
                case R.id.txtWeb:
                    setErrorWeb(isWrongWeb());
                    break;
            }

        }
    }

    private boolean isWrongName() {
        boolean isWrong = false;
        if (txtName.getText().toString().length() <= 0) {
            isWrong = true;
        }
        fieldsDisableds[0] = isWrong;
        return isWrong;
    }

    private void setErrorName(boolean wrong) {
        if (wrong) {
            txtName.setError((getString(R.string.main_invalid_data)));
            lblName.setEnabled(false);
        } else {
            txtName.setError(null);
            lblName.setEnabled(true);
        }
    }

    private boolean isWrongEmail() {
        boolean isWrong = false;
        if (!ValidationUtils.isValidEmail(txtEmail.getText().toString())) {
            isWrong = true;
        }
        fieldsDisableds[1] = isWrong;
        return isWrong;
    }

    private void setErrorEmail(boolean wrong) {
        if (wrong) {
            txtEmail.setError((getString(R.string.main_invalid_data)));
            imgEmail.setEnabled(false);
            lblEmail.setEnabled(false);
        } else {
            txtAddress.setError(null);
            imgEmail.setEnabled(true);
            lblEmail.setEnabled(true);
        }
    }

    private boolean isWrongPhonenumber() {
        boolean isWrong = false;
        if (!ValidationUtils.isValidPhone(txtPhonenumber.getText().toString())) {
            isWrong = true;
        }
        fieldsDisableds[2] = isWrong;
        return isWrong;
    }

    private void setErrorPhonenumber(boolean wrong) {
        if (wrong) {
            txtPhonenumber.setError((getString(R.string.main_invalid_data)));
            imgPhonenumber.setEnabled(false);
            lblPhonenumber.setEnabled(false);
        } else {
            txtPhonenumber.setError(null);
            imgPhonenumber.setEnabled(true);
            lblPhonenumber.setEnabled(true);
        }
    }

    private boolean isWrongAddress() {
        boolean isWrong = false;
        if (txtAddress.getText().toString().length() <= 0) {
            isWrong = true;
        }
        fieldsDisableds[3] = isWrong;
        return isWrong;
    }

    private void setErrorAddress(boolean wrong) {
        if (wrong) {
            txtAddress.setError((getString(R.string.main_invalid_data)));
            imgAddress.setEnabled(false);
            lblAddress.setEnabled(false);
        } else {
            txtAddress.setError(null);
            imgAddress.setEnabled(true);
            lblAddress.setEnabled(true);
        }
    }

    private boolean isWrongWeb() {
        boolean isWrong = false;
        if (!ValidationUtils.isValidUrl(txtWeb.getText().toString())) {
            isWrong = true;
        }
        fieldsDisableds[4] = isWrong;
        return isWrong;
    }

    private void setErrorWeb(boolean wrong) {
        if (wrong) {
            txtWeb.setError((getString(R.string.main_invalid_data)));
            imgWeb.setEnabled(false);
            lblWeb.setEnabled(false);
        } else {
            txtWeb.setError(null);
            imgWeb.setEnabled(true);
            lblWeb.setEnabled(true);
        }
    }

    private void save() {
        boolean valid;
        KeyboardUtils.hideSoftKeyboard(this);

        valid = isFormValid();

        if (!valid) {
            SnackbarUtils.snackbar(imgAvatar, getString(R.string.main_error_saving), Snackbar.LENGTH_SHORT);
            return;
        }

        user.setAvatar(avatar);
        user.setName(txtName.getText().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setPhone(txtPhonenumber.getText().toString());
        user.setAddress(txtAddress.getText().toString());
        user.setWeb(txtWeb.getText().toString());

        Intent result = new Intent();
        result.putExtra(EXTRA_USER, user);
        setResult(RESULT_OK, result);
        finish();
    }

    private boolean isFormValid() {
        boolean valid = true;

        if (isWrongName()) {
            valid = false;
            setErrorName(isWrongName());
        }
        if (isWrongEmail()) {
            valid = false;
            setErrorEmail(isWrongEmail());
        }
        if (isWrongPhonenumber()) {
            valid = false;
            setErrorPhonenumber(isWrongPhonenumber());
        }
        if (isWrongAddress()) {
            valid = false;
            setErrorAddress(isWrongAddress());
        }

        if (isWrongWeb()) {
            valid = false;
            setErrorWeb(isWrongWeb());
        }

        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuSave) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
