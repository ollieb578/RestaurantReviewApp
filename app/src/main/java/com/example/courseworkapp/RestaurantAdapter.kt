package com.example.courseworkapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantAdapter (private val restaurantList : ArrayList<Restaurant>, private val onItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurantlayout, parent, false)
        return RestaurantHolder(itemView, onItemClicked)
    }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        val currentitem = restaurantList[position]

        holder.rid.text = currentitem.rid
        holder.name.text = currentitem.name
        val currentScore = currentitem. avgScore
        holder.score.text = "$currentScore / 5"
        holder.location.text = currentitem.location

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class RestaurantHolder(itemView : View,
                           private val onItemClicked: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val rid : TextView = itemView.findViewById(R.id.restaurantId)
        val name : TextView = itemView.findViewById(R.id.restaurantPlaceholder)
        val score : TextView = itemView.findViewById(R.id.scorePlaceholder)
        val location : TextView = itemView.findViewById(R.id.locationPlaceholder)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }


    }

}