package com.example.ifpizzaapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpizzaapp.adapters.CarritoAdapter
import com.example.ifpizzaapp.adapters.ProductoAdapter
import com.example.ifpizzaapp.models.Productos

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class Carrito : Fragment() {
    private lateinit var list: ArrayList<com.example.ifpizzaapp.models.Carrito>
    private lateinit var rv: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var idUsr: String
    private lateinit var dbref: DatabaseReference
    private var total: Double = 0.0;
    private lateinit var txvTotalPorProductos: TextView;
    private lateinit var txvTotalCarrito: TextView;
    private lateinit var  btnGuardar: Button;
    private lateinit var txvDireccionCarrito: EditText

    private lateinit var productosArrayList: ArrayList<Productos>

    companion object {
        fun newInstance(): Carrito = Carrito()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.activity_carrito, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.rv = view.findViewById(R.id.rcvCarrito)
        this.txvTotalPorProductos = view.findViewById(R.id.txvTotalPorProductos)
        this.txvTotalCarrito = view.findViewById(R.id.txvTotalCarrito)
        this.btnGuardar = view.findViewById(R.id.btnGenerarOrden)
        //this.txvDireccionCarrito = view.findViewById(R.id.txvDireccionCarrito)

        //asignar funcionalidad al boton
        this.btnGuardar.setOnClickListener(View.OnClickListener { view ->
            guardarPedido()
        })


        list  = arrayListOf<com.example.ifpizzaapp.models.Carrito>()
        productosArrayList = arrayListOf<Productos>()
        this.rv.layoutManager = LinearLayoutManager(rv.context)
        this.auth = FirebaseAuth.getInstance()
        this.idUsr = auth.currentUser?.uid.toString()
        cargarCarrito();
    }

    private fun cargarCarrito() {
        dbref = FirebaseDatabase.getInstance().getReference("carrito")
        val id = auth.currentUser?.uid.toString()
        dbref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {
                    try {

                        for (userSnapshot in snapshot.children) {
                            var nombre = userSnapshot.child("nombre").getValue().toString()
                            var precio:Float = userSnapshot.child("precio").getValue().toString().toFloat()
                            var cantidad:Int= userSnapshot.child("cantidad").getValue().toString().toInt()
                            var id = userSnapshot.child("productoId").getValue().toString()
                            var url = userSnapshot.child("imagenURL").getValue().toString()


                            var carrito = com.example.ifpizzaapp.models.Carrito(
                                producto=nombre,
                                precio=precio,
                                cantidad= cantidad,
                                totalCarrito = precio*cantidad,
                                url = url,
                                productoId = id
                            )
                            total += precio*cantidad
                            list.add(carrito)
                        }

                        rv.adapter = CarritoAdapter(list);
                        total.toString()
                       //obtener el total en dos decimales

                        var totalDosDecimales = String.format("%.2f", total)
                        //total carrito +0.99
                        var totalCarrito = total + 0.99
                        var totalCarritoDosDecimales = String.format("%.2f", totalCarrito)
                        txvTotalPorProductos.text = "Total por productos: $ $totalDosDecimales"
                        txvTotalCarrito.text = "Total: $ $totalCarritoDosDecimales"


                    }catch (e: Exception){
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

     fun guardarPedido() {

         if (list.size == 0){
             Toast.makeText(context, "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
             return;
         }
        //uid
         //validar si se quiere guardar el pedido alert dialog
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmar")
            builder.setMessage("¿Desea guardar el pedido?")
          builder.setPositiveButton("Si"){dialog, which ->
              var uid = auth.currentUser?.uid.toString()
              dbref = FirebaseDatabase.getInstance().getReference("pedidos/"+uid)
              //json pedido

              var instrucciones = view?.findViewById<TextView>(R.id.edtInstruccionesOrden)?.text.toString()
              val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
              val fecha = sdf.format(Date())
              val direccion = view?.findViewById<TextView>(R.id.txvDireccionCarrito)?.text.toString()
              var pedido = hashMapOf<String, Any>(

                  "total" to total+0.99,
                  "estatus" to "pendiente",
                  "id_usuario" to uid,
                  "productos" to list,
                  "direccion" to direccion,
                  "instrucciones" to instrucciones,
                  "fecha"  to fecha
              )
              dbref.push().setValue(pedido);
              //eliminar carrito
              dbref = FirebaseDatabase.getInstance().getReference("carrito")
              dbref.child(uid).removeValue()
              //reiniciar todo
                list.clear()
                rv.adapter = CarritoAdapter(list);
                total = 0.0
                  txvTotalPorProductos.text = "Total por productos: $total"
                    txvTotalCarrito.text = "Total: $total"
                Toast.makeText(context, "Pedido guardado", Toast.LENGTH_SHORT).show()
          }
            builder.setNegativeButton("No"){dialog,which ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
    }
}