package com.example.tripster.fragment.accommodations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationFormViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AccommodationFormViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}