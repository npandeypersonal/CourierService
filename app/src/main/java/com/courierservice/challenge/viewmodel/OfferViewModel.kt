package com.courierservice.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.repositories.IOfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(private val iOfferRepository: IOfferRepository)
    : ViewModel() {
    fun addOffer(offerEntity: OfferEntity){
        viewModelScope.launch(Dispatchers.IO) {
            iOfferRepository.addOfferData(offerEntity)
        }
    }
    fun addOfferList(offerList: List<OfferEntity>){
        viewModelScope.launch(Dispatchers.IO){
            iOfferRepository.addOfferListData(offerList)
        }
    }
}