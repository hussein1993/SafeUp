package com.example.safeup.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.safeup.FavoriteHolidays;
import com.example.safeup.HolidaysFragment;

public class VpAdapter extends FragmentStateAdapter {
    private String[] titles = {"Home","Cart"};
    public VpAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HolidaysFragment();
            case 1:
                return new FavoriteHolidays();
        }
        return new HolidaysFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
