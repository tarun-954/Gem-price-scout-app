package com.example.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GemAdapter(private val gems: List<GemItem>) : RecyclerView.Adapter<GemAdapter.GemViewHolder>() {

    class GemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gemImage: ImageView = view.findViewById(R.id.gemImage)
        val gemName: TextView = view.findViewById(R.id.gemName)
        val gemPrice: TextView = view.findViewById(R.id.gemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gem, parent, false)
        return GemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GemViewHolder, position: Int) {
        val gem = gems[position]
        holder.gemName.text = gem.name
        holder.gemPrice.text = "₹${gem.price}"

        Glide.with(holder.itemView.context).apply {
            if (gem.imageResId != null) {
                load(gem.imageResId)
                    .into(holder.gemImage)
            } else {
                load(gem.imageUrl)
                    .placeholder(R.drawable.gem)
                    .into(holder.gemImage)
            }
        }

    }

    override fun getItemCount() = gems.size
}
