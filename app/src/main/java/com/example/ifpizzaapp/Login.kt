package com.example.ifpizzaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.annotations.NotNull

class Login : AppCompatActivity(), View.OnClickListener{

    lateinit var edtDigiteEmail : EditText
    lateinit var edtDigiteClave : EditText
    lateinit var btnIniciarSesion : Button
    lateinit var btnRegistrarse : Button
    lateinit var txvOlvidoContra : TextView

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtDigiteEmail = findViewById(R.id.edtDigiteEmail)
        edtDigiteClave = findViewById(R.id.edtDigiteClave)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        btnRegistrarse = findViewById(R.id.btnRegistrarseLogin)
        txvOlvidoContra = findViewById(R.id.txvOlvidoContra)

        btnIniciarSesion.setOnClickListener(this)
        btnRegistrarse.setOnClickListener(this)
        txvOlvidoContra.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnIniciarSesion -> {
                val correo: String = edtDigiteEmail.text.toString()
                val clave: String = edtDigiteClave.text.toString()
                if(correo.isEmpty()) {
                    edtDigiteEmail.error = "Usuario requerido."
                }
                if(clave.isEmpty()) {
                    edtDigiteClave.error = "Contraseña requerida."
                }
                else {
                    login(correo, clave)
                }
            }
            R.id.btnRegistrarseLogin -> {
                startActivity(Intent(this, Registrarse::class.java))
            }
            R.id.txvOlvidoContra -> {
                startActivity(Intent(this, RecuperarClave::class.java))
            }
        }
    }

    fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, MainActivity2::class.java))
                finish()
            }
            else{
                MaterialAlertDialogBuilder(this)
                    .setTitle("Credenciales incorrectas")
                    .setMessage("Su usuario y/o contraseña son incorrectos.")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        if(mAuth.currentUser != null){
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
    }
}