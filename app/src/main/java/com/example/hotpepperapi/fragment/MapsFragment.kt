package com.example.hotpepperapi.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.hotpepperapi.R
import com.example.hotpepperapi.viewModel.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var callback: OnMapReadyCallback
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = OnMapReadyCallback { googleMap ->

            viewModel.list.observe(viewLifecycleOwner) { list ->
                viewModel.multipleFlag.observe(viewLifecycleOwner) { flag ->

                    if (flag) {

                        for (i in list.indices) {
                            val store = LatLng(list[i].lat, list[i].lng)

                            googleMap.addMarker(
                                MarkerOptions().position(store).title(list[i].name)
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    store,
                                    15F
                                )
                            ) //zoom表示
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(store))
                        }

                    } else {

                        viewModel.forMap.observe(viewLifecycleOwner) {
                            val store = LatLng(it!!.lat, it.lng)

                            googleMap.addMarker(
                                MarkerOptions().position(store).title(it.name)
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    store,
                                    15F
                                )
                            ) //zoom表示
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(store))
                        }
                    }
                }

                googleMap.uiSettings.isZoomControlsEnabled = true //zoomボタン表示
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}