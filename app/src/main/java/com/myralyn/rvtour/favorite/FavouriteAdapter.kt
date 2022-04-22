package com.myralyn.rvtour.favorite

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myralyn.rvtour.R
import com.myralyn.rvtour.city.City

//Adapter always extends RecyclerView.Adapter which takes in parameter type: FavouriteAdapter.FavouriteViewHolder
class FavouriteAdapter(val context: Context, var favCityList: ArrayList<City>): RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    //create a limited number of empty viewHolder that will be used to display the images on the device
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        Log.i("FavouriteAdapter", "onCreateViewHolder: ViewHolder created")
        //inflate the layout file, list_item_favourite_city to a view then return in the form for FavouriteViewHolder but still empty no data
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_favourite_city, parent, false)
        return FavouriteViewHolder(itemView)
    }

    //take in the empty FavouriteViewHolder returned by onCreateViewHolder then bind the data to the FavouriteViewHolder
    override fun onBindViewHolder(favViewHolder: FavouriteViewHolder, position: Int) {

        Log.i("FavouriteAdapter", "onBindViewHolder: position: $position")
        val city = favCityList[position] //get the current city from cityList
        favViewHolder.setFavData(city, position) //setData to the empty viewholder

    }

    override fun getItemCount(): Int = favCityList.size


    //create FavouriteViewHolder. For ViewHolder we always extend RecyclerView.ViewHolder
    inner class FavouriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var currentPosition: Int = -1
        private var currentCity: City? = null

        private val txvFavCityName = itemView.findViewById<TextView>(R.id.txv_fav_city_name)
        private val imvFavCityImage = itemView.findViewById<ImageView>(R.id.imv_fav_city)

        fun setFavData(city: City, position: Int){

            txvFavCityName.text = city.name
            imvFavCityImage.setImageResource(city.imageId)

            this.currentCity = city
            this.currentPosition = position
        }
    }
}