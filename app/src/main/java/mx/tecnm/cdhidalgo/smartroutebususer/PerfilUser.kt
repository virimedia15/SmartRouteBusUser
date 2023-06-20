package mx.tecnm.cdhidalgo.smartroutebususer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PerfilUser : AppCompatActivity() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var nombre: EditText
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var tel: EditText
    private lateinit var bguardar: Button
    private lateinit var perfil: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_user)

        //Llamar Vistas
        nombre=findViewById(R.id.Perfil_nombre)
        email=findViewById(R.id.Perfil_email)
        pass=findViewById(R.id.Perfil_pass)
        tel=findViewById(R.id.Perfil_tel)
        bguardar=findViewById(R.id.Button_Perfil_Guardar)

        auth= Firebase.auth
        database= Firebase.firestore
        perfil = Firebase.auth.currentUser!!
        val correo = perfil.email
        email.setText (correo)
        obtenerDatos(correo!!)
        bguardar.setOnClickListener { guardarDatos() }
    }

    fun obtenerDatos(email:String){
        database.collection("users")
            .whereEqualTo("email",email)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    nombre.setText( document.data.get("nombre").toString())
                    tel.setText( document.data.get("telefono").toString())

                }
            }
    }

    fun guardarDatos() {

        val name:String=nombre.text.toString()
        val email:String=email.text.toString()
        val pass:String=pass.text.toString()
        val tel:String=tel.text.toString()

        perfil.updatePassword(pass).addOnCompleteListener{

        }

        val user = hashMapOf(
            "nombre" to name,
            "email" to email,
            "telefono" to tel
        )
        database.collection("users").document(email)
            .set(user)
            .addOnSuccessListener { documentReference ->

                Toast.makeText(this,"BIEN", Toast.LENGTH_SHORT).show()

            }
            //Log cambiar por Toast
            .addOnFailureListener { e ->

                Toast.makeText(this,"ERROR NO SE PUDO REGISTRAR", Toast.LENGTH_SHORT).show()

            }

    }
}