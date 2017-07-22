package com.moodi.tagsearch.util

/**
 * Created by moodi on 22/07/2017.
 */

interface TagCallback {
    fun onTagRemoved(removedTag: CharSequence?)
    fun onTagAdded(tag: CharSequence?)
    fun onTagClick(tag: CharSequence)
    fun onTagLongClick(text: CharSequence?)
    fun onSearchPressed(textView: CharSequence)

}