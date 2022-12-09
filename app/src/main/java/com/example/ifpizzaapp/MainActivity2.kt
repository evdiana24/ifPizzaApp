package com.example.ifpizzaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ifpizzaapp.models.Productos
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.annotations.NotNull

class MainActivity2 : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val abrirFragmento:String = intent.getStringExtra("abrirFragmento").toString()
        //val listaProductos: String = intent.getStringExtra("productosCarrito").toString()

        //val listaProductos = intent.extras?.get("productosCarrito") as MutableList<Productos>
        //val listaProductos : ArrayList<Productos> = intent.getParcelableExtra("workoutlist")

        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    val fragment = Home.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_favoritos -> {
                    val fragment = Favoritos.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_pedidos -> {
                    val fragment = Ordenes.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_carrito -> {
                    val fragment = Carrito.newInstance()
                    openFragment(fragment)
                    true
                }
                R.id.navigation_usuario -> {
                    val fragment = MenuUsuario.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }

        if(abrirFragmento == "carrito"){
            bottomNav.selectedItemId = R.id.navigation_carrito
            //Toast.makeText(this, "array $listaProductos", Toast.LENGTH_LONG).show()
        }else{
            bottomNav.selectedItemId = R.id.navigation_home
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}