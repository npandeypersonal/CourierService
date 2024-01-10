# CourierService(Android-Kotlin-MVVM-Room-Hilt)
This repository contains a CourierService project in Kotlin to solve below 2 chanllenges.
1. Calculate estimate the total delivery cost of each package with
an offer code (if applicable).
2. Calculate the estimated delivery time for every package by maximizing no. of packages in every shipment

# Libraries 
This project using some libraries as follow
 AndroidX, MVVM, Coroutines, Hilt, Room, View Binding, Junit, Mockito, LiveData,Repository pattern etc..
# Design Pattern:
List of some design pattern use in this.
  Builder patter, Factory pattern, Singleton, Repository, Proxy etc..

# Project Flow
  This project have one Activity class for giving input(instead on commandline) to perform test
  1. Add Package Button: After clicking this button it will ask for package details.
  2. Add New Offer: We have already inserted given discount coupon in DB, this button for offer scalability, we can add new offer for futur using this.
  3. Delivery Cost: Calculate estimate the total delivery cost of each package, it will show answer of 1 challenge.
  4. Delivery Time: Calculate the estimated delivery time for every package, it will show answer of 2 challenge.
