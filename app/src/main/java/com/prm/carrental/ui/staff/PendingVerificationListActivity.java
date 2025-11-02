package com.prm.carrental.ui.staff;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Displays users waiting for verification.
 */
public class PendingVerificationListActivity extends BaseActivity implements PendingVerificationListAdapter.Callback {

    @Override
    protected int layoutId() {
        return R.layout.activity_pending_verification_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.pendingUsersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PendingVerificationListAdapter(this));
    }

    @Override
    public void onUserSelected(String name) {
        Intent intent = new Intent(this, UserVerificationDetailActivity.class);
        intent.putExtra(UserVerificationDetailActivity.EXTRA_USER_NAME, name);
        startActivity(intent);
    }
}
