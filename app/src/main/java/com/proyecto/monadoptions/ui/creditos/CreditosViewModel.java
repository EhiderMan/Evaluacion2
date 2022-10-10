package com.proyecto.monadoptions.ui.creditos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreditosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreditosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Acerca de mí!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}