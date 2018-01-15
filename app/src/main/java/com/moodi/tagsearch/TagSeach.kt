package com.moodi.tagsearch

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
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
import com.moodi.tagsearch.util.background

/**
 * Created by moodi on 22/07/2017.
 */

class TagSearch @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), TextWatcher {


    override fun afterTextChanged(s: Editable?) {
        if (s?.isNotEmpty()!!) {
            mCancelButton?.visibility = VISIBLE
        } else {
            mCancelButton?.visibility = GONE
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    //Views
    var mSelectedTag: TextView? = null
    var mSearchViewEditText: EditText? = null
    var mTagListeners: TagCallback? = null
    var mCancelButton: ImageView? = null

    //default Properties
    var mTagRadius: Float = 1f

    @ColorInt
    var mTagColor = R.color.colorAccent
    var mSelectedTagText = ""
    var mTagDrawable = GradientDrawable()
    var mTextSize = 12.0F;
    var mHint = "Search any thing";

    init {

        val mainView = LayoutInflater.from(context)
                .inflate(R.layout.search_view_layout, this, true)

        mSelectedTag = mainView.findViewById(R.id.selected_tag) as TextView
        mCancelButton = mainView.findViewById(R.id.cancel_search) as ImageView
        mSearchViewEditText = mainView.findViewById(R.id.search_text_view) as EditText

        setTagColor(mTagColor)
        addTagClickListener()
        addTagLongClickListener()
        addCancelListener()
        detectRemoveKeyEvent()
        addSearchButtonListener()

        mSearchViewEditText?.addTextChangedListener(this)
    }

    private fun addCancelListener() {
        mCancelButton?.setOnClickListener { mSearchViewEditText?.setText("") }
    }

    private fun detectRemoveKeyEvent() {
        mSearchViewEditText?.setOnKeyListener({ _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                mTagListeners?.onTagRemoved(mSelectedTag?.text)
                mSelectedTag?.text = ""
                mSelectedTag?.visibility = GONE
            }
            false
        })
    }


    fun addTagCallBack(tagListeners: TagCallback) {
        this.mTagListeners = tagListeners
    }


    private fun addTagClickListener() {
        mSelectedTag?.setOnClickListener { this.mTagListeners?.onTagClick(mSelectedTag!!.text) }
    }

    private fun addTagLongClickListener() {
        mSelectedTag?.setOnLongClickListener {
            this.mTagListeners?.onTagLongClick(mSelectedTag?.text)
            true
        }
    }

    private fun addSearchButtonListener() {

        mSearchViewEditText?.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_SEARCH) {
                this.mTagListeners?.onSearchPressed(textView.text)
            }
            false
        }
    }


    private fun setTagTextSize(textSize: Float) {
        this.mTextSize = textSize
        mSelectedTag?.textSize = textSize


    }

    fun setSearchHint(hint: String) {
        this.mHint = hint
        mSearchViewEditText?.hint = mHint
    }


    fun setTagText(tag: String) {
        mSelectedTagText = tag

        mSelectedTag?.visibility = VISIBLE
        mSelectedTag?.text = tag
        this.mTagListeners?.onTagAdded(mSelectedTag?.text)
    }


    fun setTagColor(@ColorInt color: Int) {

        mTagColor = color
        mSelectedTag
                .background()
                .setColor(mTagColor)

    }


    fun setTagRadius(radius: Float) {
        mTagRadius = radius
        mSelectedTag
                .background()
                .cornerRadius = mTagRadius
    }


    fun initialize() {

    }

    //extention Funtions


}