package com.markul.android.ui.Personal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.markul.android.MarkulApplication
import com.markul.android.R

class AlbumAdapter (val picUrlList:ArrayList<String?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val picImage: ImageView = itemView.findViewById(R.id.picImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view=
            LayoutInflater.from(MarkulApplication.context).inflate(R.layout.ablum_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(picUrlList[position].toString())
            .into((holder as AlbumAdapter.ViewHolder).picImage)
    }

    override fun getItemCount()=picUrlList.size
}