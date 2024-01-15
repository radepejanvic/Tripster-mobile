package com.example.tripster.fragment.accommodations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AccommodationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is accommodations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}