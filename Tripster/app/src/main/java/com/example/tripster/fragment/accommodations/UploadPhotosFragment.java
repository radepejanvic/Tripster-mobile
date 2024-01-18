package com.example.tripster.fragment.accommodations;

import static androidx.navigation.ViewKt.findNavController;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
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
import com.example.tripster.client.ClientUtils;
import com.example.tripster.databinding.FragmentUploadPhotosBinding;
import com.example.tripster.model.view.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPhotosFragment extends Fragment {
    private Long id;
    private String mode;
    private UploadPhotosViewModel mViewModel;
    private FragmentUploadPhotosBinding binding;
    private LinearLayout linearLayoutImages;
    private ScrollView scrollView;
    private List<Long> deletedPhoto = new ArrayList<>();
    private List<Uri> imagesAccommodation = new ArrayList<>();

    public static UploadPhotosFragment newInstance() {
        UploadPhotosFragment fragment = new UploadPhotosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong("id");
            mode = getArguments().getString("mode");
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button see = binding.buttonPhoto;
        Button upload = binding.uploadPhoto;
        linearLayoutImages = binding.line1;
        scrollView = binding.scrollPhoto;

        if (mode.equals("update")){

            Call<List<Photo>> photoCall = ClientUtils.accommodationService.getPhotos(id);

            photoCall.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {

                    if(response.code() == 200) {
                        addPhotos(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {

                }
            });
        }

        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(10), uris -> {
                    if (!uris.isEmpty()) {
                        if(uris.size() >= 5){
                            if (!mode.equals("update")){
                                linearLayoutImages.removeAllViews();
                                imagesAccommodation.clear();
                            }
                            for(Uri imageUri : uris){
                                imagesAccommodation.add(imageUri);
                                ImageView imageView = new ImageView(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(scrollView.getWidth(),  800);
                                layoutParams.setMargins(5, 10, 5, 5);
                                imageView.setLayoutParams(layoutParams);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setImageURI(imageUri);
                                imageView.setOnClickListener((v)->{

                                        linearLayoutImages.removeView(imageView);
                                        imagesAccommodation.remove(imageUri);
                                });

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
                List<MultipartBody.Part> images = convertImages();
                if (!mode.equals("update")){
                    if (images.size() >= 5){

                        postImages(id, images);
                    }else {
                        Toast.makeText(getContext(), "5 images are minimum", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    postImages(id, images);
                    deleteImages();
                }

            }
        });

        return root;
    }


    private void addPhotos(List<Photo> photos){

        for (Photo photo:photos){
            String base64Image = photo.getBytes();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(scrollView.getWidth(),  800);
            layoutParams.setMargins(5, 10, 5, 5);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(decodedBitmap);
            imageView.setOnClickListener((v)->{
                linearLayoutImages.removeView(imageView);
                deletedPhoto.add(photo.getId());
            });

            linearLayoutImages.addView(imageView);
        }
    }
    private void postImages(Long accommodationId, List<MultipartBody.Part> images){
        Call<Void> call = ClientUtils.accommodationService.uploadImages(images, accommodationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Log.d("IMAGES", "Successful");
                    if (!mode.equals("update")){
                        Toast.makeText(getActivity(), "Accommodation successful added", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(getView());
                        findNavController(getView()).navigate(R.id.action_uploadPhotosFragment_to_availabilityFragment);
                    }
                }else{
                    Log.d("IMAGES", "Some other code status");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("IMAGES", t.getMessage() != null?t.getMessage():"error");
                Log.d("IMAGES", "FAIL");
            }
        });
    }

    private void deleteImages(){
        Call<List<Long> > call = ClientUtils.accommodationService.deletePhotos(deletedPhoto);
        call.enqueue(new Callback<List<Long> >() {
            @Override
            public void onResponse(Call<List<Long> > call, Response<List<Long>> response) {
                if(response.code() == 200){
                    Log.d("IMAGES", "Successful");
                    Toast.makeText(getActivity(), "Accommodation successful added", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getView());
                    findNavController(getView()).navigate(R.id.action_uploadPhotosFragment_to_availabilityFragment);
                }else{
                    Log.d("IMAGES", "Some other code status");
                }
            }

            @Override
            public void onFailure(Call<List<Long> > call, Throwable t) {
                Toast.makeText(getActivity(), "Accommodation successful added", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(getView());
                findNavController(getView()).navigate(R.id.action_uploadPhotosFragment_to_availabilityFragment);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private  List<MultipartBody.Part> convertImages(){
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri imageUri : imagesAccommodation) {
            Log.d("IMAGES", imageUri.getEncodedPath());
            File file = new File(getRealPathFromUri(imageUri));
            if (file.exists()){
                RequestBody requestBody = RequestBody.create(MediaType.parse("photo/*"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
                imageParts.add(part);
            }

        }
        return imageParts;
    }

    public String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}