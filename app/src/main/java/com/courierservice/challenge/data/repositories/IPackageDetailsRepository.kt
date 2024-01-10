package com.courierservice.challenge.data.repositories

import com.courierservice.challenge.data.database.entities.PackageDetailsEntity

interface IPackageDetailsRepository {
    suspend fun addPackageDetails(packageDetailsEntity: PackageDetailsEntity):Boolean
    suspend fun getPackageDetails():List<PackageDetailsEntity>
}