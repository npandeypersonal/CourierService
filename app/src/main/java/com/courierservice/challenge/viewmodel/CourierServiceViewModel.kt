package com.courierservice.challenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.PackageDetails
import com.courierservice.challenge.data.models.VehicleDetails
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.IPackageDetailsRepository
import com.courierservice.challenge.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourierServiceViewModel @Inject constructor(): ViewModel() {

    @Inject
    lateinit var iOfferRepository: IOfferRepository

    @Inject
    lateinit var iPackageDetailsRepository: IPackageDetailsRepository

    private var mutualDeliveryCostList = MutableLiveData<List<PackageDetails>>()
    val deliveryCostList: LiveData<List<PackageDetails>> = mutualDeliveryCostList

    fun getDeliveryCost(){
        viewModelScope.launch{
            val packageList = viewModelScope.async(Dispatchers.IO) { iPackageDetailsRepository.getPackageDetails() }.await()
            mutualDeliveryCostList.postValue(getPackageDetailsList(packageList))
        }
    }

    fun getDeliveryTimeEstimation(vehicleDetails: VehicleDetails){
        viewModelScope.launch{
            val packageList = viewModelScope.async(Dispatchers.IO) { iPackageDetailsRepository.getPackageDetails() }.await()
            val offerList = getPackageDetailsList(packageList)
            val deliveryTimeList = Utils.getDeliveryEstimatedTimeList(vehicleDetails,packageList)
            val mergeList = mergeLists(offerList,deliveryTimeList)
            mutualDeliveryCostList.postValue(mergeList)
        }
    }

    private fun mergeLists(offerList: MutableList<PackageDetails>,deliveryTimeList: List<PackageDetails>): List<PackageDetails> {
        val deliveryTimeListMap = deliveryTimeList.associateBy { it.pkgId }
       offerList.map { pkg ->
            pkg.deliveryTime = deliveryTimeListMap[pkg.pkgId]?.deliveryTime ?: "0.0"
        }
        return offerList
    }

    private suspend fun getPackageDetailsList(packageList: List<PackageDetailsEntity>): MutableList<PackageDetails>{
        val deliveryCostList = mutableListOf<PackageDetails>()
        packageList.forEach{
            val offerJob = viewModelScope.async(Dispatchers.IO) {
                iOfferRepository.getOfferByCode(it.offerCode)
            }
            val offer = offerJob.await()
            val (deliveryCost,discount) = Utils.getDiscountAmount(offer,it)
            val totalCost = deliveryCost - discount
            deliveryCostList.add(PackageDetails(it.pkgId,discount.toString(),totalCost.toString()))
        }
        return deliveryCostList
    }
}