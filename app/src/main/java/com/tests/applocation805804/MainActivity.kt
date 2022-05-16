package com.tests.applocation805804
//Liat Shear 805804
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*
import android.widget.*


class MainActivity : AppCompatActivity() {

    //Declaring the location variable

    private lateinit var Locationtxt: TextView






    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // declaring all the buttons
        val getPos = findViewById<Button>(R.id.getPos)
        val shareContact = findViewById<Button>(R.id.buttonShare1)
        val shareApp = findViewById<Button>(R.id.buttonShare2)

        // click on the get current location button to display location
        getPos.setOnClickListener {
            getCurrentLocation(null)
        }

        shareContact.setOnClickListener {
            // take you to the page to share to whatsapp contact
            val intent = Intent(this, ShareLocation::class.java)
            startActivity(intent)

        }

        shareApp.setOnClickListener{

            //share with other apps, needs to be called before the whatsapp button

            //callActivity()
            //val msg: String = Locationtxt.text.toString()
            val message: String = Locationtxt.text.toString()
            val intent = Intent()
            //val textM = findViewById<TextView>(R.id.Locationtxt)
            //val message = textM.text.toString()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share to: "))

        }


    }

   private fun callActivity(){
        val textM = findViewById<TextView>(R.id.Locationtxt)
        val message = textM.text.toString()
        val intent = Intent(this, ShareLocation::class.java).also{
            it.putExtra("EXTRA_MESSAGE", message)
            startActivity(it)
        }
    }


    private fun getCurrentLocation(view: View?){
        //check the permissions and that it is enabled
        if(checkPermission()){
            if(isLocationEnabled()){
                //final location
                var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) {task ->
                    var location:Location?=task.result
                    if(location == null){
                        Toast.makeText(this, "Null received", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this, "Successly found you!", Toast.LENGTH_SHORT).show()
                       /* var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        val task = fusedLocationProviderClient.lastLocation*/
                        val addresses: List<Address>
                        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
                        addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        var textview = findViewById<TextView>(R.id.Locationtext)
                        textview.text = addresses[0].getAddressLine(0)
                        Locationtxt = textview
                    }
                }
            }else{
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent=Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            // request permission
            requestPermissions()
        }

    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

   private fun checkPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission and false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
       return false

    }

    private fun requestPermissions(){
        //this function will allows us to tell the user to request the necessary permission if they are not granted
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //*Log.d("Debug:","You have the Permission")*//*
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation(null)
            }
            //If the user denied permissions, provide an explanation why the permission is needed, and
            //also provide a way for the user to open the proper settings screen to manually grant your
                    //app the relevant permission.

            else{
                val denied = getString(R.string.denied)
                val getPos = findViewById<Button>(R.id.getPos)
                val builder = AlertDialog.Builder(this)
                getPos.setOnClickListener{
                    builder.apply{
                        setTitle("Permission Denied")
                        setMessage("$denied")
                        setCancelable(true)

                        builder.setPositiveButton("Will do!") { po, p1 ->
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }

            }
        }


    }


}
