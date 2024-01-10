package com.courierservice.challenge.data.repositories

import com.courierservice.challenge.data.database.dao.PackageDetailsDao
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageDetailsRepository (private val packageDetailsDao: PackageDetailsDao):IPackageDetailsRepository {

    override suspend fun addPackageDetails(packageDetailsEntity: PackageDetailsEntity):Boolean{
        val id = packageDetailsDao.insertPackageDetailsEntity(packageDetailsEntity)
        return id >= 0
    }

    override suspend fun getPackageDetails(): List<PackageDetailsEntity> {
       return packageDetailsDao.getAllPackageDetailsEntity()
    }
}