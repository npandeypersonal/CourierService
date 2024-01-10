package com.courierservice.challenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OfferTable")
data class OfferEntity(
    @PrimaryKey
    val offerCode: String,
    val discountPercent: Double,
    val minDistance: Double,
    val maxDistance: Double,
    val minWeight: Double,
    val maxWeight: Double
)


