package com.example.ifpizzaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.annotations.NotNull

class Registrarse : AppCompatActivity() {

    lateinit var edtNombreRegistro: EditText
    lateinit var edtApellidoRegistro: EditText
    lateinit var edtCorreoRegistro: EditText
    lateinit var edtTelefonoRegistro: EditText
    lateinit var edtUsuarioRegistro: EditText
    lateinit var edtClaveRegistro: EditText
    lateinit var edtConfirmarClaveRegistro: EditText
    lateinit var btnNuevoRegistro: Button

    var nombre: String = ""
    var apellido: String = ""
    var correo: String = ""
    var telefono: String = ""
    var usuario: String = ""
    var clave: String = ""
    var claveConfirmacion: String = ""

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        edtNombreRegistro = findViewById(R.id.edtNombreRegistro)
        edtApellidoRegistro = findViewById(R.id.edtApellidoRegistro)
        edtCorreoRegistro = findViewById(R.id.edtCorreoRegistro)
        edtTelefonoRegistro = findViewById(R.id.edtTelefonoRegistro)
        edtUsuarioRegistro = findViewById(R.id.edtUsuarioRegistro)
        edtClaveRegistro = findViewById(R.id.edtClaveRegistro)
        edtConfirmarClaveRegistro = findViewById(R.id.edtConfirmarClaveRegistro)
        btnNuevoRegistro = findViewById(R.id.btnNuevoRegistro)
    }

    fun registrarUsuario(view: View) {
        nombre = edtNombreRegistro.text.toString()
        apellido = edtApellidoRegistro.text.toString()
        correo = edtCorreoRegistro.text.toString()
        telefono = edtTelefonoRegistro.text.toString()
        usuario = edtUsuarioRegistro.text.toString()
        clave = edtClaveRegistro.text.toString()
        claveConfirmacion = edtConfirmarClaveRegistro.text.toString()

        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || telefono.isEmpty() || usuario.isEmpty() || clave.isEmpty() || claveConfirmacion.isEmpty() || clave.length <= 6 || clave != claveConfirmacion){
            if(nombre.isEmpty()) {
                edtNombreRegistro.error = "Nombre requerido."
            }
            if(apellido.isEmpty()) {
                edtApellidoRegistro.error = "Apellido requerido."
            }
            if(correo.isEmpty()) {
                edtCorreoRegistro.error = "Correo requerido."
            }
            if(telefono.isEmpty()) {
                edtTelefonoRegistro.error = "Teléfono requerido."
            }
            if(usuario.isEmpty()) {
                edtUsuarioRegistro.error = "Usuario requerido."
            }
            if(clave.isEmpty()) {
                edtClaveRegistro.error = "Contraseña requerida."
            }
            if(claveConfirmacion.isEmpty()) {
                edtConfirmarClaveRegistro.error = "Confirma tu contraseña."
            }
            if(clave.length < 6) {
                edtClaveRegistro.error = "La contraseña debe tener al menos 7 caracteres."
            }
            if(clave != claveConfirmacion) {
                edtConfirmarClaveRegistro.error = "Las contraseñas no concuerdan."
            }
        }
        else {
            registrar(nombre, apellido, telefono, usuario, correo, clave)
        }
    }

    fun registrar(name: String, lastname: String, phone: String, user: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val map = hashMapOf<String, String>()
                map["nombre"] = name
                map["apellido"] = lastname
                map["telefono"] = phone
                map["usuario"] = user
                map["correo"] = email
                map["password"] = password

                var id: String =  mAuth.currentUser!!.uid
                mDatabase.child("usuarios").child(id).setValue(map).addOnCompleteListener(this){ task2 ->
                    if(task2.isSuccessful){
                        val intent = Intent(this, Login::class.java)
                        this.startActivity(intent)
                    }
                    else {
                        Toast.makeText(this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
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