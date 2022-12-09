package com.example.ifpizzaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class RecuperarClave : AppCompatActivity() {
    lateinit var edtDigiteCorreoRecuperarClave: EditText
    lateinit var btnVerificar: Button

    var email: String = ""

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_clave)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtDigiteCorreoRecuperarClave = findViewById(R.id.edtDigiteCorreoRecuperarClave)
        btnVerificar = findViewById(R.id.btnVerificar)
        mAuth = FirebaseAuth.getInstance()
    }

    fun verficarRecuperacionClave(v: View){
        email = edtDigiteCorreoRecuperarClave.text.toString()

        if(email.isNotEmpty()){
            resetearClave()
        }
        else{
            edtDigiteCorreoRecuperarClave.error = "Ingrese su correo"
        }
    }

    fun resetearClave(){
        mAuth.setLanguageCode("es")
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                MaterialAlertDialogBuilder(this)
                    .setTitle("Te hemos enviado un correo de reestablecimiento")
                    .setMessage("Revisa tu correo y sigue las instrucciones para reestablecer tu contraseña.")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
            else{
                MaterialAlertDialogBuilder(this)
                    .setTitle("Error!")
                    .setMessage("Revisa que tu correo esté correctamente escrito.")
                    .setPositiveButton("Aceptar", null)
                    .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}