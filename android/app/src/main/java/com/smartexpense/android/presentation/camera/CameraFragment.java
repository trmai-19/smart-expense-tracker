package com.smartexpense.android.presentation.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.common.util.concurrent.ListenableFuture;
import com.smartexpense.android.databinding.FragmentCameraBinding;
import com.smartexpense.android.di.ViewModelFactory;
import com.smartexpense.android.presentation.camera.confirm.ConfirmActivity;
import com.smartexpense.android.presentation.history.ExpenseHistoryItem;
import com.smartexpense.android.presentation.history.ExpenseViewModel;
import com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto;

import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CameraFragment — Tab 2 in ViewPager2:
 * Pure Locket Timeline Feed (Page 0 = Camera Live View; Page 1..N = Fullscreen Photo Cards).
 */
public class CameraFragment extends Fragment implements TimelineFeedAdapter.FeedCameraListener {

    private FragmentCameraBinding binding;
    private TimelineFeedAdapter feedAdapter;

    private List<ExpenseHistoryItem> allItems = new ArrayList<>();
    private List<ExpenseHistoryItem> filteredItems = new ArrayList<>();
    private ExpenseViewModel viewModel;
    private String selectedCategory = "Tất cả";

    private ImageCapture imageCapture;
    private PreviewView cameraPreviewView;
    private ExecutorService cameraExecutor;
    private int cameraFacing = CameraSelector.LENS_FACING_BACK;

    public interface OnFeedPageChangeListener {
        void onFeedPageSelected(int position, String category);
    }

    private OnFeedPageChangeListener pageChangeListener;

    public void setOnFeedPageChangeListener(OnFeedPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    public interface OnCategoryChangeListener {
        void onCategoryChanged(String category);
    }

    private OnCategoryChangeListener categoryChangeListener;

    public void setOnCategoryChangeListener(OnCategoryChangeListener listener) {
        this.categoryChangeListener = listener;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                }
            });

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    navigateToConfirm(uri.toString());
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraExecutor = Executors.newSingleThreadExecutor();

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                allItems.clear();
                for (ExpenseResponseDto dto : expenses) {
                    String amountStr = String.format("%,.0f đ", dto.getAmount());
                    allItems.add(new ExpenseHistoryItem(dto.getId(), dto.getCaption(), amountStr, dto.getCategory(), dto.getExpenseDate(), dto.getPhotoUrl()));
                }
                filterCategory(selectedCategory);
            }
        });

        setupTimelineFeed();
        checkPermissionsAndStartCamera();
        
        viewModel.fetchExpenses();
    }

    // ─────────────────────────────────────────────
    // Timeline Feed Setup (Vertical ViewPager2)
    // ─────────────────────────────────────────────

    private void setupTimelineFeed() {
        feedAdapter = new TimelineFeedAdapter(requireContext(), filteredItems);
        feedAdapter.setCameraListener(this);

        binding.vpTimelineFeed.setAdapter(feedAdapter);
        binding.vpTimelineFeed.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        binding.vpTimelineFeed.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (pageChangeListener != null) {
                    pageChangeListener.onFeedPageSelected(position, selectedCategory);
                }
            }
        });
    }

    public void scrollToCamera() {
        if (binding == null) return;
        binding.vpTimelineFeed.setCurrentItem(0, true);
    }

    public void scrollToFeedPosition(int pos) {
        if (binding == null) return;
        binding.vpTimelineFeed.setCurrentItem(pos, false);
    }

    public int getCurrentFeedPosition() {
        return binding != null ? binding.vpTimelineFeed.getCurrentItem() : 0;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    // ─────────────────────────────────────────────
    // Category Filter Dialog
    // ─────────────────────────────────────────────

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

        if (feedAdapter != null) {
            feedAdapter.notifyDataSetChanged();
        }

        // Smoothly jump to first expense item (position 1) so user sees filtered result
        if (binding != null) {
            binding.vpTimelineFeed.post(() -> {
                if (binding != null) {
                    binding.vpTimelineFeed.setCurrentItem(1, false);
                    if (pageChangeListener != null) {
                        pageChangeListener.onFeedPageSelected(1, selectedCategory);
                    }
                }
            });
        }
    }

    // ─────────────────────────────────────────────
    // FeedCameraListener Callbacks
    // ─────────────────────────────────────────────

    @Override
    public void onShutterClicked() {
        takePhoto();
    }

    @Override
    public void onGalleryClicked() {
        pickImageLauncher.launch("image/*");
    }

    @Override
    public void onFlipCameraClicked() {
        cameraFacing = (cameraFacing == CameraSelector.LENS_FACING_BACK)
                ? CameraSelector.LENS_FACING_FRONT
                : CameraSelector.LENS_FACING_BACK;
        startCamera();
    }

    @Override
    public void onScrollToHistoryClicked() {
        if (binding != null && filteredItems.size() > 0) {
            binding.vpTimelineFeed.setCurrentItem(1, true);
        }
    }

    @Override
    public void onPreviewBound(PreviewView previewView) {
        this.cameraPreviewView = previewView;
        startCamera();
    }

    // ─────────────────────────────────────────────
    // CameraX Implementation
    // ─────────────────────────────────────────────

    private void checkPermissionsAndStartCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void startCamera() {
        if (getContext() == null || cameraPreviewView == null) return;
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (ExecutionException | InterruptedException ignored) {}
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void bindCameraUseCases(@NonNull ProcessCameraProvider cameraProvider) {
        if (cameraPreviewView == null) return;

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(cameraFacing)
                .build();

        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                    getViewLifecycleOwner(),
                    cameraSelector,
                    preview,
                    imageCapture
            );
        } catch (Exception ignored) {}
    }

    public void takePhoto() {
        if (imageCapture == null) {
            navigateToConfirm(null);
            return;
        }

        File photoFile = new File(
                requireContext().getCacheDir(),
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
        );

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = outputFileResults.getSavedUri() != null
                                ? outputFileResults.getSavedUri()
                                : Uri.fromFile(photoFile);
                        navigateToConfirm(savedUri.toString());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        navigateToConfirm(null);
                    }
                }
        );
    }

    private void navigateToConfirm(@Nullable String imageUri) {
        Intent intent = new Intent(requireContext(), ConfirmActivity.class);
        if (imageUri != null) {
            intent.putExtra("image_uri", imageUri);
        }
        startActivity(intent);
    }

    public void applyAccentColor() {
        if (feedAdapter != null) {
            feedAdapter.notifyDataSetChanged();
        }
    }

    // ─────────────────────────────────────────────
    // Sample Data
    // ─────────────────────────────────────────────

    private List<ExpenseHistoryItem> createSampleData() {
        return new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyAccentColor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
        binding = null;
    }
}
