package com.myralyn.rvtour.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myralyn.rvtour.R


class CityListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_city_list, container, false)

        setupRecyclerView(view)

        return view
    }

    private fun setupRecyclerView(view: View?) {

        val context = requireContext()

        val cityAdapter = CityAdapter(context, VacationSpots.cityList!!)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.city_recycler_view)
        recyclerView?.adapter = cityAdapter

        //set this to true if width and height of recyclerView does not change at run time and improves performance
        recyclerView?.setHasFixedSize(true)

        //define layout manager. We use LinearLayout coz we want the recyclerview to appear as a list
        //and orientation makes the recyclerView scroll in vertical direction
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager
    }
}