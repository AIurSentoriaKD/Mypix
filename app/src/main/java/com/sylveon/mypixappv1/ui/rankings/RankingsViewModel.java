package com.sylveon.mypixappv1.ui.rankings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public RankingsViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("Probando rankings con viewmodel");
    }
    public LiveData<String> getText(){ return mText; }
}