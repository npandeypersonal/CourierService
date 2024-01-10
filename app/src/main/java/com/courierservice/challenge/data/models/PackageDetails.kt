package com.courierservice.challenge.data.models

data class PackageDetails(
    val pkgId: String,
    val discount: String,
    val totalCost: String,
    var deliveryTime: String = "",
    val isDelivered: Boolean = false
)
