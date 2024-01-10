package com.courierservice.challenge.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PackageDetails")
data class PackageDetailsEntity(
    @PrimaryKey
    val pkgId: String,
    val baseCost: Double,
    val pkgWeight: Double,
    val pkgDistance: Double,
    val offerCode: String
)
