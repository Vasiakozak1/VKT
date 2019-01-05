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
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Login();
                }
                catch (IOException e){
                    TextView tw = findViewById(R.id.textView);
                    tw.setText(e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                }
                catch (Exception e) {
                    TextView tw = findViewById(R.id.textView);
                    tw.setText(e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                }

            }
        });
    }

    private void Login() throws IOException {

        getFragmentManager()
                .beginTransaction()
                .add(R.id.appContainer, new CustomWebViewFragment())
                .commit();
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
