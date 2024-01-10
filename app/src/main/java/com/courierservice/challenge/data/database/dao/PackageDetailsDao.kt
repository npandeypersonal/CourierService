package com.courierservice.challenge.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity

@Dao
interface PackageDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPackageDetailsEntity(packageDetailsEntity: PackageDetailsEntity): Long

    @Query("SELECT * FROM PackageDetails")
    fun getAllPackageDetailsEntity(): List<PackageDetailsEntity>
}