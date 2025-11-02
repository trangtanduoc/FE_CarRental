package com.prm.carrental.ui.home.rental;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.prm.carrental.R;
import com.prm.carrental.core.ui.BaseActivity;

/**
 * Displays details of a historic rental.
 */
public class RentalDetailActivity extends BaseActivity {

    public static final String EXTRA_RENTAL_TITLE = "extra_rental_title";

    @Override
    protected int layoutId() {
        return R.layout.activity_rental_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView detailTitle = findViewById(R.id.rentalDetailTitle);
        TextView detailInfo = findViewById(R.id.rentalDetailInfo);
        String title = getIntent().getStringExtra(EXTRA_RENTAL_TITLE);
        detailTitle.setText(title != null ? title : getString(R.string.rental_history_title));
        detailInfo.setText("Thông tin chi tiết sẽ được hiển thị sau khi kết nối API");
    }
}
