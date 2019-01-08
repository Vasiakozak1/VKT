package com.example.admin.vktargetapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
