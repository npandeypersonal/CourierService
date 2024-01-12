package com.courierservice.challenge.view

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.courierservice.challenge.databinding.ActivityMainBinding
import com.courierservice.challenge.databinding.AddItemLayoutBinding
import com.courierservice.challenge.databinding.AddOfferLayoutBinding
import com.courierservice.challenge.viewmodel.PackageDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.courierservice.challenge.data.database.entities.OfferEntity
import com.courierservice.challenge.data.database.entities.PackageDetailsEntity
import com.courierservice.challenge.data.models.PackageDetails
import com.courierservice.challenge.data.models.VehicleDetails
import com.courierservice.challenge.databinding.VehicleDetailsLayoutBinding
import com.courierservice.challenge.viewmodel.CourierServiceViewModel
import com.courierservice.challenge.viewmodel.OfferViewModel
import kotlin.math.max


@AndroidEntryPoint
class CourierServiceActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val packageDetailsViewModel: PackageDetailsViewModel by viewModels()
    private val courierServiceViewModel: CourierServiceViewModel by viewModels()
    private val offerViewModel:OfferViewModel by viewModels()
    private var packageList = mutableListOf<PackageDetails>()
    private lateinit var courierServiceAdapter:CourierServiceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = activityMainBinding.root
        setContentView(view)
        initView()
        syncInitialData()
        addObserverListener()
        clickListener()
    }

    private fun clickListener() {
        activityMainBinding.addingBtn.setOnClickListener { addPackage() }
        activityMainBinding.addOfferBtn.setOnClickListener { addOffer() }
        activityMainBinding.deliveryCostBtn.setOnClickListener{
            courierServiceViewModel.getDeliveryCost()
        }
        activityMainBinding.deliveryTimeBtn.setOnClickListener{ addVehicleDetails() }
    }

    private fun initView() {
        courierServiceAdapter = CourierServiceAdapter(this, packageList)
        activityMainBinding.mRecycler.layoutManager = LinearLayoutManager(this)
        activityMainBinding.mRecycler.adapter = courierServiceAdapter
        activityMainBinding.mRecycler.isNestedScrollingEnabled = false
        activityMainBinding.mRecycler.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        )

    }
    private fun syncInitialData(){
        try {
            offerViewModel.addOfferList(mutableListOf())
        }catch (e: SQLiteConstraintException){
            println("Error: $e")
        }
    }
    private fun addObserverListener(){
        courierServiceViewModel.deliveryCostList.observe(this){
            packageList.clear()
            packageList.addAll(it)
            courierServiceAdapter.notifyDataSetChanged()
        }
        offerViewModel.validationStatus.observe(this){
            if (it.second){
                it.first?.let {offerEntity->
                    offerViewModel.addOffer(offerEntity)
                    Toast.makeText(this, "Adding User Information Success", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
            }
        }
        packageDetailsViewModel.validationStatus.observe(this){
            if (it.second){
                it.first?.let {pkgObj->
                    packageDetailsViewModel.addPackageDetails(pkgObj)
                    Toast.makeText(this, "Package Information Success", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
            }
        }
        courierServiceViewModel.validationStatus.observe(this){
            if (it.second){
                it.first?.let {vehicleDetails->
                    courierServiceViewModel.getDeliveryTimeEstimation(vehicleDetails)
                }
            }else {
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addPackage() {
        val addItemLayoutBinding = AddItemLayoutBinding.inflate(LayoutInflater.from(this), activityMainBinding.root, false)
        val view: View = addItemLayoutBinding.root
        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(view)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val pkgId = addItemLayoutBinding.pkgIdTxt.text.toString()
            val basePrice = addItemLayoutBinding.basePriceTxt.text.toString()
            val pkgWeight = addItemLayoutBinding.pkgWeightTxt.text.toString()
            val distance = addItemLayoutBinding.distanceTxt.text.toString()
            val offerCode = addItemLayoutBinding.offerCodeTxt.text.toString()
            packageDetailsViewModel.inputValidation(pkgId,basePrice,pkgWeight,distance,offerCode)
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()

        }
        addDialog.create()
        addDialog.show()
    }
    private fun addOffer() {
        val addOfferLayoutBinding = AddOfferLayoutBinding.inflate(LayoutInflater.from(this), activityMainBinding.root, false)
        val view: View = addOfferLayoutBinding.root
        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(view)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val offerCode = addOfferLayoutBinding.offerIdTxt.text.toString()
            val percentage = addOfferLayoutBinding.discountTxt.text.toString()
            val minDiscount = addOfferLayoutBinding.minDiscountTxt.text.toString()
            val maxDiscount = addOfferLayoutBinding.maxDiscountTxt.text.toString()
            val minWeight = addOfferLayoutBinding.minWeightTxt.text.toString()
            val maxWeight = addOfferLayoutBinding.maxWeightTxt.text.toString()

            offerViewModel.inputValidation(offerCode, percentage, minDiscount, maxDiscount, minWeight, maxWeight)
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()

        }
        addDialog.create()
        addDialog.show()
    }
    private fun addVehicleDetails() {
        val vehicleLayoutBinding = VehicleDetailsLayoutBinding.inflate(LayoutInflater.from(this), activityMainBinding.root, false)
        val view: View = vehicleLayoutBinding.root
        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(view)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val totalVehicle = vehicleLayoutBinding.totalVehicleTxt.text.toString()
            val maxSpeed = vehicleLayoutBinding.maxSpeedTxt.text.toString()
            val loadCapacity = vehicleLayoutBinding.maxLoadTxt.text.toString()
            courierServiceViewModel.inputValidation(totalVehicle, maxSpeed,loadCapacity)
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }
}