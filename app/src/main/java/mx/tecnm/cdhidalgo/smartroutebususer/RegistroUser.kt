package mx.tecnm.cdhidalgo.smartroutebususer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.FirebaseFirestoreKtxRegistrar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegistroUser : AppCompatActivity() {
    
    //Declaracion de variables
    private lateinit var nombre: EditText
    private lateinit var email:EditText
    private lateinit var pass:EditText
    private lateinit var tel:EditText
    private lateinit var bguardar:Button
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_user)

        //Llamar Vistas
        nombre=findViewById(R.id.nombre)
        email=findViewById(R.id.email)
        pass=findViewById(R.id.pass)
        tel=findViewById(R.id.tel)
        bguardar=findViewById(R.id.bguardar)

        //Instanciar BD y autentificacion
        //database= FirebaseDatabase.getInstance()
        //auth=FirebaseAuth.getInstance()
        auth=Firebase.auth
        database=Firebase.firestore

        bguardar.setOnClickListener { createNewAccount2() }
        //Ref para leer o escribir en una ubicacion}
        //dbReference=database.reference.child("User")

    }
    //Metodo onclick
    fun btn_guardar(view: View){
        //llamar metodo para ejecutar monmento de presionar guardar
        createNewAccount2()

    }
    //obtener valores caja de texto
    private fun createNewAccount(){
        val name:String=nombre.text.toString()
        val email:String=email.text.toString()
        val pass:String=pass.text.toString()
        val tel:String=tel.text.toString()

        //validar campos
        if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(pass)
            &&!TextUtils.isEmpty(tel)){

            //dar de alta user y pass
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this){
                    //verificar que el registro se realizo correctamente
                        task ->

                    if(task.isComplete){
                        val user: FirebaseUser?=auth.currentUser
                        //enviar el correo
                        verificacionEmail(user)
                        //otra ubicacion donde se encontrara nombre
                        val userBD=dbReference.child(user?.uid!!)

                        userBD.child("nombre").setValue(nombre)
                        userBD.child("tel").setValue(tel)
                        acccion()

                    }else{
                        Toast.makeText(this,"Error no se pudo registrar",Toast.LENGTH_SHORT).show()

                    }


                }
        }
    }

    private fun createNewAccount2() {
        val name:String=nombre.text.toString()
        val email:String=email.text.toString()
        val pass:String=pass.text.toString()
        val tel:String=tel.text.toString()

        //guardar coleccion nombre de coleccion, dato que guarda documento
        val user = hashMapOf(
            "nombre" to name,
            "email" to email,
            "telefono" to tel
        )

        //validar campos
        if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(email) &&!TextUtils.isEmpty(pass)
            &&!TextUtils.isEmpty(tel)){

            //dar de alta user y pass
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this){

                    //verificar que el registro se realizo correctamente
                    task ->
                    if(task.isSuccessful){
                        database.collection("users").document(email)
                            .set(user)
                            .addOnSuccessListener { documentReference ->

                                Toast.makeText(this,"BIEN",Toast.LENGTH_SHORT).show()
                                acccion()

                            }
                                //Log cambiar por Toast
                            .addOnFailureListener { e ->

                                Toast.makeText(this,"ERROR NO SE PUDO REGISTRAR",Toast.LENGTH_SHORT).show()

                            }
                    }else{
                        Toast.makeText(this,"ERROR CORREGISR LOS DATOS",Toast.LENGTH_LONG).show()
                    }
                }
        }


    }

    //nuevo metodo accion si se realiza correctamente
    private fun acccion(){
        //mandar vista login
        startActivity(Intent(this, LoginUser::class.java))

    }
    //? llamada segura
    //enviar email a usuario que se registro correctamente
    private fun verificacionEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                    task ->
                //se realizo la tarea complegtamente
                if(task.isComplete){
                    Toast.makeText(this,"Email enviado",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Error al enviar el email",Toast.LENGTH_SHORT).show()

                }
            }

    }
}
