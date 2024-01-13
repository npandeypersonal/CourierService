package com.courierservice.challenge.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.OfferRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class OfferRepositoryImpTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var offerDao: OfferDao

    private lateinit var offerRepository: IOfferRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        offerRepository = OfferRepositoryImp(offerDao)
    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun testAddOfferData() = runBlocking {
        val offerEntity = OfferEntity("OFR001", 10.0, 0.0, 200.0, 70.0, 200.0)

        offerRepository.addOfferData(offerEntity)

        verify(offerDao).insertOffer(offerEntity)
    }

    @Test
    fun testAddOfferListData() = runBlocking {
        val offerList = listOf(
            OfferEntity("OFR001", 10.0, 0.0, 200.0, 70.0, 200.0),
            OfferEntity("OFR002", 7.0, 50.0, 150.0, 100.0, 250.0)
        )
        offerRepository.addOfferListData(offerList)

        verify(offerDao).insertOfferList(offerList)
    }

    @Test
    fun testGetOfferByCode() = runBlocking {
        val offerEntity = OfferEntity("OFR001", 10.0, 0.0, 200.0, 70.0, 200.0)
        `when`(offerDao.getOfferByCode("OFR001")).thenReturn(offerEntity)

        val result = offerRepository.getOfferByCode("OFR001")
        assertEquals(result.offerCode,offerEntity.offerCode)
    }
}
