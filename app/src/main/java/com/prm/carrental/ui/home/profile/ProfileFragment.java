package com.prm.carrental.ui.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.di.ServiceLocator;
import com.prm.carrental.core.session.SessionManager;
import com.prm.carrental.core.ui.BaseFragment;
import com.prm.carrental.ui.auth.LoginActivity;

/**
 * Displays profile info and logout action.
 */
public class ProfileFragment extends BaseFragment {

    private SessionManager sessionManager;

    @Override
    protected int layoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = ServiceLocator.sessionManager();
        TextView profileName = view.findViewById(R.id.profileName);
        String fullName = sessionManager.getFullName();
        profileName.setText(fullName != null ? fullName : getString(R.string.profile_title));
        Button logout = view.findViewById(R.id.buttonLogout);
        logout.setOnClickListener(v -> {
            sessionManager.clear();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
