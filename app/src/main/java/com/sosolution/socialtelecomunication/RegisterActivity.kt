package com.sosolution.socialtelecomunication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.HashMap


class RegisterActivity : AppCompatActivity() {

    //Declaracion de Componentes
    lateinit var rCicleImageBack: CircleImageView
    lateinit var rTextInputUsername: TextInputEditText
    lateinit var rTextInputEmail: TextInputEditText
    lateinit var rTextInputPassword: TextInputEditText
    lateinit var rTextInputConfirmPassword: TextInputEditText
    lateinit var rBtnRegister: Button

    lateinit var rAuth: FirebaseAuth
    lateinit var  rFirebaseFirestore : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inicializarVistas()
        clickAtras()
        clickRegistrar()

    }



    //btnRegister Hacer accion de registar
    private fun clickRegistrar() {
        rBtnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {

        val userName = rTextInputUsername.text.toString()
        val email = rTextInputEmail.text.toString()
        val password = rTextInputPassword.text.toString()
        val confirmPassword = rTextInputConfirmPassword.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {

            if (isPasswordValid(password, confirmPassword) && isEmailValid(email)) {
                createUser(userName,email, password)
            } else {
                Toast.makeText(this, "Error al procesar los campos", Toast.LENGTH_LONG).show()

            }

        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()

        }
    }

    //una vez validado crea usuario
    private fun createUser(userName : String ,email: String, password: String) {

        rAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                val id : String = rAuth.currentUser?.uid ?:  ""
                val map : MutableMap<String,String> = mutableMapOf()
                map["email"] = email
                map["username"] =userName
                createFirebaseBBDD(map,id)

            }else{
                Toast.makeText(this, "No se pudo registar el usuario", Toast.LENGTH_LONG).show()

            }
        }

    }

    private fun createFirebaseBBDD(map: MutableMap<String, String>, id:String) {

        rFirebaseFirestore.collection("Users").document(id).set(map).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"El usuario se almacenó en la base de datos",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"El usuario  no se almacenó en la base de datos",Toast.LENGTH_LONG).show()

            }
        }
    }

    //ValidarPassword
    private fun isPasswordValid(password: String, confirmPassword: String): Boolean {
        if (password.equals(confirmPassword)) {
            if (password.length >= 6) {
                return true
            }
        }
        return false
    }

    //valida email si posee los caracteres necesarios
    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    //Vuelve a Pantalla anterior
    private fun clickAtras() {
        rCicleImageBack.setOnClickListener {
            finish();
        }
    }

    //Inicializo los componentes que usare
    private fun inicializarVistas() {
        rCicleImageBack = findViewById(R.id.cicleImageBack);
        rTextInputUsername = findViewById(R.id.textInputUsername)
        rTextInputEmail = findViewById(R.id.textInputEmail)
        rTextInputPassword = findViewById(R.id.textInputPassword)
        rTextInputConfirmPassword = findViewById(R.id.textInputConfirmPassword)
        rBtnRegister = findViewById(R.id.btnRegister)
        rAuth = FirebaseAuth.getInstance()
        rFirebaseFirestore =Firebase.firestore

    }
}

private fun <K, V> MutableMap<K, V>.put(key: K, value: K) {

}
