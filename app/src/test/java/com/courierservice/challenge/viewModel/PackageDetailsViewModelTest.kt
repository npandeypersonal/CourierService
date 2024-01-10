package com.courierservice.challenge.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.VehicleDetails
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.IPackageDetailsRepository
import com.courierservice.challenge.viewmodel.CourierServiceViewModel
import com.courierservice.challenge.viewmodel.PackageDetailsViewModel
import kotlinx.coroutines.Dispatchers
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
class PackageDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var iPackageDetailsRepository: IPackageDetailsRepository

    private lateinit var packageDetailsViewModel: PackageDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        packageDetailsViewModel = PackageDetailsViewModel(iPackageDetailsRepository)
    }

    @Test
    fun `test addPackageDetails`() = runBlockingTest{
        // Mock data
        val packageDetails = PackageDetailsEntity("1", 10.0, 5.0, 100.0, "OFR001")
        // Mocking behavior
        `when`(iPackageDetailsRepository.addPackageDetails(packageDetails)).thenReturn(true)

        // Call the method
        packageDetailsViewModel.addPackageDetails(packageDetails)

        packageDetailsViewModel.packageDetails.value?.equals(mutableListOf(packageDetails))
            ?.let { assert(it) }
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the test
        Dispatchers.resetMain()
    }
}