package com.example.ifpizzaapp

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpizzaapp.adapters.ProductoAdapter
import com.example.ifpizzaapp.models.Productos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListaProductos : AppCompatActivity(), ProductoAdapter.ProductosListClickListener{

    ///AGREGADO PARA PRUEBAS DE CARRITO
    private var itemsInTheCartList: MutableList<Productos?>? = null
    private var totalItemInCartCount = 0

    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var idUsr: String
    private lateinit var productoRecyclerView: RecyclerView
    private lateinit var productosArrayList: ArrayList<Productos>
    private lateinit var btnVerOrden: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
     try {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_lista_productos)


         supportActionBar?.setDisplayHomeAsUpEnabled(true)

         btnVerOrden = findViewById(R.id.btnVerOrden)
         btnVerOrden.setOnClickListener(View.OnClickListener { view ->
             val intent = Intent(this, MainActivity2::class.java)
             intent.putExtra("abrirFragmento", "carrito")
             //intent.putExtra("productosCarrito", itemsInTheCartList.toString())
             //intent.extras?.putSerializable("productosCarrito", itemsInTheCartList.toString())
             // intent.putExtra("productosCarrito", ArrayList(itemsInTheCartList))

             startActivity(intent)
         })
         auth= FirebaseAuth.getInstance()
         idUsr = auth.currentUser?.uid.toString()
         productoRecyclerView = findViewById(R.id.productosRecyclerView)
         productoRecyclerView.layoutManager = LinearLayoutManager(this)

         productosArrayList = arrayListOf<Productos>()
         getInfoProductos()
     }catch (e:Exception){
         Toast.makeText(this, "Error3: ${e.message}", Toast.LENGTH_SHORT).show()
     }
    }

    /// TRAE LOS ITEMS DE FIREBASE AL RECYCLERVIEW
    private fun getInfoProductos() {
    try {

    //ADDED FOR CATEGORY FILTER
    val categoria:String = intent.getStringExtra("categoria").toString()

    dbref = FirebaseDatabase.getInstance().getReference("productos")
    dbref.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(productoSnapshot in snapshot.children){

                    val producto1 = productoSnapshot.value.toString().contains(categoria)

                    if(producto1){
                        val producto = productoSnapshot.getValue(Productos::class.java)
                        var productoId = productoSnapshot.key.toString()
                        producto?.productoId = productoId
                        productosArrayList.add(producto!!)

                        //Toast.makeText(productoRecyclerView.context, "filtro $producto", Toast.LENGTH_LONG).show()
                    }
                    //continue
                }

                productoRecyclerView.adapter = ProductoAdapter(productosArrayList, this@ListaProductos)
                //recorrerProductos(productosArrayList)
                val uid = auth.currentUser?.uid.toString()
                for (producto in productosArrayList){
                    try {
                        dbref = FirebaseDatabase.getInstance().getReference("carrito/"+uid+"/"+producto?.productoId)
                        dbref.get().addOnSuccessListener {
                            if(it.exists()){
                                var cantidad = it.child("cantidad").value.toString().toInt()
                                producto?.cantidad = cantidad
                                if(itemsInTheCartList == null) {
                                    itemsInTheCartList = ArrayList()
                                }
                                itemsInTheCartList?.add(producto)



                            }
                        }
                        dbref= FirebaseDatabase.getInstance().getReference("carrito/"+uid)
                        dbref.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                   var total= 0;
                                    for (producto in snapshot.children){
                                        val cantidad = producto.child("cantidad").value.toString().toInt()
                                        total += cantidad
                                    }
                                    totalItemInCartCount = total
                                    btnVerOrden.text = "Ver orden ($total) items"
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }


                        })
                    }catch (e: Exception){
                        Toast.makeText(this@ListaProductos, "Error3311: ${e.message}", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}catch (e: Exception){

}
    }

    override fun addToCartClickListener(producto: Productos) {
    try {
        if(itemsInTheCartList == null) {
            itemsInTheCartList = ArrayList()
        }
        itemsInTheCartList?.add(producto)
        totalItemInCartCount = 0
        for(producto in itemsInTheCartList!!) {
            totalItemInCartCount = totalItemInCartCount + producto?.cantidad!!
        }
        btnVerOrden.text = "Ver orden (" + totalItemInCartCount +") items"
        //agregarProducto a firebase
        dbref = FirebaseDatabase.getInstance().getReference("carrito")
        var id = auth.currentUser?.uid.toString()
        var productoId = producto?.productoId.toString()

        dbref.child(id).child(productoId).setValue(producto)
    }catch (e: Exception){
        Toast.makeText(this, "Error: $e", Toast.LENGTH_LONG).show()
    }

    }

    override fun updateCartClickListener(producto: Productos) {
        try {
            val index = itemsInTheCartList!!.indexOf(producto)

            itemsInTheCartList?.removeAt(index)
            itemsInTheCartList?.add(producto)
            totalItemInCartCount = 0
            for (producto in itemsInTheCartList!!) {
                totalItemInCartCount = totalItemInCartCount + producto?.cantidad!!
            }
            btnVerOrden.text = "Ver orden (" + totalItemInCartCount + ") items"

        } catch (e: Exception) {
            Toast.makeText(this, "Error1: ${e.message}", Toast.LENGTH_LONG).show()



        }
    }

    override fun removeFromCartClickListener(producto: Productos) {
       try {
           if(itemsInTheCartList!!.contains(producto)) {
               itemsInTheCartList?.remove(producto)
               totalItemInCartCount = 0
               for(producto in itemsInTheCartList!!) {
                   totalItemInCartCount = totalItemInCartCount + producto?.cantidad!!
               }
               btnVerOrden.text = "Ver orden (" + totalItemInCartCount +") items"
           }
       }catch (e: Exception){
           Toast.makeText(this, "Error2: $e", Toast.LENGTH_LONG).show()
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