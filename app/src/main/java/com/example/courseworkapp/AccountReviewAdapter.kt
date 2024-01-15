package com.example.courseworkapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AccountReviewAdapter(private val reviewList : ArrayList<Review>) : RecyclerView.Adapter<AccountReviewAdapter.AccountReviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.accountreviewlayout, parent, false)
        return AccountReviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AccountReviewHolder, position: Int) {
        val currentitem = reviewList[position]

        holder.rname.text = currentitem.rname
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

    class AccountReviewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val rname : TextView = itemView.findViewById(R.id.restaurantPlaceholder)
        val score : TextView = itemView.findViewById(R.id.scorePlaceholder)
        val img : ImageView = itemView.findViewById(R.id.imgPlaceholder)
        val imgHolder : LinearLayout = itemView.findViewById(R.id.accountReviewImgHolder)
        val location : TextView = itemView.findViewById(R.id.locationPlaceholder)
        val reviewtext : TextView = itemView.findViewById(R.id.reviewPlaceholder)

    }

}