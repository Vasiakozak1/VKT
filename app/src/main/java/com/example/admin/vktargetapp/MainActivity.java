package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements NavigationHost {
    public SharedPreferences preferences;
    private DrawerLayout appNavigationDrawer;
    private Toolbar appToolbar;
    private TextView userEmailView;

    public static ProgressBar ProgressBar;
    public static WebView WebCrawlerView;
    public static NavigationView AppNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebCrawlerView = findViewById(R.id.webCrawlerView);

        VkTargetApplication.setCurrentActivity(this);
        ProgressBar = findViewById(R.id.progressBar);
        ProgressBar.setVisibility(View.VISIBLE);
        appNavigationDrawer = findViewById(R.id.drawer_layout);
        AppNavigationView = findViewById(R.id.nav_view);
        appToolbar = findViewById(R.id.main_toolbar);

        preferences = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        View navigation = AppNavigationView.getHeaderView(0);
        userEmailView = navigation.findViewById(R.id.userEmail);
        String currentUserEmail = preferences.getString("email", "");
        userEmailView.setText(currentUserEmail != null ? currentUserEmail : "");
       // session = new Session(getApplicationContext());


        setSupportActionBar(appToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        AppNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                int menuItemId = menuItem.getItemId();
                switch (menuItemId) {
                    case R.id.logoutBtn:
                        LogOut();
                        break;
                    case R.id.tasksBtn:
                        ShowTasks();
                        break;
                    case R.id.doneTasks:
                        ShowFinishedTasks();
                        break;
                }
                appNavigationDrawer.closeDrawers();

                return true;
            }
        });

        try{
            ManageLogin();
        }
        catch (IOException e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    private void Login() throws IOException {
        NavigateTo(new LoginFragment(), false);
    }

    private void LogOut() {
        VkTargetWebCrawler
                .getInstance()
                .LogOut();
        TextView emailInHeader = findViewById(R.id.userEmail);
        emailInHeader.setText("");
        NavigateTo(new LoginFragment(), false);
    }

    @Override
    public void NavigateTo(Fragment fragment, boolean addTobackstck) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.appContainer, fragment);
        if(addTobackstck) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                appNavigationDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void retrieveTasks() {
        VkTargetWebCrawler
                .getInstance()
                .RetrieveTasks();
    }

    public void ManageLogin() throws IOException {
        String apiKey = VkTargetApplication.getApiKey();
        if(Session.NeedsLogin == NeedsLogin.Yes
                || apiKey.equals("")) {
            Login();
        }
        else if(Session.NeedsLogin == NeedsLogin.CheckIsNeeded) {
            VkTargetWebCrawler.getInstance()
                    .CheckIsLoginNeeded();
        }
        else if(Session.NeedsLogin == NeedsLogin.No) {
            // String currentEmail = session.getCurrentEmail();
            //  if(currentEmail != null && !currentEmail.equals("")){
            //       userEmailView.setText(currentEmail);
            //   }
            ShowTasks();
        }
    }

    private void ShowTasks() {
        NavigateTo(new TasksFragment(), false);
    }
    private void ShowFinishedTasks() {
        NavigateTo(new FinishedTasksFragment(), false);
    }

}
