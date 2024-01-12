package com.courierservice.challenge.viewmodel

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.databinding.AddOfferLayoutBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(private val iOfferRepository: IOfferRepository)
    : ViewModel() {
    private var validatedObject = MutableLiveData<Pair<OfferEntity?,Boolean>>()
    val validationStatus: LiveData<Pair<OfferEntity?,Boolean>> = validatedObject
    fun addOffer(offerEntity: OfferEntity){
        viewModelScope.launch(Dispatchers.IO) {
            iOfferRepository.addOfferData(offerEntity)
        }
    }
    fun addOfferList(offerList: MutableList<OfferEntity>){
        viewModelScope.launch(Dispatchers.IO){
            if (offerList.isEmpty()) {
                val offer001 = OfferEntity("OFR001", 10.0, 0.0, 200.0, 70.0, 200.0)
                val offer002 = OfferEntity("OFR002", 7.0, 50.0, 150.0, 100.0, 250.0)
                val offer003 = OfferEntity("OFR003", 5.0, 50.0, 250.0, 10.0, 150.0)
                offerList.add(offer001)
                offerList.add(offer002)
                offerList.add(offer003)
            }
            iOfferRepository.addOfferListData(offerList)
        }
    }
    fun inputValidation(offerCode:String, percentage:String, minDiscount:String, maxDiscount:String, minWeight:String, maxWeight:String){
        if(offerCode.isEmpty() || percentage.isEmpty() || minDiscount.isEmpty() || maxDiscount.isEmpty()
                || minWeight.isEmpty() || maxWeight.isEmpty()){
            validatedObject.postValue(Pair(null,false))
            return
        }
        validatedObject.postValue(Pair(OfferEntity(offerCode,percentage.toDouble(),minDiscount.toDouble(),maxDiscount.toDouble(),minWeight.toDouble(),maxWeight.toDouble()),true))
    }
}