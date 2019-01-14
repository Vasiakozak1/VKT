package com.example.admin.vktargetapp;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.vktargetapp.com.example.admin.vktargetapp.models.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationHost {
    private DrawerLayout appNavigationDrawer;
    private NavigationView appNavigationView;
    private Toolbar appToolbar;

    public static WebView WebCrawlerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebCrawlerView = findViewById(R.id.webCrawlerView);
        VkTargetApplication.setCurrentActivity(this);
        appNavigationDrawer = findViewById(R.id.drawer_layout);
        appNavigationView = findViewById(R.id.nav_view);
        appToolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(appToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        appNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                appNavigationDrawer.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });
        try{
            if(UserData.APIKey == null) {
                Login();
            }
            else {
                //Menu
            }
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


}
