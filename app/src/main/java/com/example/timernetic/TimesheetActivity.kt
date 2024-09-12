package com.example.timernetic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timernetic.utils.ImageEncoder
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TimesheetActivity : AppCompatActivity() {

    private lateinit var taskPictureIV: ImageView
    private var pic: Bitmap? = null
    //Task data
    private lateinit var closeGroupPopUpBtn: ImageView
    private lateinit var taskName: EditText
    private lateinit var taskGroupName: EditText
    private lateinit var taskDescription: EditText
    private lateinit var taskStartDate: EditText
    private lateinit var taskEndDate: EditText
    //take pic btn
    private lateinit var taskPicIV: ImageView
    private lateinit var addGroupbtn:ImageView
    //Database
    private lateinit var auth: FirebaseAuth
    private lateinit var dataReference: DatabaseReference

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet)

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
        setContentView(R.layout.activity_timesheet)
        //ADD TO DATABASE
        addGroupbtn= findViewById(R.id.addGroupbtn)
        taskName= findViewById(R.id.taskName)
        taskGroupName= findViewById(R.id.taskGroupName)
        taskDescription= findViewById(R.id.taskDescription)
        taskStartDate= findViewById(R.id.taskStartDate)
        taskEndDate= findViewById(R.id.taskStartDate)

        addGroupbtn.setOnClickListener{
            val taskNme = taskName.text.toString()
           val taskGrupName= taskGroupName.text.toString()
            val  taskDescrption= taskDescription.text.toString()
            val  taskStartate= taskStartDate.text.toString()
            val taskEndDte= taskEndDate.text.toString()
            var taskPicture:String?=null
            if (pic != null) {
                taskPicture = pic?.let { ImageEncoder.encodeImage(it) }
            }
            if (taskNme.isNotEmpty()) {
                auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val dataReference = FirebaseDatabase.getInstance().reference
                        .child("Task")
                        .child(userId)

                    // Check if the category already exists
                    val TaskQuery = dataReference.orderByChild("Task").equalTo(taskNme)
                    TaskQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Category already exists
                                Toast.makeText(applicationContext, "Task already exists", Toast.LENGTH_SHORT).show()
                            } else {
                                // Category does not exist, add it to the database
                                val newTask=dataReference.push()

                                newTask.child("taskName").setValue(taskNme)
                                newTask.child("taskGroupName").setValue(taskGrupName)
                                newTask.child("taskDescription").setValue(taskDescrption)
                                newTask.child("taskStartDate").setValue(taskStartate)
                                newTask.child("taskEndDate").setValue(taskEndDte)
                                newTask.child("taskPicture").setValue(taskPicture).addOnCompleteListener { groupTask ->
                                    if (groupTask.isSuccessful) {
                                        Toast.makeText(applicationContext, "Task added successfully", Toast.LENGTH_SHORT).show()
                                        taskName.text = null
                                        taskGroupName.text = null
                                        taskDescription.text = null
                                        taskStartDate.text = null
                                        taskEndDate.text = null
                                        taskPicture = null
                                        taskPictureIV.visibility = View.GONE
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

        //close group btn
        closeGroupPopUpBtn= findViewById(R.id.closeGroupPopUpBtn)
        closeGroupPopUpBtn.setOnClickListener{
            val intent = Intent(this@TimesheetActivity, ViewTimesheetActivity::class.java)
            startActivity(intent)
        }
        //loading picture to image view
        taskPictureIV= findViewById(R.id.taskPictureIV)
        taskPictureIV.visibility = View.GONE
        taskPictureIV.isEnabled = false
        //taking picture button
        taskPicIV= findViewById(R.id.taskPicIV)
        taskPicIV.isEnabled = false
        //REQUEST FOR CAMERA PERMISSIONS AND ENABLED BUTTON IF ALLOWED
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            taskPicIV.isEnabled = true
        }
        //OPEN CAMER TAKE PICTURE
        taskPicIV.setOnClickListener {
            var icamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(icamera, 101)
        }


    }
    //ONCE PICTURE TAKEN SAVE PIC AND UPLOAD TO IMAGE VIEW
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            pic = data?.getParcelableExtra<Bitmap>("data")
            taskPictureIV.isEnabled = true
            taskPictureIV.visibility = View.VISIBLE
            taskPictureIV.setImageBitmap(pic)
        }
    }
    //FOR CAMERA PERMISSIONS
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            taskPicIV.isEnabled = true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}