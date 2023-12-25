package com.example.tripster;



import static androidx.navigation.ViewKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripster.databinding.FragmentAccommodationsBinding;
import com.example.tripster.databinding.FragmentAccountBinding;
import com.example.tripster.databinding.FragmentHomeBinding;
import com.example.tripster.databinding.FragmentNotificationsBinding;
import com.example.tripster.ui.notifications.NotificationsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsFragment extends Fragment {

    private FragmentAccommodationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccommodationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.hotel1012.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(R.id.action_navigation_home_to_singleAccommodationFragment);

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}