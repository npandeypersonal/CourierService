package com.courierservice.challenge.data.repositories

import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.entities.OfferEntity

interface IOfferRepository {
    suspend fun addOfferData(offerEntity: OfferEntity)
    suspend fun addOfferListData(offerList: List<OfferEntity>)
    suspend fun getOfferByCode(code: String): OfferEntity
}