package com.courierservice.challenge.data.repositories

import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.entities.OfferEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfferRepositoryImp(private val offerDao: OfferDao): IOfferRepository {
    override suspend fun addOfferData(offerEntity: OfferEntity){
        offerDao.insertOffer(offerEntity)
    }

    override suspend fun addOfferListData(offerList: List<OfferEntity>) {
        offerDao.insertOfferList(offerList)
    }

    override suspend fun getOfferByCode(code: String): OfferEntity {
        return offerDao.getOfferByCode(code)
    }
}