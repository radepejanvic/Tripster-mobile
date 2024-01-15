package com.example.tripster.fragment.accommodations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AvailabilityViewModel extends ViewModel {
    private final MutableLiveData<String> price;

    public AvailabilityViewModel() {
        price = new MutableLiveData<>();
        price.setValue("This is where the interval is displayed");
    }

    public LiveData<String> getText() {
        return price;
    }
}