package com.prm.carrental.core.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Common activity behaviour for simple toast-based error handling.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int layoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        setupViews();
        observeViewModel();
    }

    protected void setupViews() {
        // Default empty implementation
    }

    protected void observeViewModel() {
        // Default empty implementation
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
