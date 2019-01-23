package com.example.snackapp

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private var snackListView: ListView? = null
    private var snackAdapter: SnackAdapter? = null
    private var veggieCheckBox: CheckBox? = null
    private var nonVeggieCheckBox: CheckBox? = null
    private var veggieList: ArrayList<Snack>? = null
    private var nonVeggieList: ArrayList<Snack>? = null
    private var fullSnackList: ArrayList<Snack>? = null
    private var submitBtn: Button? = null

    // Default list of snacks
    private val defaultVeggie: Array<String> =
        arrayOf("French fries", "Veggieburger", "Carrots", "Banana", "Apple", "Milkshake")
    private val defaultNonVeggie: Array<String> =
        arrayOf("Cheeseburger", "Hamburger", "Hot dog")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        veggieCheckBox = findViewById(R.id.veggieCheck)
        nonVeggieCheckBox = findViewById(R.id.nonVeggieCheck)
        snackListView = findViewById(R.id.listItems)
        submitBtn = findViewById(R.id.submitBtn)

        // If one of the top checkboxes is clicked, check to see which list needs to be shown to the list view adapter
        veggieCheckBox!!.setOnClickListener {
            updateListView()
        }
        nonVeggieCheckBox!!.setOnClickListener {
            updateListView()
        }

        // If submit button is clicked display a popup dialog with selected item
        submitBtn!!.setOnClickListener {
            // Use the full list of snacks, this would mean that the two top checkboxes will act as a show/hide button
            // to display the snack type the user want to choose at that moment, a use case is if a user wants to choose
            // both snack type but only want to look at the veggies snack first then non-veggie snacks later.
            // Filter out items that are selected then get the names of them
            val items = fullSnackList!!.filter { e -> e.isSelected }.map { e -> e.snackName }.toTypedArray()
            val builder = AlertDialog.Builder(this)

            builder.setTitle(R.string.selectedSnacks)
            builder.setItems(items) { _, _ -> }
            // Just quietly dismiss the dialog when clicking ok
            builder.setPositiveButton(R.string.ok) { _, _ -> }
            // Set everything back to default state when the summary dialog is dismissed
            builder.setOnDismissListener {
                defaultState()
            }
            builder.show()
        }

        makeDefaultList()
        updateListView(fullSnackList!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_add -> {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.add_snack_popup, null)
            val addSnackName  = dialogLayout.findViewById<EditText>(R.id.addSnackName)
            val addIsVeggie = dialogLayout.findViewById<CheckBox>(R.id.addIsVeggie)

            builder.setTitle(R.string.addSnackTitle)
            builder.setView(dialogLayout)
            builder.setNegativeButton(R.string.cancel) { _, _ -> }
            builder.setPositiveButton(R.string.save) { _, _ ->
                val newSnack = Snack(false, addSnackName.text.toString(), addIsVeggie.isChecked)
                if (newSnack.isVeggie) {
                    veggieList!!.add(newSnack)
                } else {
                    nonVeggieList!!.add(newSnack)
                }
                fullSnackList!!.add(newSnack)
                updateListView()
            }
            builder.show()
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    /**
     * Method to create list of snacks with default data
     */
    private fun makeDefaultList() {
        veggieList = ArrayList()
        for (e in defaultVeggie) {
            veggieList!!.add(Snack(false, e, true))
        }
        nonVeggieList = ArrayList()
        for (e in defaultNonVeggie) {
            nonVeggieList!!.add(Snack(false, e, false))
        }
        fullSnackList = ArrayList()
        fullSnackList!!.addAll(veggieList!!)
        fullSnackList!!.addAll(nonVeggieList!!)
    }

    /**
     * Helper method to update list view adapter with specific list
     */
    private fun updateListView(list: ArrayList<Snack>) {
        snackAdapter = SnackAdapter(this, list)
        snackListView!!.adapter = snackAdapter
    }

    /**
     * Helper method to update list view with current shown list of snacks
     */
    private fun updateListView() {
        updateListView(getPresentList())
    }

    /**
     * Get which list to display in list view depending on the states of the two top checkboxes
     */
    private fun getPresentList() : ArrayList<Snack> {
        if (veggieCheckBox!!.isChecked && !nonVeggieCheckBox!!.isChecked) {
            return veggieList!!
        }
        if (nonVeggieCheckBox!!.isChecked && !veggieCheckBox!!.isChecked) {
            return nonVeggieList!!
        }
        if (!nonVeggieCheckBox!!.isChecked && !veggieCheckBox!!.isChecked) {
            return ArrayList()
        }
        return fullSnackList!!
    }

    /**
     * Method to set everything to default state
     */
    private fun defaultState() {
        veggieCheckBox!!.isChecked = true
        nonVeggieCheckBox!!.isChecked = true
        makeDefaultList()
        updateListView(fullSnackList!!)
    }
}
