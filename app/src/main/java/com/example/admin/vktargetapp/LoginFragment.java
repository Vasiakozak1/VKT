package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginFragment extends Fragment {

    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    Session session;
    TextView userEmailInHeader;
    Chip errorMessageView;
    MainActivity mainActivity;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_fragment, container, false);
        this.view = view;

        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);

        errorMessageView = view.findViewById(R.id.errorMessage);
        VkTargetApplication.setCurrentFragment(this);
        VkTargetApplication.disableNavigationMenu();
       // session = new Session(VkTargetApplication.getAppContext());

        final Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VkTargetApplication.setLoading();
                mainActivity = (MainActivity) VkTargetApplication
                        .getCurrentActivity();
                userEmailInHeader = mainActivity
                        .findViewById(R.id.userEmail);

                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                userEmailInHeader.setText(email);
                SharedPreferences.Editor dataEditor = mainActivity.preferences.edit();
                dataEditor.putString("email", email);
                dataEditor.commit();
                //session.setEmail(email);
                VkTargetWebCrawler.getInstance().RetrieveApiKey(email, password);
            }
        });
        VkTargetApplication.setLoaded();
        return view;
    }

    public void ShowWrongCredentialsMessage() {
            this.errorMessageView.setVisibility(View.VISIBLE);
    }
    private void hideErrorMEssage(){
        this.errorMessageView.setVisibility(View.INVISIBLE);
    }
}
