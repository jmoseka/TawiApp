package com.mila.agrimo;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class AuthAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public AuthAdapter(FragmentManager fm, int behavior, Context context, int totalTabs) {
        super(fm, behavior);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    public AuthAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new LoginFragment();
            case 1: return new SignupFragment();

            default:return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if(position == 0){
            title = " Login";

        }

        if(position == 1){
            title = "Sign Up";
        }


        return title;
    }



}

