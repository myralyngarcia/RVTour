package com.myralyn.rvtour.city

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.myralyn.rvtour.R

//context of the calling activity or fragment
//cityList we receive from calling activity or fragment

class CityAdapter (val context: Context, var cityList: ArrayList<City>): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    //purpose of this method is to create the limited number of ViewHolder object which will be use to display items on screen
    //these items will be recycled as we scroll up of scroll down. Note these item views are still empty here
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {

        Log.i("CityAdapter", "onCreateViewHolder: ViewHolder created")

        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item_city, parent, false)
        return CityViewHolder(itemView)
    }

    // once the empty viewHolder are created in onCreateViewHolder is internally passed to these fun onBindViewHolder
    // it is the job of fun onBindViewHolder to set data to the empty viewHolder and present it to the user with Image, name of city, favourite, delete
    // this function is called for each item present in the list
    override fun onBindViewHolder(cityViewHolder: CityViewHolder, position: Int) {

        // ViewHolder are created once, set of 7 and being re-cycled each time a scroll occurs
        Log.i("CityAdapter", "onBindViewHolder: position: $position")

        val city = cityList[position] //get the current city from the cityList
        cityViewHolder.setData(city, position) //set data to viewObjects

        //we know that for each item in the list fun onBindViewHolder is executed
        //so inside this method we call setListener, this is going to set Listener on the viewObjects, CityViewHolder
        cityViewHolder.setListeners() //create setLisners in class CityViewHolder

    }

    //return the size of cityList object
    override fun getItemCount(): Int = cityList.size

    inner class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var currentPosition: Int = -1
        private var currentCity: City? = null

        private val txvCityName = itemView.findViewById<TextView>(R.id.txv_city_name)
        private val imvCityImage = itemView.findViewById<ImageView>(R.id.imv_city)
        private val imvDelete = itemView.findViewById<ImageView>(R.id.imv_delete)
        private val imvFavorite = itemView.findViewById<ImageView>(R.id.imv_favorite)

        private val icFavoriteFilledImage = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_favorite_filled, null)
        private val icFavoriteBorderedImage = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_favorite_bordered, null)

        fun setData(city: City, position: Int){

            txvCityName.text = city.name
            imvCityImage.setImageResource(city.imageId)

            if(city.isFavorite)
                imvFavorite.setImageDrawable(icFavoriteFilledImage)
            else
                imvFavorite.setImageDrawable(icFavoriteBorderedImage)

            this.currentCity = city
            this.currentPosition = position
        }

        fun setListeners(){
            //we set listeners on the imvDelete view and imvFavorite view
            imvDelete.setOnClickListener(this@CityViewHolder) //need to implement setOnClickListener in CityViewHolder class, implement the interface OnClickListener and override the abstract method, onClick, by alt+enter
            imvFavorite.setOnClickListener(this@CityViewHolder)
        }

        override fun onClick(v: View?) {
            //Here write statement on which icon is actually clicked by the user
            when (v!!.id) {
                R.id.imv_delete -> deleteItem() //implement this method
                R.id.imv_favorite -> addToFavorite() //implement this method
            }
        }

        private fun deleteItem() {
            //To delete, first delete the item from the cityList from the position it was clicked
            cityList.removeAt(currentPosition)
            //then notify the adapter after removing the item
            notifyItemRemoved(currentPosition) //this is structural event which means the index of the item will be updated internally ony, but visually for the ViewHolder object the item index is not updated, for this we need to write one more statement
            notifyItemRangeChanged(currentPosition, cityList.size) //it forces the adpater to update the ViewHolder objects from this currentPosition onward based on the new cityList.size

            //here is another condition when user mark city as favourite then delete it, we need the code below
            VacationSpots.favoriteCityList.remove(currentCity!!)
        }

        private fun addToFavorite() {
            //first need to write code to toggle the boolean value to 'City.isFavorite' when user click the imv_favorite
            currentCity?.isFavorite = !(currentCity?.isFavorite!!)

            if (currentCity?.isFavorite!!){   //if it is favourite - update icon and add  the city object to favorite list
                imvFavorite.setImageDrawable(icFavoriteFilledImage)
                VacationSpots.favoriteCityList.add(currentCity!!)
            }else {   // else it is not favourite - update icon and remove the city object from the favorite list
                imvFavorite.setImageDrawable(icFavoriteBorderedImage)
                VacationSpots.favoriteCityList.remove(currentCity!!)
            }
        }
    }
}