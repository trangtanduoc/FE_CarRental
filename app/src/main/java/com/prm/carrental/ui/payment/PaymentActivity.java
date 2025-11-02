package com.prm.carrental.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Handles payment confirmation for a rental.
 */
public class PaymentActivity extends BaseActivity {

    private static final String EXTRA_AMOUNT = "extra_amount";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(EXTRA_AMOUNT, 150000); // demo value
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_payment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView amountView = findViewById(R.id.paymentAmount);
        Button confirmButton = findViewById(R.id.buttonConfirmPayment);

        int amount = getIntent().getIntExtra(EXTRA_AMOUNT, 0);
        amountView.setText("Số tiền: " + amount + "đ");

        confirmButton.setOnClickListener(v -> {
            showMessage("Thanh toán demo thành công");
            startActivity(new Intent(this, PaymentSuccessActivity.class));
            finish();
        });
    }
}
