package com.courierservice.challenge.di

import android.content.Context
import androidx.room.Room
import com.courierservice.challenge.data.database.CourierServiceDB
import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.dao.PackageDetailsDao
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.IPackageDetailsRepository
import com.courierservice.challenge.data.repositories.OfferRepositoryImp
import com.courierservice.challenge.data.repositories.PackageDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AapModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CourierServiceDB {
        return Room.databaseBuilder(
            context,
            CourierServiceDB::class.java,
            "courier_service_db"
        ).build()
    }

    @Provides
    fun provideOfferDao(database: CourierServiceDB): OfferDao {
        return database.offerDao()
    }

    @Provides
    fun providePackageDetailsDao(database: CourierServiceDB): PackageDetailsDao {
        return database.packageDetailsDao()
    }

    @Provides
    @Singleton
    fun provideOfferRepository(offerDao: OfferDao) : IOfferRepository {
        return OfferRepositoryImp(offerDao)
    }
    @Provides
    @Singleton
    fun providePackageDetailsRepository(packageDetailsDao: PackageDetailsDao) : IPackageDetailsRepository {
        return PackageDetailsRepository(packageDetailsDao)
    }

    /*@Provides
    @Singleton
    fun provideCourierServiceRepository(iOfferRepository: IOfferRepository,iPackageDetailsRepository: IPackageDetailsRepository) : ICourierServiceRepository {
        return ICourierServiceRepository

    }*/
}