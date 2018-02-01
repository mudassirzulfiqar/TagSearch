package com.moodi.tagsearch.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moodi.tagsearch.R

/**
 * Created by moodi on 19/01/2018.
 */
class TagsAdapter(val context: Context, var tags: ArrayList<String>, var clickListener: (String) -> Unit) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val posistionTag = tags[position]

        holder?.tagText?.text = posistionTag
        holder?.tagText?.setOnClickListener {
            clickListener(posistionTag)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.tag_layout, parent, false)
        return ViewHolder(inflate)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tagText: TextView? = itemView.findViewById(R.id.tag) as TextView

    }

    fun addList(hotTags: ArrayList<String>) {
        tags.clear()
        tags.addAll(hotTags)
        notifyDataSetChanged()
    }
}