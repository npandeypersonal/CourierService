package com.courierservice.challenge.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.VehicleDetails
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.IPackageDetailsRepository
import com.courierservice.challenge.viewmodel.CourierServiceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
class CourierServiceViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var iPackageDetailsRepository: IPackageDetailsRepository

    @Mock
    lateinit var iOfferRepository: IOfferRepository

    private lateinit var courierServiceViewModel: CourierServiceViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // Set up the main dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)
        courierServiceViewModel = CourierServiceViewModel()
        courierServiceViewModel.iPackageDetailsRepository = iPackageDetailsRepository
        courierServiceViewModel.iOfferRepository = iOfferRepository
    }

    @Test
    fun `test getDeliveryCost`(): Unit = runBlocking{
        // Mock data
        val packageList = listOf(PackageDetailsEntity("1", 10.0, 5.0, 100.0, "OFR001"))
        val offer = OfferEntity("OFR001", 5.0, 0.0, 200.0, 70.0, 200.0)

        // Mocking behavior
            `when`(courierServiceViewModel.iPackageDetailsRepository.getPackageDetails()).thenReturn(packageList)
            `when`(courierServiceViewModel.iOfferRepository.getOfferByCode("OFR001")).thenReturn(offer)

        // Call the method
        courierServiceViewModel.getDeliveryCost()

        verify(courierServiceViewModel.iPackageDetailsRepository).getPackageDetails()
    }

    @Test
    fun `test getDeliveryTimeEstimation`(): Unit = runBlocking{
        // Mock data
        val packageList = listOf(PackageDetailsEntity("1", 10.0, 5.0, 100.0, "OFR001"))
        val offer = OfferEntity("OFR001", 5.0, 0.0, 200.0, 70.0, 200.0)

        // Mocking behavior
        `when`(iPackageDetailsRepository.getPackageDetails()).thenReturn(packageList)
        `when`(iOfferRepository.getOfferByCode("OFR001")).thenReturn(offer)

        // Call the method
        courierServiceViewModel.getDeliveryTimeEstimation(VehicleDetails(2,70.00,200.00))

        delay(1000)
        verify(iPackageDetailsRepository).getPackageDetails()
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }
}
