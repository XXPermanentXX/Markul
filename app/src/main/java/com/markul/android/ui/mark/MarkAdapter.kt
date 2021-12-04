package com.markul.android.ui.mark


import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.markul.android.R


class MarkAdapter(val picUrlList:ArrayList<String>,val maxNum:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //加号布局
    val ADD_ITEM = 1
    //图片布局
    val PIC_ITEM = 2

    private var onItemClickListener: OnItemClickListener? = null

    //加号布局
    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    //图片布局
    inner class PicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pic: ImageView = itemView.findViewById(R.id.ivImage)
        val del: ImageView = itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            ADD_ITEM->{
                var view=LayoutInflater.from(parent.context)
                    .inflate(R.layout.add_item,parent,false)
                return AddViewHolder(view)
            }
            else->{
                var view=LayoutInflater.from(parent.context)
                    .inflate(R.layout.item,parent,false)
                return PicViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        //加号的布局
        if (holder is AddViewHolder) {
            //增加的布局
            holder.itemView.setOnClickListener {
                onItemClickListener?.onItemAddClick(position)
            }
        }
        //加载图片的布局
        else {
            Glide.with(holder.itemView.context).load(picUrlList[position].toString())
                .into((holder as PicViewHolder).pic)
            holder.pic.setOnClickListener {
                onItemClickListener?.onItemPicClick(position)
            }
            holder.del.setOnClickListener {
                onItemClickListener?.onItemDelClick(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            ADD_ITEM
        } else {
            PIC_ITEM
        }
    }

    override fun getItemCount(): Int {
        return if(picUrlList.size<maxNum){
            picUrlList.size+1
        }else{
            picUrlList.size
        }
    }

    fun setOnMyClickListener(onClickListener:  OnItemClickListener?) {
        onItemClickListener = onClickListener
    }

    interface OnItemClickListener{
        //点击增加按键
        fun onItemAddClick(position: Int)

        //点击删除按键
        fun onItemDelClick(position: Int)

        //点击图片
        fun onItemPicClick(position: Int)
    }

}