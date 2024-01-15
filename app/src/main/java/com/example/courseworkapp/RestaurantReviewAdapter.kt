package com.example.courseworkapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantReviewAdapter (private val reviewList : ArrayList<Review>) : RecyclerView.Adapter<RestaurantReviewAdapter.RestaurantReviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurantreviewlayout, parent, false)
        return RestaurantReviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantReviewHolder, position: Int) {
        val currentitem = reviewList[position]

        holder.email.text = currentitem.email
        val currentScore = currentitem. score
        holder.score.text = "$currentScore / 5"
        holder.location.text = currentitem.location
        holder.reviewtext.text = currentitem.reviewtext

        if (currentitem.img.equals("null")) {
            holder.imgHolder.visibility = View.GONE
            holder.img.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class RestaurantReviewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val email : TextView = itemView.findViewById(R.id.emailPlaceholder)
        val score : TextView = itemView.findViewById(R.id.scorePlaceholder)
        val img : ImageView = itemView.findViewById(R.id.imgPlaceholder)
        val imgHolder : LinearLayout = itemView.findViewById(R.id.restaurantReviewImgHolder)
        val location : TextView = itemView.findViewById(R.id.locationPlaceholder)
        val reviewtext : TextView = itemView.findViewById(R.id.reviewPlaceholder)

    }
}