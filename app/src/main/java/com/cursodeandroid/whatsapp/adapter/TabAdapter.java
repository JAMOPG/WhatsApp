package com.cursodeandroid.whatsapp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cursodeandroid.whatsapp.fragment.ContactsFragment;
import com.cursodeandroid.whatsapp.fragment.TalkFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] titletabs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){

            case 0:
                fragment = new TalkFragment();
                break;

            case 1:
                fragment = new ContactsFragment();
        }


        return fragment;
    }

    @Override
    public int getCount() {
        return titletabs.length;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titletabs [position];
    }
}
