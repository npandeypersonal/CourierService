package com.courierservice.challenge.utils

import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.VehicleDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun testGetDiscountAmount_WithValidOffer() {
        val offerEntity = OfferEntity("OFR001", 10.0, 0.0, 200.0, 10.0, 200.0)
        val packageDetailsEntity = PackageDetailsEntity("P001", 50.0, 15.0, 100.0, "OFR001")

        val result = Utils.getDiscountAmount(offerEntity, packageDetailsEntity)

        assertEquals(700.0, result.first, 0.01)
        assertEquals(70.0, result.second, 0.01)
    }

    @Test
    fun testGetDiscountAmount_WithInvalidOffer() {
        val offerEntity = OfferEntity("OFR001", 10.0, 0.0, 200.0, 70.0, 200.0)
        val packageDetailsEntity = PackageDetailsEntity("P001", 50.0, 5.0, 300.0, "OFR001")

        val result = Utils.getDiscountAmount(offerEntity, packageDetailsEntity)

        assertEquals(1600.0, result.first, 0.01)
        assertEquals(0.0, result.second, 0.01)
    }

    @Test
    fun testGetDeliveryEstimatedTimeList() {
        val vehicleDetails = VehicleDetails(2, 70.0, 200.0)


        val packageList = listOf(
            PackageDetailsEntity("P001", 100.0, 50.0, 30.0, "OFR001"),
            PackageDetailsEntity("P002", 100.0, 75.0, 125.0, "OFR002"),
            PackageDetailsEntity("P003", 100.0, 110.0, 60.0, "OFR003"),
            PackageDetailsEntity("P004", 100.0, 175.0, 100.0, "OFR003"),
            PackageDetailsEntity("P005", 100.0, 155.0, 95.0, "OFR003")
        )

        val result = Utils.getDeliveryEstimatedTimeList(vehicleDetails, packageList)

        assertEquals(5, result.size)
        assertEquals("0.85",result[0].deliveryTime)
    }
}