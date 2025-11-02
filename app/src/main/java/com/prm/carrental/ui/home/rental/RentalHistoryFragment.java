package com.prm.carrental.ui.home.rental;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseFragment;

/**
 * Displays rental history list.
 */
public class RentalHistoryFragment extends BaseFragment {

    @Override
    protected int layoutId() {
        return R.layout.fragment_rental_history;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.rentalHistoryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new RentalPlaceholderAdapter());
    }
}
