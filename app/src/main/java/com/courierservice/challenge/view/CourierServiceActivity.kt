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
        initData();
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

    private fun initData() {
        val list: MutableList<OfferEntity> = mutableListOf()
        val offer001 = OfferEntity("OFR001",10.0,0.0,200.0,70.0,200.0)
        val offer002 = OfferEntity("OFR002",7.0,50.0,150.0,100.0,250.0)
        val offer003 = OfferEntity("OFR003",5.0,50.0,250.0,10.0,150.0)
        list.add(offer001)
        list.add(offer002)
        list.add(offer003)
        try {
            offerViewModel.addOfferList(list)
        }catch (e: SQLiteConstraintException){
            println("Error: $e")
        }

        courierServiceAdapter = CourierServiceAdapter(this, packageList)
        activityMainBinding.mRecycler.layoutManager = LinearLayoutManager(this)
        activityMainBinding.mRecycler.adapter = courierServiceAdapter
        activityMainBinding.mRecycler.isNestedScrollingEnabled = false
        activityMainBinding.mRecycler.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        )
        courierServiceViewModel.deliveryCostList.observe(this){
            packageList.clear()
            packageList.addAll(it)
            courierServiceAdapter.notifyDataSetChanged()
        }
    }

    fun addPackage() {
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
            if (offerCode.isEmpty() || pkgId.isEmpty() || basePrice.isEmpty() || pkgWeight.isEmpty()
                || distance.isEmpty()){
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
            }else {
                packageDetailsViewModel.addPackageDetails(
                    PackageDetailsEntity(
                        pkgId,
                        basePrice.toDouble(),
                        pkgWeight.toDouble(),
                        distance.toDouble(),
                        offerCode
                    )
                )
                Toast.makeText(this, "Package Information Success", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
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
            if (offerCode.isEmpty() || percentage.isEmpty() || minDiscount.isEmpty() || maxDiscount.isEmpty()
                || minWeight.isEmpty() || maxWeight.isEmpty()){
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
            } else {
                val offerEntity =
                    OfferEntity(offerCode, percentage.toDouble(), 0.0, 200.0, 70.0, 200.0)
                offerViewModel.addOffer(offerEntity)
                Toast.makeText(this, "Adding User Information Success", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
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

            if (totalVehicle.isEmpty() || maxSpeed.isEmpty() || loadCapacity.isEmpty()){
                Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
                return@setPositiveButton
            } else {
                val vehicleDetails = VehicleDetails(
                    totalVehicle.toInt(),
                    maxSpeed.toDouble(),
                    loadCapacity.toDouble()
                )
                courierServiceViewModel.getDeliveryTimeEstimation(vehicleDetails)
                Toast.makeText(this, "Adding User Information Success", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }
}