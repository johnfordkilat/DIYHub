package com.example.diyhub.Buyer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModelCat3 extends ViewModel {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();

    public void setData(String item)
    {
        selectedItem.setValue(item);
    }

    public LiveData<String> getSelectedItem()
    {
        return selectedItem;
    }
}
