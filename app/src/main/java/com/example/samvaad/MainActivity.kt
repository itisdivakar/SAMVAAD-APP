package com.example.samvaad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var  mDbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()//mAuth ko initialize kiya
        mDbRef = FirebaseDatabase.getInstance().getReference()//initializing mDbRef


        userList = ArrayList()//hame data base se userlist is array mein daalni hai
        adapter = UserAdapter(this, userList)//but hamne userlist ko firebase se database me daala hi nhi hai
        //so add first...we can only access them from database


        userRecyclerView = findViewById(R.id.userRecyclerView)//recylerview lo initialize kiya

        userRecyclerView.layoutManager = LinearLayoutManager(this)//if we r using reclerview then we have to set the kayout manager as well
        userRecyclerView.adapter = adapter

//getting inside the user node and looping through the users available
        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //adding clicklistener to the menu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
                                     //↑ ye item check krega ki konsa item click hua hai
        if (item.itemId == R.id.logout){
            //write logic for logout
                mAuth.signOut()
            val intent = Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)
//isse logout hone ke baad fir se login page pe aa jaayenge naki app close ho jaayega
            return true
        }
     return true
    }

}