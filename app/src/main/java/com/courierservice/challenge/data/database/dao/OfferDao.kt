package com.courierservice.challenge.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.courierservice.challenge.data.database.entities.OfferEntity

@Dao
interface OfferDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOffer(offer: OfferEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOfferList(offerList: List<OfferEntity>)

    @Update
    fun updateOffer(offer: OfferEntity)

    @Query("SELECT * FROM OfferTable WHERE offerCode=:code")
    fun getOfferByCode(code: String): OfferEntity
}
