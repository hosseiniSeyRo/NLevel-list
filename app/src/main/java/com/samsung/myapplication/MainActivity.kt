package com.samsung.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.samsung.myapplication.nLevel.NLevelAdapter
import com.samsung.myapplication.nLevel.NLevelItem
import com.samsung.myapplication.nLevel.NLevelView
import com.samsung.myapplication.nLevel.SomeObject
import java.util.*


class MainActivity : AppCompatActivity() {
    var list: MutableList<NLevelItem>? = null
    var listView: ListView? = null

    var colors =
        intArrayOf(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED, Color.CYAN, Color.DKGRAY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNLevelExpandableListView(getDummyData())
    }

    private fun getDummyData(): List<SomeObject> {
        val myList = mutableListOf<SomeObject>()

        val mySubList = mutableListOf<SomeObject>()
        val mySubSubList = mutableListOf<SomeObject>()
        val mySubSubSubList = mutableListOf<SomeObject>()
        mySubSubSubList.add(SomeObject("41", null))
        mySubSubSubList.add(SomeObject("42", null))

        mySubSubList.add(SomeObject("31", mySubSubSubList))
        mySubSubList.add(SomeObject("32", null))

        mySubList.add(SomeObject("21", mySubSubList))
        mySubList.add(SomeObject("22", null))
        mySubList.add(SomeObject("23", mySubSubList))

        myList.add(SomeObject("1", mySubList))
        myList.add(SomeObject("11", mySubList))
        myList.add(SomeObject("111", null))
        myList.add(SomeObject("1111", null))

        return myList
    }

    private fun initNLevelExpandableListView(baseList: List<SomeObject>) {
        listView = findViewById<View>(R.id.listView) as ListView
        list = ArrayList()
        val inflater = LayoutInflater.from(this)
        nestedLoop(baseList, null, inflater, 0)
        val adapter = NLevelAdapter(list)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = OnItemClickListener { arg0, arg1, arg2, arg3 ->
            (listView!!.adapter as NLevelAdapter).toggle(arg2)
            (listView!!.adapter as NLevelAdapter).filter.filter()
        }
    }

    private fun nestedLoop(
        myList: List<SomeObject>,
        nLevelItem: NLevelItem?,
        inflater: LayoutInflater,
        level: Int
    ) {
        try {
            var childrenSize: Int
            for (i in myList.indices) {
                val item = myList[i]
                childrenSize = 0
                item.childsList?.let {
                    childrenSize = it.size
                }
                val parent =
                    getListItemView(i, item.name, nLevelItem, inflater, level, childrenSize <= 0)
                list!!.add(parent)
                if (childrenSize > 0) {
                    nestedLoop(item.childsList!!, parent, inflater, level + 1)
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun getListItemView(
        itemRow: Int,
        Title: String,
        nLevelItem: NLevelItem?,
        inflater: LayoutInflater,
        level: Int,
        isLast: Boolean
    ): NLevelItem {
        return NLevelItem(SomeObject(Title, null), nLevelItem, object : NLevelView {
            override fun getView(item: NLevelItem?): View? {
                val rootView = inflater.inflate(R.layout.list_item, null)
                rootView.setBackgroundColor(colors[level])

                val listItemContainer =
                    rootView.findViewById<View>(R.id.listItemContainer) as ViewGroup
                val tvTitle = rootView.findViewById<View>(R.id.textView) as TextView
                val arrowIcon = rootView.findViewById<View>(R.id.arrowIcon) as ImageView

                val mlp = tvTitle.layoutParams as MarginLayoutParams
                mlp.setMargins(level * 50, 5, 5, 5)
                tvTitle.text = (item!!.wrappedObject as SomeObject).name

                if (isLast) {
                    arrowIcon.visibility = View.GONE

                    rootView.setOnClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Clicked on: $Title",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return rootView
            }
        })
    }
}