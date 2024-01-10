package com.courierservice.challenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.dao.PackageDetailsDao
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity

@Database(entities = [PackageDetailsEntity::class,OfferEntity::class], version = 1)
abstract class CourierServiceDB : RoomDatabase() {

    abstract fun offerDao(): OfferDao
    abstract fun packageDetailsDao(): PackageDetailsDao
}