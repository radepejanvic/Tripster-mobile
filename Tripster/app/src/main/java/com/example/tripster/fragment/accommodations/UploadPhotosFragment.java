package com.example.tripster.fragment.accommodations;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.tripster.R;
import com.example.tripster.databinding.FragmentUploadPhotosBinding;

import java.util.ArrayList;
import java.util.List;

public class UploadPhotosFragment extends Fragment {

    private UploadPhotosViewModel mViewModel;
    private FragmentUploadPhotosBinding binding;
    private List<Uri> imagesAccommodation = new ArrayList<>();

    public static UploadPhotosFragment newInstance() {
        return new UploadPhotosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button see = binding.buttonPhoto;
        Button upload = binding.uploadPhoto;
        LinearLayout linearLayoutImages = binding.line1;
        ScrollView scrollView = binding.scrollPhoto;

        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                    if (!uris.isEmpty()) {
                        if(uris.size() >= 5){
                            linearLayoutImages.removeAllViews();
                            imagesAccommodation.clear();
                            for(Uri imageUri : uris){
                                Log.d("URI", imageUri.toString());
                                imagesAccommodation.add(imageUri);
                                ImageView imageView = new ImageView(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(scrollView.getWidth(),  800);
                                layoutParams.setMargins(5, 10, 5, 5);
                                imageView.setLayoutParams(layoutParams);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setImageURI(imageUri);

                                linearLayoutImages.addView(imageView);
                            }
                        }else {
                            Toast.makeText(getContext(), "5 images are minimum", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UploadPhotosViewModel.class);
        // TODO: Use the ViewModel
    }

}