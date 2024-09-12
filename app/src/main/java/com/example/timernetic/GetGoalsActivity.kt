package com.example.timernetic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.timernetic.utils.goalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GetGoalsActivity : AppCompatActivity() {

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var dataReference: DatabaseReference


    private lateinit var minGoalText : TextView
    private lateinit var maxGoalText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_goals)

        minGoalText = findViewById(R.id.MinGoaleEdt)
        maxGoalText = findViewById(R.id.MaxGoaleEdt)

        auth = FirebaseAuth.getInstance()

        // data reference
        dataReference = FirebaseDatabase.getInstance().reference
            .child("Goals").child(auth.currentUser?.uid.toString())

        dataReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (taskSnapshot in snapshot.children){
                    val task = taskSnapshot.getValue(goalData::class.java)
                    if(task != null){
                        minGoalText.text = task.minGoal.toString()
                        maxGoalText.text = task.maxGoal.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}