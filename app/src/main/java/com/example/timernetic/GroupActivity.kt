package com.example.timernetic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timernetic.utils.groupData
import com.example.timernetic.utils.taskData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.*

class GroupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dataReference: DatabaseReference
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    //page components
    private lateinit var closeGroupPopUpBtn:ImageView
    private lateinit var taskGroupName:EditText
    private lateinit var addGroupbtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        drawerLayout = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { item ->
            // Handle navigation item clicks here
            when (item.itemId) {
                R.id.nav_option1 -> {

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
        setContentView(R.layout.activity_group)
        //set page components
        closeGroupPopUpBtn = findViewById(R.id.closeGroupPopUpBtn)
        closeGroupPopUpBtn.setOnClickListener{
            val intent = Intent(this@GroupActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        addGroupbtn = findViewById(R.id.addGroupbtn)
        taskGroupName = findViewById(R.id.taskGroupName)
        addGroupbtn.setOnClickListener{

            val groupName = taskGroupName.text.toString()
            if (groupName.isNotEmpty()) {
                auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val dataReference = FirebaseDatabase.getInstance().reference
                        .child("Group")
                        .child(userId)

                    // Check if the category already exists
                    val GroupQuery = dataReference.orderByChild("GroupName").equalTo(groupName)
                    GroupQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Category already exists
                                Toast.makeText(applicationContext, "Group already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                // Category does not exist, add it to the database
                                val newgroup=dataReference.push()
                                newgroup.child("GroupName").setValue(groupName).addOnCompleteListener { groupTask ->
                                    if (groupTask.isSuccessful) {
                                        Toast.makeText(applicationContext, "group added successfully", Toast.LENGTH_SHORT).show()
                                        taskGroupName.text = null
                                    } else {
                                        Toast.makeText(applicationContext, groupTask.exception?.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(applicationContext, databaseError.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "User is not authenticated", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Please enter a Group Name", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}