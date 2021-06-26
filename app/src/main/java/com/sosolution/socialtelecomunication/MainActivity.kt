package com.sosolution.socialtelecomunication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    //Declaracion de Componentes
    lateinit var mTextViewRegister: TextView
    lateinit var mTextInputEmail: TextInputEditText
    lateinit var mTextInputPassword: TextInputEditText
    lateinit var mBtnLogin: Button
    lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()

        clickRegistrarActivity()
        clickIniciarSesion()

    }


    //Inicializo los componentes que usare
    private fun inicializarVistas() {
        mTextViewRegister = findViewById(R.id.textViewRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    //Voy  a la pantalla Register Activity
    private fun clickRegistrarActivity() {
        mTextViewRegister.setOnClickListener {
            val intent =  Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun clickIniciarSesion() {
        mBtnLogin.setOnClickListener {
            login()
        }
    }


    private fun login() {

        val email = mTextInputEmail.text.toString();
        val password = mTextInputPassword.text.toString();
        Log.d("Campo", "email:$email")
        Log.d("Campo", "password:$password")


        if (email.isNotEmpty() && password.isNotEmpty()) {

            if (isEmailValid(email)) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "pase por aca ", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "El email o contraseña no son correctas ", Toast.LENGTH_LONG).show()
                    }

                }
            }
        } else {
            Toast.makeText(this, "Error al procesar los campos", Toast.LENGTH_LONG).show()
        }

    }

    //valida email si posee los caracteres necesarios
    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

}