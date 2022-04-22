package com.myralyn.rvtour.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myralyn.rvtour.R
import com.myralyn.rvtour.city.City
import com.myralyn.rvtour.city.VacationSpots


class FavoriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        //dont forget to call fun setupRecyclerView here, otherwise you  will not see the view in favourite
        setupFavRecyclerView(view)
        return view
    }

    //Link RecyclerView with FavouriteAdapter
    private fun setupFavRecyclerView(view: View?){
        val context = requireContext()

        //cast mutable favoriteCityList to ArrayList
        val favouriteCityList = VacationSpots.favoriteCityList as ArrayList<City>

        val favouriteAdapter = FavouriteAdapter(context, favouriteCityList)//initialize adapter and pass required params

        /**
         * Link the recyclerView with the Adapter
         */
        val favRecyclerView = view?.findViewById<RecyclerView>(R.id.fav_city_recycler_view)
        favRecyclerView?.adapter = favouriteAdapter
        favRecyclerView?.setHasFixedSize(true)

        //define layoutManage using linear layout as we want the recyclerview to appear as a list
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        favRecyclerView?.layoutManager = layoutManager
    }
}
