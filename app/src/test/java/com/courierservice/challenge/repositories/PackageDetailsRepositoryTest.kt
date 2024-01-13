package com.courierservice.challenge.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.courierservice.challenge.data.database.dao.OfferDao
import com.courierservice.challenge.data.database.dao.PackageDetailsDao
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.repositories.IOfferRepository
import com.courierservice.challenge.data.repositories.OfferRepositoryImp
import com.courierservice.challenge.data.repositories.PackageDetailsRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
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
class PackageDetailsRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var packageDetailsDao: PackageDetailsDao

    private lateinit var packageDetailsRepository: PackageDetailsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        packageDetailsRepository = PackageDetailsRepository(packageDetailsDao)
    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun testAddPackageDetails_Successful(): Unit = runBlocking {
        val packageDetailsEntity = PackageDetailsEntity("P001", 50.0, 5.0, 100.0, "OFR001")

        `when`(packageDetailsDao.insertPackageDetailsEntity(packageDetailsEntity)).thenReturn(1)

        val result = packageDetailsRepository.addPackageDetails(packageDetailsEntity)

        assertEquals(true, result)
        verify(packageDetailsDao).insertPackageDetailsEntity(packageDetailsEntity)
    }

    @Test
    fun testAddPackageDetails_Failure(): Unit = runBlocking {
        val packageDetailsEntity = PackageDetailsEntity("P001", 50.0, 5.0, 100.0, "OFR001")

        `when`(packageDetailsDao.insertPackageDetailsEntity(packageDetailsEntity)).thenReturn(-1)

        val result = packageDetailsRepository.addPackageDetails(packageDetailsEntity)

        assertEquals(false, result)
        verify(packageDetailsDao).insertPackageDetailsEntity(packageDetailsEntity)
    }

    @Test
    fun testGetPackageDetails(): Unit = runBlocking {
        val mockPackageList = listOf(
            PackageDetailsEntity("P001", 50.0, 5.0, 100.0, "OFR001"),
            PackageDetailsEntity("P002", 30.0, 3.0, 50.0, "OFR002")
        )

        `when`(packageDetailsDao.getAllPackageDetailsEntity()).thenReturn(mockPackageList)

        val result = packageDetailsRepository.getPackageDetails()

        assertEquals(mockPackageList, result)
        verify(packageDetailsDao).getAllPackageDetailsEntity()
    }
}