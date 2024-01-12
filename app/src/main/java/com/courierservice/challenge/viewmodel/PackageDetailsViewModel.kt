package com.courierservice.challenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.repositories.IPackageDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PackageDetailsViewModel @Inject constructor(private val iPackageDetailsRepository: IPackageDetailsRepository)
    : ViewModel() {
    private var mutualPackageDetails = MutableLiveData<List<PackageDetailsEntity>>()
    val packageDetails: LiveData<List<PackageDetailsEntity>> = mutualPackageDetails
    private var validatedObject = MutableLiveData<Pair<PackageDetailsEntity?,Boolean>>()
    val validationStatus: LiveData<Pair<PackageDetailsEntity?,Boolean>> = validatedObject

    fun addPackageDetails(packageDetailsEntity: PackageDetailsEntity){
        viewModelScope.launch(Dispatchers.IO) {
            val isInserted = iPackageDetailsRepository.addPackageDetails(packageDetailsEntity)
            if (isInserted){
                mutualPackageDetails.postValue(mutableListOf(packageDetailsEntity))
            }
        }
    }
    fun inputValidation(pkgId:String, baseCost:String, pkgWeight:String, pkgDistance:String, offerCode:String){
        if(offerCode.isEmpty() || pkgId.isEmpty() || baseCost.isEmpty() || pkgWeight.isEmpty()
            || pkgDistance.isEmpty()){
            validatedObject.postValue(Pair(null,false))
            return
        }
        validatedObject.postValue(Pair(PackageDetailsEntity(pkgId,baseCost.toDouble(),pkgWeight.toDouble(),pkgDistance.toDouble(),offerCode),true))
    }
}