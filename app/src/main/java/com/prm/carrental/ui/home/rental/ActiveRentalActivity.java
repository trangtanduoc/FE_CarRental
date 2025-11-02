package com.prm.carrental.ui.home.rental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.payment.PaymentActivity;

/**
 * Shows current rental with quick actions.
 */
public class ActiveRentalActivity extends BaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_active_rental;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button buttonNavigatePayment = findViewById(R.id.buttonNavigatePayment);
        buttonNavigatePayment.setOnClickListener(v ->
            startActivity(PaymentActivity.createIntent(this))
        );
    }
}
