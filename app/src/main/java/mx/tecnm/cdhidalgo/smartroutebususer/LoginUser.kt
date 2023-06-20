package mx.tecnm.cdhidalgo.smartroutebususer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginUser : AppCompatActivity() {

    //Variables
    private lateinit var etUser: EditText
    private lateinit var etPass: EditText
    private lateinit var bIngresar: Button
    private lateinit var bRegistro: Button
    private lateinit var bForgot: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        //Llamar Vistas
        etUser = findViewById(R.id.etUser)
        etPass = findViewById(R.id.etPass)
        bIngresar = findViewById(R.id.bIngresar)
        bRegistro = findViewById(R.id.bRegistro)
        bForgot = findViewById(R.id.bForgot)

        //auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        bIngresar.setOnClickListener { loginUser() }
        bRegistro.setOnClickListener { btn_regresar() }
        bForgot.setOnClickListener { btn_forgotPass() }
    }

    fun btn_forgotPass() {
        startActivity(Intent(this, ForgotPassUser::class.java))
    }

    fun btn_regresar() {
        startActivity(Intent(this, RegistroUser::class.java))
    }

    private fun loginUser() {
        val user: String = etUser.text.toString()
        val pass: String = etPass.text.toString()

        //que no esten vacios los campos
        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pass)){

            auth.signInWithEmailAndPassword(user,pass)
                .addOnCompleteListener(this){
                    //se realizo completamenteg
                        task ->
                    if (task.isSuccessful){
                        accion()
                    }else{
                        Toast.makeText(this,"Error en la autentificación",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    private fun accion(){
        startActivity(Intent(this, AlertaUser::class.java))
    }
}
