package dev.marawanxmamdouh.wander

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.marawanxmamdouh.wander.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        createMenu()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val googleEgypt = LatLng(30.020940, 31.424945)
        map.addMarker(MarkerOptions().position(googleEgypt).title("Google Office Egypt"))
        map.moveCamera(CameraUpdateFactory.newLatLng(googleEgypt))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(googleEgypt, 15f))

        setMapLongClick(map)
        setPoiClick(map)
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker?.showInfoWindow()
        }
    }

    private fun createMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.map_options, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.normal_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_NORMAL
                        true
                    }
                    R.id.hybrid_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_HYBRID
                        true
                    }
                    R.id.satellite_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        true
                    }
                    R.id.terrain_map -> {
                        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        true
                    }
                    else -> false
                }
            }
        })
    }
}