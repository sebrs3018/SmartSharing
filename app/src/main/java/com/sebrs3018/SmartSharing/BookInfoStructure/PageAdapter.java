package com.sebrs3018.SmartSharing.BookInfoStructure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {


    ArrayList<String> tabNames = new ArrayList<>();
    List<Fragment> fragmentsList = new ArrayList<>();


    public PageAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }


    public void addFragment(Fragment fragment, String title){
        tabNames.add(title);
        fragmentsList.add(fragment);
    }



    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }
}
