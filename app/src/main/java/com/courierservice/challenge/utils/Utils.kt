package com.courierservice.challenge.utils

import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.PackageDetails
import com.courierservice.challenge.data.models.VehicleDetails
import java.math.RoundingMode

object Utils {

     fun getDiscountAmount(offerEntity: OfferEntity?, packageDetailsEntity: PackageDetailsEntity):Pair<Double,Double> {
         val deliveryCost =
             packageDetailsEntity.baseCost + (10 * packageDetailsEntity.pkgWeight) + (5 * packageDetailsEntity.pkgDistance)
         var discount = 0.0
         offerEntity?.let {
             if (packageDetailsEntity.pkgDistance >= offerEntity.minDistance && packageDetailsEntity.pkgDistance <= offerEntity.maxDistance
                 && packageDetailsEntity.pkgWeight >= offerEntity.minWeight && packageDetailsEntity.pkgWeight <= offerEntity.maxWeight
             ) {
                 discount = (deliveryCost * offerEntity.discountPercent) / 100
             }
        }
        return Pair(deliveryCost,discount)
    }

    fun getDeliveryEstimatedTimeList(vehicleDetails: VehicleDetails,packageList: List<PackageDetailsEntity>):MutableList<PackageDetails>{
        val vehicleAvailability = MutableList(vehicleDetails.totalVehicles) {0.0}
        val sortedPackages = packageList.sortedWith(compareByDescending<PackageDetailsEntity> { it.pkgWeight }.thenBy { it.pkgDistance })
        return calculateDeliveryEstimation(sortedPackages,vehicleAvailability,vehicleDetails, mutableListOf())
    }
    private fun calculateDeliveryEstimation(packages: List<PackageDetailsEntity>, vehicleAvailability: MutableList<Double>, vehicleDetails: VehicleDetails, shipments: MutableList<PackageDetails>): MutableList<PackageDetails> {
        var currentWeight = 0.0
        var currentDuration = vehicleAvailability.min()
        val vehicleIndex = vehicleAvailability.indexOf(currentDuration)
        val pkgSize = packages.size
        if (pkgSize <= 1){
            if (pkgSize == 1) {
                val deliveryTime = packages[0].pkgDistance / vehicleDetails.maxSpeed
                currentWeight += packages[0].pkgWeight
                val totalTime = currentDuration + deliveryTime
                val roundedUp = totalTime.toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
                shipments.add(PackageDetails(packages[0].pkgId, "", "", roundedUp.toString()))
            }
            return shipments
        }
        var newPkgList = packages

        var start = 0
        var end = 0
        var currSum: Double = packages[0].pkgWeight
        var minDiff = vehicleDetails.maxCarriableWeight - currSum

        while (end < pkgSize - 1) {
            if (currSum < vehicleDetails.maxCarriableWeight) {
                end++
                currSum += packages[end].pkgWeight
            } else {
                currSum -= packages[start].pkgWeight
                start++
            }
            if (vehicleDetails.maxCarriableWeight - currSum < minDiff && vehicleDetails.maxCarriableWeight - currSum >=0 ) {
                minDiff = vehicleDetails.maxCarriableWeight - currSum
            }
        }
        start = 0
        end = 0
        var currentIndex = 0
        currSum = packages[0].pkgWeight
        while (end < pkgSize - 1) {

            if (currSum < vehicleDetails.maxCarriableWeight) {
                end++
                currSum += packages[end].pkgWeight
                currentIndex = end
            } else {
                currentIndex = start
                currSum -= packages[start].pkgWeight
                start++
            }
            if (vehicleDetails.maxCarriableWeight - currSum == minDiff) {
                var tempCurrentTime = 0.0
                for (i in start .. end) {
                    val deliveryTime = packages[i].pkgDistance/vehicleDetails.maxSpeed
                    currentWeight += packages[i].pkgWeight
                    val totalTime = currentDuration + deliveryTime
                    val roundedUp = totalTime.toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
                    tempCurrentTime = Math.max(tempCurrentTime,roundedUp)
                    shipments.add(PackageDetails(packages[i].pkgId,"","",roundedUp.toString()))
                    newPkgList = newPkgList.filter { it.pkgId != packages[i].pkgId }
                }
                currentDuration += tempCurrentTime
                break
            }else if (currSum > vehicleDetails.maxCarriableWeight && vehicleDetails.maxCarriableWeight - (currSum - packages[currentIndex].pkgWeight) == minDiff){
                var tempCurrentTime = 0.0
                end--
                for (i in start .. end) {
                    val deliveryTime = packages[i].pkgDistance/vehicleDetails.maxSpeed
                    currentWeight += packages[i].pkgWeight
                    val totalTime = currentDuration + deliveryTime
                    val roundedUp = totalTime.toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
                    tempCurrentTime = Math.max(tempCurrentTime,roundedUp)
                    shipments.add(PackageDetails(packages[i].pkgId,"","",roundedUp.toString()))
                    newPkgList = newPkgList.filter { it.pkgId != packages[i].pkgId }
                }
                currentDuration += tempCurrentTime
                break
            }

        }

        vehicleAvailability[vehicleIndex] = 2*currentDuration
        return calculateDeliveryEstimation(newPkgList,vehicleAvailability,vehicleDetails,shipments)
    }
}