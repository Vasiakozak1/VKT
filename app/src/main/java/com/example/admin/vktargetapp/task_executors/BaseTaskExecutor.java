package com.example.admin.vktargetapp.task_executors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.example.admin.vktargetapp.R;
import com.example.admin.vktargetapp.VkTargetApplication;

public abstract class BaseTaskExecutor implements ITaskExecutor {
    protected WebView webView;
    protected String currentTasktToExecuteUrl;
    protected String currentLoggedInUserEmail;
    protected String tempLogin; // Both of these fields is to call login() method just after logout()
    protected String tempPassword;

    protected abstract void login(String userName, String password);
    protected abstract void logout();
    protected abstract void determineWhatToDoInTask();

    protected void setDialogForLogin() {
        LayoutInflater layoutInflater = LayoutInflater.from(VkTargetApplication.getCurrentActivity());
        View dialogView = layoutInflater.inflate(R.layout.enter_creds_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(VkTargetApplication.getCurrentActivity());
        builder.setView(dialogView);
        builder.setTitle("Залогінитися на сервісі");
        final TextInputEditText loginTextInput = dialogView.findViewById(R.id.loginDialogInput);
        final TextInputEditText passwordTextInput = dialogView.findViewById(R.id.passwordDialogInput);
        builder.setCancelable(false)
                .setPositiveButton("Логін", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String login = loginTextInput.getText().toString();
                        String password = passwordTextInput.getText().toString();
                        if(login.equals(currentLoggedInUserEmail)) {
                            webView.loadUrl(currentTasktToExecuteUrl);
                            determineWhatToDoInTask();
                        }
                        else if(currentLoggedInUserEmail.equals("")) {
                            webView.loadUrl(currentTasktToExecuteUrl);
                            login(login, password);
                        }
                        else if(!currentLoggedInUserEmail.equals(login)) {
                            tempLogin = login;
                            tempPassword = password;
                            logout();
                        }
                    }
                });
        builder.setNegativeButton("Відміна", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
