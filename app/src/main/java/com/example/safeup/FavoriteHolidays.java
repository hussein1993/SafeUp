package com.example.safeup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.safeup.adapters.HolidayEntryAdapter;
import com.example.safeup.models.HolidayModelItem;
import com.example.safeup.viewmodels.FavoriteHolidaysViewModel;
import com.example.safeup.viewmodels.HolidaysViewModel;

import java.util.ArrayList;

public class FavoriteHolidays extends Fragment {

    private FavoriteHolidaysViewModel mViewModel;

    private RecyclerView recyclerView;
    public static FavoriteHolidays newInstance() {
        return new FavoriteHolidays();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favorite_holidays_fragment, container, false);

        recyclerView = root.findViewById(R.id.fav_entries);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        HolidayEntryAdapter adapter = new HolidayEntryAdapter(getContext());

        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(requireActivity()).get(FavoriteHolidaysViewModel.class);
        mViewModel.getFavHolidays().observe(getActivity(), new Observer<ArrayList<HolidayModelItem>>() {
            @Override
            public void onChanged(ArrayList<HolidayModelItem> holidayModelItems) {
                if(holidayModelItems!= null) {
                    adapter.setList(holidayModelItems);
                }
            }
        });

        return root;
    }

}