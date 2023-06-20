package mx.tecnm.cdhidalgo.smartroutebususer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class AlertaUser : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var correo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerta_user)

        auth= Firebase.auth
        database= Firebase.firestore

        correo = Firebase.auth.currentUser!!.email!!

        //variables
        val buttonAlert = findViewById<Button>(R.id.button_alert)
        val buttonPerfil = findViewById<Button>(R.id.button_perfil)
        val textViewResult = findViewById<TextView>(R.id.text_view_result)
        val Button_logout = findViewById<Button>(R.id.Button_logout)



        //envia el mensaje alerta
        buttonAlert.setOnClickListener  {
            var enviar = true;
            getLocation(textViewResult, enviar)
            //textViewResult.text = "¡Alerta enviada!"
            //Toast.makeText(this, "Alerta enviada", Toast.LENGTH_SHORT).show()
        }

        buttonPerfil.setOnClickListener {
            //val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
            val intent = Intent(this, PerfilUser::class.java)
            startActivity(intent)
        }
        Button_logout.setOnClickListener {
            // Cerrar sesion y regrsa a la pantalla de login
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
            finish()
        }
    }

    ////Este código solicita permiso al usuario para acceder a la ubicación del dispositivo
    //y luego obtiene la ubicación del usuario y la envía al servidor.

    fun getLocation(textView: TextView, enviar: Boolean) {
        var enviar = enviar
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                // Aquí puedes enviar la ubicación en tiempo real al servidor
                //Log.d("DEBUG", "lat:"+latitude + ", lon:" + longitude)

                if(enviar) {
                    enviar = false
                    val alerta = hashMapOf(
                        "fecha" to Date(),
                        "latitud" to latitude,
                        "logintud" to longitude
                    )
                    database.collection("alerta_users").document(correo)
                        .set(alerta, SetOptions.merge())
                        .addOnSuccessListener { documentReference ->

                            textView.text = "lat:" + latitude + ", lon:" + longitude
                            //Toast.makeText(AlertaUser@this,"BIEN",Toast.LENGTH_SHORT).show()

                        }
                        //Log cambiar por Toast
                        .addOnFailureListener { e ->

                            //Toast.makeText(this,"ERROR NO SE PUDO REGISTRAR",Toast.LENGTH_SHORT).show()

                        }
                }

            }

            override fun onProviderDisabled(provider: String) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
        // Pide permiso para acceder a la ubicación del usuario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

    }

}





