package com.example.admin.vktargetapp;

import android.support.v4.app.Fragment;

public interface NavigationHost {

    void NavigateTo(Fragment fragment, boolean addTobackstck);
}
