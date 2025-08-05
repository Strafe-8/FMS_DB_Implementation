package com.example.farmingmanagemengsystempartial;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GrowthFragment();
            case 1:
                return new FeedsFragment(); // Ensure this class exists
            case 2:
                return new MortalityFragment(); // Ensure this class exists
            default:
                return new GrowthFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}