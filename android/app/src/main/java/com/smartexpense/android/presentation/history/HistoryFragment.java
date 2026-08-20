package com.smartexpense.android.presentation.history;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import com.smartexpense.android.databinding.FragmentHistoryBinding;
import com.smartexpense.android.presentation.util.ThemeManager;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.ViewModelProvider;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;
import com.smartexpense.android.di.ViewModelFactory;
import com.smartexpense.android.presentation.history.ExpenseViewModel;

public class HistoryFragment extends Fragment {

    private static final String ARG_START_GRID = "arg_start_grid";

    private FragmentHistoryBinding binding;
    private HistoryFullscreenAdapter fullscreenAdapter;
    private HistoryGridAdapter gridAdapter;
    private List<ExpenseHistoryItem> allItems;
    private List<ExpenseHistoryItem> filteredItems;

    private ExpenseViewModel viewModel;

    private ScaleGestureDetector scaleGestureDetector;
    private boolean isGridMode = false;
    private String selectedCategory = "Táº¥t cáº£";

    public interface OnCloseHistoryListener {
        void onCloseHistory();
    }

    private OnCloseHistoryListener closeListener;

    public void setOnCloseHistoryListener(OnCloseHistoryListener listener) {
        this.closeListener = listener;
    }

    public static HistoryFragment newInstance(boolean startInGridMode) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_START_GRID, startInGridMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allItems = new ArrayList<>();
        filteredItems = new ArrayList<>();

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                allItems.clear();
                for (ExpenseResponseDto dto : expenses) {
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
                filterData(selectedCategory);
            }
        });
        viewModel.fetchExpenses();

        setupFullscreenPager();
        setupGridView();
        setupScaleGesture();
        setupListeners();
        applyAccentTint();

        boolean startGrid = getArguments() != null && getArguments().getBoolean(ARG_START_GRID, false);
        if (startGrid) {
            showGrid();
        } else {
            showFullscreen(0);
        }
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // Setup
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void setupFullscreenPager() {
        fullscreenAdapter = new HistoryFullscreenAdapter(requireContext(), filteredItems);
        binding.vpHistoryFullscreen.setAdapter(fullscreenAdapter);
        binding.vpHistoryFullscreen.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        binding.vpHistoryFullscreen.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateIndexLabel(position, filteredItems.size());
            }
        });
    }

    private void setupGridView() {
        gridAdapter = new HistoryGridAdapter(filteredItems);
        gridAdapter.setOnItemClickListener(position -> {
            // Tap a grid cell -> open fullscreen timeline at that position
            showFullscreen(position);
        });

        binding.rvHistoryGrid.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.rvHistoryGrid.setAdapter(gridAdapter);

        if (filteredItems.isEmpty()) {
            binding.layoutEmptyHistory.setVisibility(View.VISIBLE);
            binding.rvHistoryGrid.setVisibility(View.GONE);
        }
    }

    private void setupScaleGesture() {
        scaleGestureDetector = new ScaleGestureDetector(requireContext(),
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        // Detect pinch-in to switch to grid mode (Locket feature)
                        if (detector.getScaleFactor() < 0.85f && !isGridMode) {
                            showGrid();
                            return true;
                        }
                        return false;
                    }
                });

        binding.layoutFullscreen.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return false;
        });
    }

    private void setupListeners() {
        binding.btnCloseFullscreen.setOnClickListener(v -> closeHistory());
        binding.btnToggleGrid.setOnClickListener(v -> showGrid());
        binding.btnCloseGrid.setOnClickListener(v -> {
            if (isGridMode) {
                showFullscreen(0);
            } else {
                closeHistory();
            }
        });

        binding.btnFilterDropdown.setOnClickListener(v -> showCategoryFilterDialog());
    }

    private void showCategoryFilterDialog() {
        String[] categories = {"Táº¥t cáº£", "Ä‚n uá»‘ng", "Di chuyá»ƒn", "Mua sáº¯m", "HÃ³a Ä‘Æ¡n", "Giáº£i trÃ­"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Lá»c danh má»¥c chi tiÃªu")
                .setItems(categories, (dialog, which) -> {
                    selectedCategory = categories[which];
                    binding.tvFilterTitle.setText(selectedCategory + " â–¼");
                    filterData(selectedCategory);
                })
                .show();
    }

    private void filterData(String category) {
        filteredItems.clear();
        if ("Táº¥t cáº£".equals(category)) {
            filteredItems.addAll(allItems);
        } else {
            for (ExpenseHistoryItem item : allItems) {
                if (category.equals(item.getCategory())) {
                    filteredItems.add(item);
                }
            }
        }
        fullscreenAdapter.notifyDataSetChanged();
        gridAdapter.notifyDataSetChanged();

        if (filteredItems.isEmpty()) {
            binding.layoutEmptyHistory.setVisibility(View.VISIBLE);
            binding.rvHistoryGrid.setVisibility(View.GONE);
            updateIndexLabel(0, 0);
        } else {
            binding.layoutEmptyHistory.setVisibility(View.GONE);
            binding.rvHistoryGrid.setVisibility(View.VISIBLE);
            if (!isGridMode) {
                showFullscreen(0);
            }
        }
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // Mode Switching: Fullscreen <-> Grid
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public void showFullscreen(int startPosition) {
        isGridMode = false;

        binding.layoutGrid.setVisibility(View.GONE);
        binding.layoutFullscreen.setVisibility(View.VISIBLE);
        binding.layoutFullscreen.setAlpha(0f);
        binding.layoutFullscreen.setScaleX(0.92f);
        binding.layoutFullscreen.setScaleY(0.92f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(binding.layoutFullscreen, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.layoutFullscreen, "scaleX", 0.92f, 1f),
                ObjectAnimator.ofFloat(binding.layoutFullscreen, "scaleY", 0.92f, 1f)
        );
        set.setDuration(260);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

        int targetPos = Math.max(0, Math.min(startPosition, filteredItems.size() - 1));
        binding.vpHistoryFullscreen.setCurrentItem(targetPos, false);
        updateIndexLabel(targetPos, filteredItems.size());
        applyAccentTint();
    }

    public void showGrid() {
        isGridMode = true;

        binding.layoutFullscreen.setVisibility(View.GONE);
        binding.layoutGrid.setVisibility(View.VISIBLE);
        binding.layoutGrid.setAlpha(0f);
        binding.layoutGrid.setScaleX(1.08f);
        binding.layoutGrid.setScaleY(1.08f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(binding.layoutGrid, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.layoutGrid, "scaleX", 1.08f, 1f),
                ObjectAnimator.ofFloat(binding.layoutGrid, "scaleY", 1.08f, 1f)
        );
        set.setDuration(260);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    private void closeHistory() {
        if (closeListener != null) {
            closeListener.onCloseHistory();
        } else if (getParentFragmentManager() != null) {
            getParentFragmentManager().popBackStack();
        }
    }

    private void updateIndexLabel(int current, int total) {
        if (total > 0) {
            binding.tvFullscreenIndex.setText((current + 1) + " / " + total);
        } else {
            binding.tvFullscreenIndex.setText("0 / 0");
        }
    }

    private void applyAccentTint() {
        if (getContext() == null || binding == null) return;
        int accentColor = ThemeManager.getAccentColorInt(requireContext());
        binding.tvPinchHint.setTextColor(accentColor);
    }

    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    // Sample Data
    // â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private List<ExpenseHistoryItem> createSampleData() {
        List<ExpenseHistoryItem> list = new ArrayList<>();
        list.add(new ExpenseHistoryItem("1", "TÃ´ bÃºn bÃ² huáº¿ 50k nhÃ© ðŸœ", "50.000 â‚«", "Ä‚n uá»‘ng", "HÃ´m nay lÃºc 12:30", null));
        list.add(new ExpenseHistoryItem("2", "CÃ  phÃª sá»¯a Ä‘Ã¡ sÃ¡ng nay â˜•", "25.000 â‚«", "Ä‚n uá»‘ng", "HÃ´m nay lÃºc 08:15", null));
        list.add(new ExpenseHistoryItem("3", "Äá»• xÄƒng xe mÃ¡y Petrolimex ðŸ›µ", "70.000 â‚«", "Di chuyá»ƒn", "HÃ´m qua lÃºc 17:45", null));
        list.add(new ExpenseHistoryItem("4", "Mua Ã¡o phÃ´ng Uniqlo ðŸ‘•", "199.000 â‚«", "Mua sáº¯m", "2 ngÃ y trÆ°á»›c", null));
        list.add(new ExpenseHistoryItem("5", "Grab Ä‘i siÃªu thá»‹ ðŸš—", "35.000 â‚«", "Di chuyá»ƒn", "2 ngÃ y trÆ°á»›c", null));
        list.add(new ExpenseHistoryItem("6", "Bá»¯a trÆ°a vÄƒn phÃ²ng ðŸ±", "45.000 â‚«", "Ä‚n uá»‘ng", "3 ngÃ y trÆ°á»›c", null));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

