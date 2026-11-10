package com.example.feechecker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout tilBaggageWeight;
    private TextInputEditText etBaggageWeight;
    private RadioGroup rgTicketClass;
    private RadioButton rbEconomy;
    private RadioButton rbBusiness;
    private TextInputLayout tilRatePerKg;
    private TextInputEditText etRatePerKg;

    private TextView tvExcessWeightValue;
    private TextView tvExcessChargeValue;
    private TextView tvServiceTaxValue;
    private TextView tvTotalPayableValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilBaggageWeight = findViewById(R.id.tilBaggageWeight);
        etBaggageWeight = findViewById(R.id.etBaggageWeight);
        rgTicketClass = findViewById(R.id.rgTicketClass);
        rbEconomy = findViewById(R.id.rbEconomy);
        rbBusiness = findViewById(R.id.rbBusiness);
        tilRatePerKg = findViewById(R.id.tilRatePerKg);
        etRatePerKg = findViewById(R.id.etRatePerKg);

        tvExcessWeightValue = findViewById(R.id.tvExcessWeightValue);
        tvExcessChargeValue = findViewById(R.id.tvExcessChargeValue);
        tvServiceTaxValue = findViewById(R.id.tvServiceTaxValue);
        tvTotalPayableValue = findViewById(R.id.tvTotalPayableValue);
    }

    private void setupListeners() {
        MaterialButton btnCalculate = findViewById(R.id.btnCalculate);
        MaterialButton btnClear = findViewById(R.id.btnClear);

        if (btnCalculate != null) {
            btnCalculate.setOnClickListener(v -> calculateFee());
        }

        if (btnClear != null) {
            btnClear.setOnClickListener(v -> clearInputs());
        }
    }

    private void calculateFee() {
        tilBaggageWeight.setError(null);
        tilRatePerKg.setError(null);

        String weightStr = etBaggageWeight.getText() != null ? etBaggageWeight.getText().toString().trim() : "";
        String rateStr = etRatePerKg.getText() != null ? etRatePerKg.getText().toString().trim() : "";

        boolean isValid = true;
        double baggageWeight = 0.0;
        double ratePerKg = 0.0;

        if (TextUtils.isEmpty(weightStr)) {
            tilBaggageWeight.setError(getString(R.string.err_empty_weight));
            isValid = false;
        } else {
            try {
                baggageWeight = Double.parseDouble(weightStr);
                if (baggageWeight < 0) {
                    tilBaggageWeight.setError(getString(R.string.err_invalid_weight));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilBaggageWeight.setError(getString(R.string.err_invalid_weight));
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(rateStr)) {
            tilRatePerKg.setError(getString(R.string.err_empty_rate));
            isValid = false;
        } else {
            try {
                ratePerKg = Double.parseDouble(rateStr);
                if (ratePerKg < 0) {
                    tilRatePerKg.setError(getString(R.string.err_invalid_rate));
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilRatePerKg.setError(getString(R.string.err_invalid_rate));
                isValid = false;
            }
        }

        if (!isValid) {
            return;
        }

        boolean isBusinessClass = rbBusiness.isChecked();

        FeeCalculator.Result result = FeeCalculator.calculate(baggageWeight, isBusinessClass, ratePerKg);

        tvExcessWeightValue.setText(String.format(Locale.US, "%.2f kg", result.getExcessWeight()));
        tvExcessChargeValue.setText(String.format(Locale.US, "RM %.2f", result.getExcessCharge()));
        tvServiceTaxValue.setText(String.format(Locale.US, "RM %.2f", result.getServiceTax()));
        tvTotalPayableValue.setText(String.format(Locale.US, "RM %.2f", result.getTotalPayable()));

        Toast.makeText(this, "Calculation updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void clearInputs() {
        if (etBaggageWeight != null) {
            etBaggageWeight.setText("");
        }
        if (etRatePerKg != null) {
            etRatePerKg.setText("");
        }
        if (tilBaggageWeight != null) {
            tilBaggageWeight.setError(null);
        }
        if (tilRatePerKg != null) {
            tilRatePerKg.setError(null);
        }

        if (rbEconomy != null) {
            rbEconomy.setChecked(true);
        }

        tvExcessWeightValue.setText(getString(R.string.default_weight));
        tvExcessChargeValue.setText(getString(R.string.default_currency));
        tvServiceTaxValue.setText(getString(R.string.default_currency));
        tvTotalPayableValue.setText(getString(R.string.default_currency));

        if (etBaggageWeight != null) {
            etBaggageWeight.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            shareApplication();
            return true;
        } else if (itemId == R.id.action_about) {
            openAboutPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareApplication() {
        String shareText = getString(R.string.share_text);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
    }

    private void openAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}