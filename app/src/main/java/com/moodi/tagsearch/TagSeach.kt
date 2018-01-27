package com.moodi.tagsearch

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.moodi.tagsearch.ui.TagsAdapter
import com.moodi.tagsearch.util.TagCallback
import com.moodi.tagsearch.util.background


/**
 * Created by moodi on 22/07/2017.
 */

class TagSearch @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr), TextWatcher {


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
    var innerArea: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var tagArea: RelativeLayout? = null
    var animatorView: LinearLayout? = null

    //default Properties
    var mTagRadius: Float = 1f

    @ColorInt
    var mTagColor = R.color.colorAccent
    var mSelectedTagText = ""
    var mTagDrawable = GradientDrawable()
    var mTextSize = 12.0F;
    var mHint = "Search any thing";
    var mSearchBarRadius = 0f;

    init {
        useCompatPadding = true
        elevation = 4f

        val mainView = LayoutInflater.from(context)
                .inflate(R.layout.search_view_layout, this, true)

        mSelectedTag = mainView.findViewById(R.id.selected_tag) as TextView
        mCancelButton = mainView.findViewById(R.id.cancel_search) as ImageView
        mSearchViewEditText = mainView.findViewById(R.id.search_text_view) as EditText
        innerArea = mainView.findViewById(R.id.inner) as RelativeLayout
        recyclerView = mainView.findViewById(R.id.recycler) as RecyclerView
        tagArea = mainView.findViewById(R.id.tag_view) as RelativeLayout
        animatorView = mainView.findViewById(R.id.whole_animator) as LinearLayout

        setTagColor(mTagColor)
        addTagClickListener()
        addTagLongClickListener()
        addCancelListener()
        detectRemoveKeyEvent()
        addSearchButtonListener()

        initRecyclerView(context)

        mSearchViewEditText?.addTextChangedListener(this)


        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.TagSearch, 0, 0)

        try {

            setTagText(a.getString(R.styleable.TagSearch_defaultTagText))
            setSearchHint(a.getString(R.styleable.TagSearch_searchHint))
            setTagColor(a.getColor(R.styleable.TagSearch_tagColor, 0))
            setTagRadius(a.getFloat(R.styleable.TagSearch_tagRadius, 0F))

        } finally {
            a.recycle()
        }


    }

    private fun initRecyclerView(context: Context) {


        val adapter
                = TagsAdapter(context, arrayListOf(
                "Android", "Code", "Android", "Code", "Android", "Code", "Android", "Code"
        ), {
            setTagText(it)
//            doTransition()
            tagArea?.visibility = View.GONE
        })

        with(recyclerView) {

            this?.addItemDecoration(
                    SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.item_space), resources.getDimensionPixelOffset(R.dimen.item_space)))

            ChipsLayoutManager.newBuilder(context)
                    //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                    .setChildGravity(Gravity.CENTER)
                    //whether RecyclerView can scroll. TRUE by default
                    .setScrollingEnabled(true)
                    //set maximum views count in a particular row
                    .setMaxViewsInRow(10)
                    //set gravity resolver where you can determine gravity for item in position.
                    //This method have priority over previous one
                    .setGravityResolver { Gravity.CENTER }
                    //you are able to break row due to your conditions. Row breaker should return true for that views
                    //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                    //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_FILL_VIEW)
                    .build().let {
                this?.layoutManager = it
            }


            this?.adapter = adapter
        }


    }

    private fun addCancelListener() {
        mCancelButton?.setOnClickListener { mSearchViewEditText?.setText("") }
    }

    private fun detectRemoveKeyEvent() {
        mSearchViewEditText?.setOnKeyListener({ view, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_DEL) {

                if (mSearchViewEditText?.text?.length == 0) {
                    mTagListeners?.onTagRemoved(mSelectedTag?.text)
                    mSelectedTag?.text = ""

                    doTransition()
                    mSelectedTag?.visibility = GONE
                    tagArea?.visibility = VISIBLE
                }
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

    fun setSearchBarRadius(radius: Float) {
        this.mSearchBarRadius = radius
        setRadius(mSearchBarRadius)

    }

    //extention Funtions

    fun doTransition() {
        TransitionManager.beginDelayedTransition(animatorView)
    }

}