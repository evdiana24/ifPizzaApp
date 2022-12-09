package com.example.ifpizzaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import org.jetbrains.annotations.NotNull

class MenuUsuario : Fragment(), View.OnClickListener{

    lateinit var txvUsuarioMenu : TextView
    lateinit var txvCorreoMenu : TextView
    lateinit var txvTelefonoMenu : TextView
    lateinit var btnEditarPerfil : Button
    lateinit var btnDirecciones : Button
    lateinit var btnCamContra : Button
    lateinit var btnCerrarSesion : Button

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    companion object {
        fun newInstance(): MenuUsuario = MenuUsuario()
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.activity_menu_usuario, container, false)*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_menu_usuario, container, false)

        txvUsuarioMenu = view.findViewById(R.id.txvUsuarioMenu)
        txvCorreoMenu = view.findViewById(R.id.txvCorreoMenu)
        txvTelefonoMenu = view.findViewById(R.id.txvTelefonoMenu)
        btnCamContra = view.findViewById(R.id.btnCamContra)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)
        btnDirecciones = view.findViewById(R.id.btnDirecciones)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        btnEditarPerfil.setOnClickListener(this)
        btnCamContra.setOnClickListener(this)
        btnCerrarSesion.setOnClickListener(this)
        btnDirecciones.setOnClickListener(this)

        obtenerInfoUsuarioLogeado()

        return view
    }

    private fun obtenerInfoUsuarioLogeado(){
        var id: String =  mAuth.currentUser!!.uid
        mDatabase.child("usuarios").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(@NotNull dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    var nombre: String = dataSnapshot.child("nombre").value.toString()
                    var correo: String = dataSnapshot.child("correo").value.toString()
                    var telefono: String = dataSnapshot.child("telefono").value.toString()

                    txvUsuarioMenu.text = nombre
                    txvCorreoMenu.text = correo
                    txvTelefonoMenu.text = telefono
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database", error.toString())
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnEditarPerfil -> {
                startActivity(Intent(activity, DatosUsuario::class.java))
            }
            R.id.btnCamContra -> {
                startActivity(Intent(activity, CambiarClave::class.java))
            }
            R.id.btnCerrarSesion -> {
                mAuth.signOut()
                startActivity(Intent(activity, Login::class.java))
                activity?.finish()
            }
            R.id.btnDirecciones -> {
                startActivity(Intent(activity, Maps_Activity::class.java))
            }
        }
    }
}