@file:JvmName("AndroidUtils")

package com.moodi.tagsearch.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.TextView
import com.moodi.tagsearch.TagSearch


/**
 * Created by moodi on 15/01/2018.
 */


fun Context.d(message: String) {

    val activity = this as Activity

    Log.d(activity.packageName, message)
}

fun TextView?.background(): GradientDrawable {
    return this?.background as GradientDrawable
}

fun TagSearch.addCallbacks(
        onTagRemoved: (CharSequence?) -> Unit
        , onTagAdded: (CharSequence?) -> Unit
        , onTagClick: (CharSequence?) -> Unit
        , onTagLongClick: (CharSequence?) -> Unit
        , onSearchPressed: (CharSequence?) -> Unit) {

    this.addTagCallBack(object : TagCallback {
        override fun onTagRemoved(removedTag: CharSequence?) {
            onTagRemoved.invoke(removedTag)
        }

        override fun onTagAdded(tag: CharSequence?) {
            onTagAdded.invoke(tag)
        }

        override fun onTagClick(tag: CharSequence) {
            onTagClick.invoke(tag)
        }

        override fun onTagLongClick(text: CharSequence?) {
            onTagLongClick.invoke(text)
        }

        override fun onSearchPressed(textView: CharSequence) {
            onSearchPressed.invoke(textView)
        }

    })
}