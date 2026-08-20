package com.smartexpense.android.presentation.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.smartexpense.android.databinding.FragmentWidgetGridBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * WidgetGridFragment — Tab 0 in ViewPager2:
 * Displays a 3-column photo grid of all expense widgets.
 * Tapping an item navigates directly to that full-size card in CameraFragment's Timeline Feed.
 */
public class WidgetGridFragment extends Fragment {

    private FragmentWidgetGridBinding binding;
    private HistoryGridAdapter gridAdapter;
    private List<ExpenseHistoryItem> allItems;
    private List<ExpenseHistoryItem> filteredItems;
    private String selectedCategory = "Tất cả";

    public interface OnWidgetGridItemClickListener {
        void onWidgetGridItemClicked(int position);
    }

    private OnWidgetGridItemClickListener itemClickListener;

    public void setOnWidgetGridItemClickListener(OnWidgetGridItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnCategoryChangeListener {
        void onCategoryChanged(String category);
    }

    private OnCategoryChangeListener categoryChangeListener;

    public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
        this.categoryChangeListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWidgetGridBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private com.smartexpense.android.presentation.history.ExpenseViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allItems = new ArrayList<>();
        filteredItems = new ArrayList<>();

        viewModel = new androidx.lifecycle.ViewModelProvider(this, com.smartexpense.android.di.ViewModelFactory.getInstance()).get(com.smartexpense.android.presentation.history.ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                allItems.clear();
                for (com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto dto : expenses) {
                    String amountStr = String.format("%,.0f đ", dto.getAmount());
                    String formattedDate = dto.getExpenseDate();
                    try {
                        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
                        java.util.Date date = inputFormat.parse(dto.getExpenseDate());
                        if (date != null) {
                            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy 'lúc' HH:mm", new java.util.Locale("vi", "VN"));
                            formattedDate = outputFormat.format(date);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    allItems.add(new ExpenseHistoryItem(dto.getId(), dto.getCaption(), amountStr, dto.getCategory(), formattedDate, dto.getPhotoUrl()));
                }
                filterCategory(selectedCategory);
            }
        });
        
        viewModel.fetchExpenses();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        gridAdapter = new HistoryGridAdapter(filteredItems);
        gridAdapter.setOnItemClickListener(position -> {
            if (itemClickListener != null) {
                itemClickListener.onWidgetGridItemClicked(position);
            }
        });

        binding.rvWidgetGrid.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.rvWidgetGrid.setAdapter(gridAdapter);
    }

    public void showCategoryFilterDialog() {
        if (getContext() == null) return;
        String[] categories = {"Tất cả", "Ăn uống", "Di chuyển", "Mua sắm", "Hóa đơn", "Giải trí"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Lọc danh mục chi tiêu")
                .setItems(categories, (dialog, which) -> {
                    selectedCategory = categories[which];
                    filterCategory(selectedCategory);
                    if (categoryChangeListener != null) {
                        categoryChangeListener.onCategoryChanged(selectedCategory);
                    }
                })
                .show();
    }

    public void filterCategory(String category) {
        this.selectedCategory = category;
        filteredItems.clear();
        if ("Tất cả".equals(category)) {
            filteredItems.addAll(allItems);
        } else {
            for (ExpenseHistoryItem item : allItems) {
                if (category.equals(item.getCategory())) {
                    filteredItems.add(item);
                }
            }
        }

        if (filteredItems.isEmpty()) {
            filteredItems.add(new ExpenseHistoryItem("empty", "Chưa có chi tiêu " + category + " nào 📁", "0 ₫", category, "Mới", null));
        }

        if (gridAdapter != null) {
            gridAdapter.notifyDataSetChanged();
        }
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void applyAccentColor() {
        if (gridAdapter != null) {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private List<ExpenseHistoryItem> createSampleData() {
        List<ExpenseHistoryItem> list = new ArrayList<>();
        list.add(new ExpenseHistoryItem("1", "Tô bún bò huế 50k nhé 🍜", "50.000 ₫", "Ăn uống", "Hôm nay lúc 12:30", null));
        list.add(new ExpenseHistoryItem("2", "Cà phê sữa đá sáng nay ☕", "25.000 ₫", "Ăn uống", "Hôm nay lúc 08:15", null));
        list.add(new ExpenseHistoryItem("3", "Đổ xăng xe máy Petrolimex 🛵", "70.000 ₫", "Di chuyển", "Hôm qua lúc 17:45", null));
        list.add(new ExpenseHistoryItem("4", "Mua áo phông Uniqlo 👕", "199.000 ₫", "Mua sắm", "2 ngày trước", null));
        list.add(new ExpenseHistoryItem("5", "Grab đi siêu thị 🚗", "35.000 ₫", "Di chuyển", "2 ngày trước", null));
        list.add(new ExpenseHistoryItem("6", "Bữa trưa văn phòng 🍱", "45.000 ₫", "Ăn uống", "3 ngày trước", null));
        list.add(new ExpenseHistoryItem("7", "Tiền điện tháng này ⚡", "450.000 ₫", "Hóa đơn", "4 ngày trước", null));
        list.add(new ExpenseHistoryItem("8", "Vé xem phim CGV cuối tuần 🎬", "120.000 ₫", "Giải trí", "5 ngày trước", null));
        list.add(new ExpenseHistoryItem("9", "Trà sữa Koi Thé 🧋", "65.000 ₫", "Ăn uống", "6 ngày trước", null));
        list.add(new ExpenseHistoryItem("10", "Mua sách kỹ năng mềm 📚", "135.000 ₫", "Mua sắm", "1 tuần trước", null));
        list.add(new ExpenseHistoryItem("11", "Tiền nước sinh hoạt 💧", "85.000 ₫", "Hóa đơn", "1 tuần trước", null));
        list.add(new ExpenseHistoryItem("12", "Cà phê boardgame cùng bạn bè 🎲", "60.000 ₫", "Giải trí", "1 tuần trước", null));
        list.add(new ExpenseHistoryItem("13", "Bảo dưỡng thay nhớt xe 🔧", "110.000 ₫", "Di chuyển", "2 tuần trước", null));
        return list;
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
