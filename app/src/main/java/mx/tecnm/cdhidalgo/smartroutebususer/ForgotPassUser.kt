package mx.tecnm.cdhidalgo.smartroutebususer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class ForgotPassUser : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var bsend: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_user)

        email=findViewById(R.id.txtEmail)
        bsend=findViewById(R.id.bsend)
        //crear instancia
        auth=FirebaseAuth.getInstance()

        bsend.setOnClickListener { send() }
    }
    fun send(){
        //que el cuadro de texto no este vacio
        val email=email.text.toString()

        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                        task ->

                    if(task.isSuccessful){
                        Toast.makeText(this,"REVISE EN SU CORREO ELECTRONICO", Toast.LENGTH_SHORT).show()
                        //startActivity(Intent(this, LoginUser::class.java))
                    }else{
                        Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}

