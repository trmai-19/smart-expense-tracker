package com.smartexpense.android.presentation.dashboard;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.smartexpense.android.R;
import com.smartexpense.android.databinding.DialogDateRangePickerBinding;
import com.smartexpense.android.databinding.FragmentDashboardBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    public enum TimeFilter {
        WEEK, MONTH, YEAR, CUSTOM
    }

    private FragmentDashboardBinding binding;
    private TimeFilter currentFilter = TimeFilter.MONTH;
    private String customDateRangeLabel = "Tùy chọn 📅";
    private String customDateRangeDetail = "Chi tiêu trong khoảng đã chọn";

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Default start date = 7 days ago
        startCalendar.add(Calendar.DAY_OF_MONTH, -7);

        setupFilterListeners();
        updateFilterUI();
        applyAccentColor();
    }

    public void applyAccentColor() {
        if (getContext() == null || binding == null) return;
        int accentColor = ThemeManager.getAccentColorInt(requireContext());
        binding.tvTotalSpent.setTextColor(accentColor);
        updateFilterUI();

        // Tint chart bars
        binding.barDay1.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay2.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay3.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay4.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay5.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay6.setBackgroundTintList(ColorStateList.valueOf(accentColor));
        binding.barDay7.setBackgroundTintList(ColorStateList.valueOf(accentColor));
    }

    private void setupFilterListeners() {
        binding.btnFilterWeek.setOnClickListener(v -> setTimeFilter(TimeFilter.WEEK));
        binding.btnFilterMonth.setOnClickListener(v -> setTimeFilter(TimeFilter.MONTH));
        binding.btnFilterYear.setOnClickListener(v -> setTimeFilter(TimeFilter.YEAR));
        binding.btnFilterCustom.setOnClickListener(v -> showDateRangePopup());
    }

    private void setTimeFilter(TimeFilter filter) {
        this.currentFilter = filter;
        if (filter != TimeFilter.CUSTOM) {
            customDateRangeLabel = "Tùy chọn 📅";
        }
        if (binding != null) {
            binding.btnFilterCustom.setText(customDateRangeLabel);
        }
        updateFilterUI();
        updateDashboardData();
    }

    private void updateFilterUI() {
        if (getContext() == null || binding == null) return;
        int accentColor = ThemeManager.getAccentColorInt(requireContext());

        resetFilterButton(binding.btnFilterWeek);
        resetFilterButton(binding.btnFilterMonth);
        resetFilterButton(binding.btnFilterYear);
        resetFilterButton(binding.btnFilterCustom);

        TextView activeBtn = null;
        switch (currentFilter) {
            case WEEK:
                activeBtn = binding.btnFilterWeek;
                break;
            case MONTH:
                activeBtn = binding.btnFilterMonth;
                break;
            case YEAR:
                activeBtn = binding.btnFilterYear;
                break;
            case CUSTOM:
                activeBtn = binding.btnFilterCustom;
                break;
        }

        if (activeBtn != null) {
            activeBtn.setBackgroundTintList(ColorStateList.valueOf(accentColor));
            activeBtn.setTextColor(Color.BLACK);
        }
    }

    private void resetFilterButton(TextView btn) {
        btn.setBackgroundTintList(null);
        btn.setTextColor(getResources().getColor(R.color.text_secondary, null));
    }

    private void updateDashboardData() {
        if (binding == null) return;

        switch (currentFilter) {
            case WEEK:
                binding.tvTotalPeriodLabel.setText("Tổng chi tiêu tuần này");
                binding.tvTotalSpent.setText("320.000 ₫");
                binding.tvComparisonBadge.setText("↓ 15% so với tuần trước");
                binding.tvCategoryFoodVal.setText("180.000 ₫");
                binding.tvCategoryTransportVal.setText("60.000 ₫");
                binding.tvCategoryShoppingVal.setText("40.000 ₫");
                binding.tvCategoryOtherVal.setText("40.000 ₫");
                break;
            case MONTH:
                binding.tvTotalPeriodLabel.setText("Tổng chi tiêu tháng này");
                binding.tvTotalSpent.setText("1.250.000 ₫");
                binding.tvComparisonBadge.setText("↓ 12% so với tháng trước");
                binding.tvCategoryFoodVal.setText("562.500 ₫");
                binding.tvCategoryTransportVal.setText("250.000 ₫");
                binding.tvCategoryShoppingVal.setText("187.500 ₫");
                binding.tvCategoryOtherVal.setText("250.000 ₫");
                break;
            case YEAR:
                binding.tvTotalPeriodLabel.setText("Tổng chi tiêu năm 2026");
                binding.tvTotalSpent.setText("15.800.000 ₫");
                binding.tvComparisonBadge.setText("↓ 8% so với năm 2025");
                binding.tvCategoryFoodVal.setText("7.110.000 ₫");
                binding.tvCategoryTransportVal.setText("3.160.000 ₫");
                binding.tvCategoryShoppingVal.setText("2.370.000 ₫");
                binding.tvCategoryOtherVal.setText("3.160.000 ₫");
                break;
            case CUSTOM:
                binding.tvTotalPeriodLabel.setText(customDateRangeDetail);
                binding.tvTotalSpent.setText("850.000 ₫");
                binding.tvComparisonBadge.setText("Đã lọc theo khoảng ngày đã chọn");
                binding.tvCategoryFoodVal.setText("380.000 ₫");
                binding.tvCategoryTransportVal.setText("190.000 ₫");
                binding.tvCategoryShoppingVal.setText("130.000 ₫");
                binding.tvCategoryOtherVal.setText("150.000 ₫");
                break;
        }
    }

    /**
     * Compact Popup Dialog for selecting Start Date and End Date.
     * Uses native compact DatePickerDialog with Year -> Month -> Day layered navigation.
     */
    private void showDateRangePopup() {
        if (getContext() == null) return;

        DialogDateRangePickerBinding dialogBinding = DialogDateRangePickerBinding.inflate(getLayoutInflater());
        int accentColor = ThemeManager.getAccentColorInt(requireContext());

        SimpleDateFormat fullSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat shortSdf = new SimpleDateFormat("dd/MM", Locale.getDefault());

        dialogBinding.tvStartDateVal.setText(fullSdf.format(startCalendar.getTime()));
        dialogBinding.tvEndDateVal.setText(fullSdf.format(endCalendar.getTime()));
        dialogBinding.btnDialogApply.setBackgroundTintList(ColorStateList.valueOf(accentColor));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Pick Start Date (Layer: Year -> Month -> Day)
        dialogBinding.btnSelectStartDate.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        startCalendar.set(Calendar.YEAR, year);
                        startCalendar.set(Calendar.MONTH, month);
                        startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dialogBinding.tvStartDateVal.setText(fullSdf.format(startCalendar.getTime()));
                    },
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });

        // Pick End Date (Layer: Year -> Month -> Day)
        dialogBinding.btnSelectEndDate.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        endCalendar.set(Calendar.YEAR, year);
                        endCalendar.set(Calendar.MONTH, month);
                        endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dialogBinding.tvEndDateVal.setText(fullSdf.format(endCalendar.getTime()));
                    },
                    endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),
                    endCalendar.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });

        // Cancel
        dialogBinding.btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

        // Apply
        dialogBinding.btnDialogApply.setOnClickListener(v -> {
            if (startCalendar.after(endCalendar)) {
                Toast.makeText(requireContext(), "Ngày bắt đầu không thể sau ngày kết thúc!", Toast.LENGTH_SHORT).show();
                return;
            }

            String startStr = fullSdf.format(startCalendar.getTime());
            String endStr = fullSdf.format(endCalendar.getTime());
            customDateRangeLabel = shortSdf.format(startCalendar.getTime()) + " - " + shortSdf.format(endCalendar.getTime());
            customDateRangeDetail = "Từ ngày " + startStr + " đến " + endStr;

            dialog.dismiss();
            setTimeFilter(TimeFilter.CUSTOM);
            Toast.makeText(requireContext(), "Đã lọc: " + customDateRangeDetail, Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyAccentColor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
