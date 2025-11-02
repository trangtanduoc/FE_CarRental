package com.prm.carrental.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;
import com.prm.carrental.ui.home.HomeActivity;

/**
 * Displays confirmation after payment.
 */
public class PaymentSuccessActivity extends BaseActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_payment_success;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button buttonBackHome = findViewById(R.id.buttonBackHome);
        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
