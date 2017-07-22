package com.moodi.tagsearch

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.moodi.tagsearch.util.TagCallback


/**
 * Created by moodi on 22/07/2017.
 */

class TagSearch @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        if (s?.isNotEmpty()!!) {
            cancelButton?.visibility = VISIBLE
        } else {
            cancelButton?.visibility = GONE
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    var selectedTag: TextView? = null
    var searchViewEditText: EditText? = null
    var tagListeners: TagCallback? = null
    var cancelButton: ImageView? = null

    var tagRadius: Float = 1f
    var tagColor = R.color.colorAccent


    init {

        val mainView = LayoutInflater.from(context)
                .inflate(R.layout.search_view_layout, this, true)
        selectedTag = mainView.findViewById(R.id.selected_tag) as TextView
        cancelButton = mainView.findViewById(R.id.cancel_search) as ImageView
        searchViewEditText = mainView.findViewById(R.id.search_text_view) as EditText

        initializeTagView()
        addTagClickListener()
        addTagLongClickListener()
        addCancelListener()
        detectRemoveKeyEvent()


        searchViewEditText?.addTextChangedListener(this)

    }

    private fun addCancelListener() {
        cancelButton?.setOnClickListener { searchViewEditText?.setText("") }
    }

    private fun detectRemoveKeyEvent() {
        searchViewEditText?.setOnKeyListener({ _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                tagListeners?.onTagRemoved(selectedTag?.text)
                selectedTag?.text = ""
                selectedTag?.visibility = GONE
            }
            false
        })
    }


    fun addTagCallBack(tagListeners: TagCallback) {
        this.tagListeners = tagListeners
    }

    fun addTag(tag: String) {
        selectedTag?.visibility = VISIBLE

        selectedTag?.text = tag
        this.tagListeners?.onTagAdded(selectedTag?.text)
    }

    private fun addTagClickListener() {
        selectedTag?.setOnClickListener { this.tagListeners?.onTagClick(selectedTag!!.text) }
    }

    private fun addTagLongClickListener() {
        selectedTag?.setOnLongClickListener {
            this.tagListeners?.onTagLongClick(selectedTag?.text)
            true
        }
    }

    private fun addSearchButtonListener() {

        searchViewEditText?.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_SEARCH) {
                this.tagListeners?.onSearchPressed(textView.text)
            }
            false
        }
    }


    private fun initializeTagView() {

        val tagDrawable = GradientDrawable()

        with(tagDrawable) {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = tagRadius
            setColor(ContextCompat.getColor(context, tagColor))

        }

        selectedTag?.background = tagDrawable

    }

}