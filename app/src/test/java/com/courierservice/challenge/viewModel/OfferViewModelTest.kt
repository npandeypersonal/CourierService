package com.courierservice.challenge.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.VehicleDetails
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.viewmodel.OfferViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class OfferViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var iOfferRepository: IOfferRepository

    private lateinit var offerViewModel: OfferViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // Set up the main dispatcher for testing
        Dispatchers.setMain(UnconfinedTestDispatcher())

        offerViewModel = OfferViewModel(iOfferRepository)
    }

    @Test
    fun `test addOffer`() = runBlocking{
        // Mock data
         val offer = OfferEntity("OFR001", 5.0, 0.0, 200.0, 70.0, 200.0)

        // Call the method
        offerViewModel.addOffer(offer)

        delay(1000)
        verify(iOfferRepository).addOfferData(offer)
    }

    @Test
    fun `test addOfferList`() = runBlocking{
        // Mock data
        val offer = mutableListOf(OfferEntity("OFR001", 5.0, 0.0, 200.0, 70.0, 200.0))

        // Call the method
        offerViewModel.addOfferList(offer)
        delay(1000)

        verify(iOfferRepository).addOfferListData(offer)
    }

    @After
    fun tearDown(){
       Dispatchers.resetMain()
    }
}