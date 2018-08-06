package com.vlad.lesson7_maskaikin_kotlin.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.vlad.lesson7_maskaikin_kotlin.MainActivity.Companion.MY_LOG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.provider.Settings
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.vlad.lesson7_maskaikin_kotlin.*
import com.vlad.lesson7_maskaikin_kotlin.R
import com.vlad.lesson7_maskaikin_kotlin.getBridge.Object
import com.vlad.lesson7_maskaikin_kotlin.getBridge.ResultBridge
import com.vlad.lesson7_maskaikin_kotlin.retrofit.RetrofitClient
import com.vlad.lesson7_maskaikin_kotlin.retrofit.ServiceBridge
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bridge_view.*
import kotlinx.android.synthetic.main.fragment_view_map_bridge.*


class FragmentViewMapBridge : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var jsonApi: ServiceBridge
    private var disposable: Disposable? = null
    private var idBridge: Int? = -1
    private var checkAlarm: Boolean? = false
    private var textRemind: String? = null

    companion object {

        const val REQUEST_CODE_PERMISSION_FINE_LOCATION = 555
        const val LAT_SANKT_PERETSBURG = 59.9358148
        const val LNG_SANKT_PERETSBURG = 30.3284948


        private lateinit var locationRequest: LocationRequest
        private lateinit var lastLocation: Location
        private var currLocationMarker: Marker? = null
        private var locationLatLng : LatLng? = null
        private var fusedLocationProviderClient: FusedLocationProviderClient? = null
        private val sanktPetersburg = LatLng(LAT_SANKT_PERETSBURG, LNG_SANKT_PERETSBURG)


        fun getInstance(): FragmentViewMapBridge {
            return FragmentViewMapBridge()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        idBridge = this.arguments?.getInt(InfoSetReminderActivity.ID_BRIDGE)
        checkAlarm = this.arguments?.getBoolean(InfoSetReminderActivity.CHECK_ALARM)
        textRemind = this.arguments?.getString(InfoSetReminderActivity.TEXT_FOR_VIEW_ALARM)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(MY_LOG, "карты")
        mMap = googleMap

        createLocationRequest()

        val permissionStatus = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_CODE_PERMISSION_FINE_LOCATION)
            imageViewLocation.visibility = View.GONE
        } else {
            fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            imageViewLocation.visibility = View.VISIBLE
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanktPetersburg, 12f))
        mMap.setOnMapClickListener {
            bridgeViewInFragmenWithMap.visibility = View.GONE
        }

        loadBridgeDisposableForMapFragment()

        imageViewLocation.setOnClickListener {
            startGetLocation()
            if (locationLatLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 12f))
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_FINE_LOCATION -> {
                val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentGoogleMap)
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(MY_LOG, "Все ок")
                    // Просит проверить на Permission, хотя тут ве проверенно, из-за этого  @SuppressLint("MissingPermission")
                    imageViewLocation.visibility = View.VISIBLE
                    fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    (mapFragment as? SupportMapFragment)?.getMapAsync(this)
                } else {
                    Log.d(MY_LOG, "Все плохо")
                    imageViewLocation.visibility = View.GONE
                    val snackbarIntentSettings = Snackbar.make(constraintLayoutFragmentMapParent,
                            R.string.turnOnLocationSettings,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(getText(R.string.yes)) {
                                startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                                (mapFragment as? SupportMapFragment)?.getMapAsync(this)
                            }
                    snackbarIntentSettings.show()
                }
                return
            }
        }
    }


    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                lastLocation = location
                if (currLocationMarker != null) {
                    currLocationMarker!!.remove()
                }

                locationLatLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(locationLatLng!!)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                currLocationMarker = mMap.addMarker(markerOptions)

            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val retrofit = RetrofitClient.instance
        jsonApi = retrofit.create(ServiceBridge::class.java)

        val view = inflater.inflate(R.layout.fragment_view_map_bridge, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.context)
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentGoogleMap)
        (mapFragment as? SupportMapFragment)?.getMapAsync(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentGoogleMap)
        (mapFragment as? SupportMapFragment)?.getMapAsync(this)
    }

    override fun onPause() {
        super.onPause()

        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        }
    }

    private fun setMarkersBridge(bridges: ResultBridge, latBridges: DoubleArray?, lngBridges: DoubleArray?, countBridge: Int) {

        var position = 0
        while (position < countBridge) {
            val bridgePosition = latBridges?.get(position)?.let { lngBridges?.get(position)?.let { it1 -> LatLng(it, it1) } }
            val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(getImageStatusBridgeForMap(position, bridges))
            val markerBridge: Marker = mMap.addMarker(bridgePosition?.let { MarkerOptions().position(it) })
            markerBridge.tag = position
            markerBridge.setIcon(icon)
            position++
        }

    }

    private fun showBottomViewBridge(bridges: ResultBridge, positionBridge: Int) {
        Log.d(MY_LOG, "Чики пуки")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bridgeViewInFragmenWithMap.setBackgroundColor(resources.getColor(R.color.white, null))
        }
        val bridge = bridges.objects!![positionBridge]
        imageViewStatusBridge.setBackgroundResource(getImageStatusBridge(positionBridge, bridges))
        textViewNameBridge.text = bridge.name?.replace(getString(R.string.replace_Bridge), getString(R.string.replace_empty))
                ?.replace(getString(R.string.replace_bridge), getString(R.string.replace_empty))
        textViewCloseTimeBridge.text = getTimeCloseBridge(positionBridge, bridge)
        if (bridge.checkAlarm) {
            imageViewStatusAlarm.setBackgroundResource(R.drawable.ic_kolocol_on)
        } else {
            imageViewStatusAlarm.setBackgroundResource(R.drawable.ic_kolocol_off)
        }
        bridgeViewInFragmenWithMap.visibility = View.VISIBLE
    }

    private fun startInfoSetReminderActivity(bridge: Object) {
        val intent = Intent(activity, InfoSetReminderActivity::class.java)
        intent.putExtra(FragmentViewListBridge.BRIDGE, bridge)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun startGetLocation() {
        val permissionStatus = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(Array(1) { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_CODE_PERMISSION_FINE_LOCATION)
        } else {
            fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private fun loadBridgeDisposableForMapFragment() {

        disposable = jsonApi.bridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { bridges ->
                            val countBridge = bridges.objects?.size
                            if (countBridge != null) {
                                Log.d(MY_LOG, "не равно нулю число")
                                checkAlarmBridge(bridges, context)
                                val latBridges = getLatBridges(bridges, countBridge)
                                val lngBridges = getLngBridges(bridges, countBridge)
                                setMarkersBridge(bridges, latBridges, lngBridges, countBridge)
                                mMap.setOnMarkerClickListener { marker ->
                                    if (marker.tag != null) {
                                        val positionBridge = marker.tag as Int
                                        showBottomViewBridge(bridges, positionBridge)
                                        bridgeViewInFragmenWithMap.setOnClickListener {
                                            startInfoSetReminderActivity(bridges.objects!![positionBridge])
                                        }
                                    } else {
                                        bridgeViewInFragmenWithMap.visibility = View.GONE
                                    }
                                    true
                                }
                            }
                        },
                        { exception ->
                            exception.printStackTrace()
                            val snackbarError = Snackbar.make(constraintLayoutFragmentMapParent,
                                    R.string.errorGetBridgesFromRx,
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction(getText(R.string.yes)) {
                                        loadBridgeDisposableForMapFragment()
                                    }
                            snackbarError.show()

                        }
                )

    }

}

