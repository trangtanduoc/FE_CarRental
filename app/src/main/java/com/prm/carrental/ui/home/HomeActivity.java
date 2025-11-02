package com.prm.carrental.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.home.profile.ProfileFragment;
import com.prm.carrental.ui.home.rental.RentalHistoryFragment;
import com.prm.carrental.ui.home.station.StationListFragment;

/**
 * Hosts renter navigation between stations, rentals, and profile.
 */
public class HomeActivity extends BaseActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected int layoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.navigationStations);
            switchFragment(new StationListFragment());
        }
    }

    @Override
    protected void setupViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = resolveFragment(item.getItemId());
            if (fragment == null) {
                return false;
            }
            switchFragment(fragment);
            return true;
        });
    }

    private void switchFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.homeContainer, fragment)
            .commit();
    }

    @Nullable
    private Fragment resolveFragment(int itemId) {
        if (itemId == R.id.navigationStations) {
            return new StationListFragment();
        }
        if (itemId == R.id.navigationRentals) {
            return new RentalHistoryFragment();
        }
        if (itemId == R.id.navigationProfile) {
            return new ProfileFragment();
        }
        return null;
    }
}
