package com.tests.applocation805804

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest

class ShareLocation : AppCompatActivity() {


    private var layoutManager: RecyclerView.LayoutManager?= null
    private var adapter:RecyclerAdapter?= null
    private var Locationtxt: String? = null
    private var names = mutableListOf<String>()
    private var nums = mutableListOf<String>()

    //initialize for phone name, number and id
    var cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)
        val searchView = findViewById<ListView>(R.id.searchView)

        //button to return back to home screen
        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top, androidx.appcompat.R.anim.abc_slide_out_top)
        }

        val share = findViewById<Button>(R.id.GetPos)

        share.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, Array(1) { Manifest.permission.READ_CONTACTS },
                111)
        } else
            readContactRecycle()

    }

    fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults:
    IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            readContactRecycle()
    }
    @SuppressLint("Range")
    private fun readContactRecycle(){
       /* val searchView = findViewById<SearchView>(R.id.searchView)
        val listview1 = findViewById<ListView>(R.id.listview1)*/
        var to = intArrayOf(android.R.id.text1, android.R.id.text2)
        var from = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER).toTypedArray()

        var reS = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            cols, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        reS?.moveToFirst();
        //iterate through the contacts and add them to the recycle view
        while(reS?.moveToNext() == true){
            var Name: String = reS.getString(reS.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var Num : String = reS.getString(reS.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            if(Name != "" && Num != ""){
                    names.add(Name)
                    nums.add(Num)
            }
            else{
                Name = "Missing name"
                Num = "Missing number"
            }
        }
        layoutManager = LinearLayoutManager(this)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(names, nums, Locationtxt, this)
        recyclerView.adapter=adapter
        reS?.close()
    }

}