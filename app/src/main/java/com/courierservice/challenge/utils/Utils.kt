package com.courierservice.challenge.utils

import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.PackageDetails
import com.courierservice.challenge.data.models.VehicleDetails
import java.math.RoundingMode
import kotlin.math.max

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
        var currentDuration = vehicleAvailability.min()
        val vehicleIndex = vehicleAvailability.indexOf(currentDuration)
        val pkgSize = packages.size
        if (pkgSize <= 1){
            if (pkgSize == 1) {
                val deliveryTime = packages[0].pkgDistance / vehicleDetails.maxSpeed
                currentDuration += deliveryTime
                val roundedUp = currentDuration.toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
                shipments.add(PackageDetails(packages[0].pkgId, "", "", roundedUp.toString()))
            }
            return shipments
        }
        val minDiff = getMinDifference(packages,vehicleDetails.maxCarriableWeight)
        val (newPkgList,newShipments,currentTime) = getDeliveriesAndRemainingShipments(packages,vehicleDetails,minDiff,shipments,currentDuration)

        vehicleAvailability[vehicleIndex] = 2*currentTime
        return calculateDeliveryEstimation(newPkgList,vehicleAvailability,vehicleDetails,newShipments)
    }
    private fun getMinDifference(packages: List<PackageDetailsEntity>, maxCarriableWeight: Double): Double{
        var start = 0
        var end = 0
        var currSum: Double = packages[0].pkgWeight
        var minDiff = maxCarriableWeight - currSum

        val pkgSize = packages.size
        while (end < pkgSize - 1) {
            if (currSum < maxCarriableWeight) {
                end++
                currSum += packages[end].pkgWeight
            } else {
                currSum -= packages[start].pkgWeight
                start++
            }
            if (maxCarriableWeight - currSum < minDiff && maxCarriableWeight - currSum >=0 ) {
                minDiff = maxCarriableWeight - currSum
            }
        }
        return minDiff
    }

    private fun getDeliveriesAndRemainingShipments(packages: List<PackageDetailsEntity>, vehicleDetails: VehicleDetails, minDiff:Double,
                                                   shipments: MutableList<PackageDetails>, currentTime: Double): Triple<List<PackageDetailsEntity>,MutableList<PackageDetails>,Double>{
        var start = 0
        var end = 0
        var currentIndex: Int
        var currSum = packages[0].pkgWeight
        val pkgSize = packages.size
        var newPkgList = packages.toMutableList()
        var currentWeight = 0.0
        var currentDuration = currentTime
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
            if (vehicleDetails.maxCarriableWeight - currSum == minDiff ||
                (currSum > vehicleDetails.maxCarriableWeight && vehicleDetails.maxCarriableWeight - (currSum - packages[currentIndex].pkgWeight) == minDiff)) {
                if (currSum > vehicleDetails.maxCarriableWeight){
                    end--
                }
                var tempCurrentTime = 0.0
                for (i in start .. end) {
                    val deliveryTime = packages[i].pkgDistance/vehicleDetails.maxSpeed
                    currentWeight += packages[i].pkgWeight
                    val totalTime = currentDuration + deliveryTime
                    val roundedUp = totalTime.toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
                    tempCurrentTime = max(tempCurrentTime,roundedUp)
                    shipments.add(PackageDetails(packages[i].pkgId,"","",roundedUp.toString()))
                    newPkgList.remove(packages[i])
                }
                currentDuration += tempCurrentTime
                break
            }
        }
        return Triple(newPkgList,shipments,currentDuration)
    }
}
