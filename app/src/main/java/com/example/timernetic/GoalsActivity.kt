package com.example.timernetic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.timernetic.utils.goalData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GoalsActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    //Time picker binding
    private lateinit var minGoalPicker : TimePicker
    private lateinit var maxGoalPicker : TimePicker

    private lateinit var addGoals : Button
    private lateinit var viewGoals : Button

    private lateinit var minGoal : String
    private lateinit var maxGoal : String

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var dataReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { item ->
            // Handle navigation item clicks here
            when (item.itemId) {
                R.id.nav_option1 -> {
                    val intent = Intent(this, GroupActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_option2 -> {
                    val intent = Intent(this, GoalsActivity::class.java)
                    startActivity(intent)
                }


                R.id.nav_option3 -> {
                    // Handle option 3 click
                    val intent = Intent(this, ViewTimesheetActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_option4 -> {
                    // Handle option 4 click
                    val intent = Intent(this, TimesheetActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_option5 -> {
                    // Handle option 5 click
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setContentView(R.layout.activity_goals)


        auth = FirebaseAuth.getInstance()

        // data reference
        dataReference = FirebaseDatabase.getInstance().reference
            .child("Goals").child(auth.currentUser?.uid.toString())

        addGoals = findViewById(R.id.AddGoal)
        viewGoals = findViewById(R.id.ViewGoal)

        minGoalPicker = findViewById(R.id.timePickermin)
        maxGoalPicker = findViewById(R.id.timePickermax)

        setupTimePicker()

        viewGoals.setOnClickListener{
            val intent = Intent(this, GetGoalsActivity::class.java)
        }

        addGoals.setOnClickListener {
            val goals = goalData(minGoal, maxGoal)
            dataReference.push().setValue(goals).addOnCompleteListener{
                if (it.isSuccessful){
                    Toast.makeText(this, "Goal Has Been Set", Toast.LENGTH_SHORT)
                }
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupTimePicker() {

        minGoalPicker.setOnTimeChangedListener{ _, hourOfDay, minute ->
            val dayHour = when {
                hourOfDay == 0 -> {
                    hourOfDay + 12
                }
                hourOfDay > 12 -> {
                    hourOfDay - 12
                }
                else -> hourOfDay
            }

            val format = when {
                hourOfDay >= 12 -> {
                    "PM"
                }
                else -> "AM"
            }

            val hour = if (dayHour < 10) "0$dayHour" else dayHour
            val min = if (minute < 10) "0$minute" else minute

            val text = getString(R.string.selected_time) + " " + hour + " : " + min + " " + format
            minGoal = text
        }

        maxGoalPicker.setOnTimeChangedListener{ _, hourOfDay, minute ->
            val dayHour = when {
                hourOfDay == 0 -> {
                    hourOfDay + 12
                }
                hourOfDay > 12 -> {
                    hourOfDay - 12
                }
                else -> hourOfDay
            }

            val format = when {
                hourOfDay >= 12 -> {
                    "PM"
                }
                else -> "AM"
            }

            val hour = if (dayHour < 10) "0$dayHour" else dayHour
            val min = if (minute < 10) "0$minute" else minute

            val text = getString(R.string.selected_max_time) + " " + hour + " : " + min + " " + format
            maxGoal = text
        }
    }

}