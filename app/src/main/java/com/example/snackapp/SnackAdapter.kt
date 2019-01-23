package com.example.snackapp

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class SnackAdapter(private val context: Context, private val dataSource: ArrayList<Snack>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var rowView = convertView
        // Current snack at the position within the data source
        val snack: Snack = dataSource[position]
        val holder: SnackViewHolder

        if (rowView == null) {
            holder = SnackViewHolder()
            rowView = inflater.inflate(R.layout.list_snack, parent, false)

            holder.checkBox = rowView!!.findViewById(R.id.checkbox) as CheckBox
            holder.textView = rowView.findViewById(R.id.snack) as TextView

            rowView.tag = holder
        } else {
            holder = rowView.tag as SnackViewHolder
        }

        // Set the text of current text view within an item
        holder.textView!!.text = snack.snackName
        // Set color of snack based on type
        if (snack.isVeggie) {
            holder.textView!!.setTextColor(ContextCompat.getColor(context, R.color.colorVeggie))
        } else {
            holder.textView!!.setTextColor(ContextCompat.getColor(context, R.color.colorNonVeggie))
        }

        // The current view's checkbox state is set to the boolean value of the current snack
        holder.checkBox!!.isChecked = snack.isSelected
        // The checkbox behavior is that if it's clicked its state will become the current snack isSelected state
        holder.checkBox!!.setOnClickListener {
            snack.isSelected = holder.checkBox!!.isChecked
        }

        return rowView
    }

    private inner class SnackViewHolder {
        var checkBox: CheckBox? = null
        var textView: TextView? = null
    }
}