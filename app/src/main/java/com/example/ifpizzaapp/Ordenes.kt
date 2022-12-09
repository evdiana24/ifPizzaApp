package com.example.ifpizzaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpizzaapp.adapters.PedidosAdapter
import com.example.ifpizzaapp.adapters.ProductoAdapter
import com.example.ifpizzaapp.models.Pedidos
import com.example.ifpizzaapp.models.Productos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Ordenes : Fragment() {

    private lateinit var ordenesRecyclerView: RecyclerView
    private lateinit var adapter: PedidosAdapter
    private  lateinit var ordenesList: ArrayList<Pedidos>

    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var idUsr: String

    companion object {
        fun newInstance(): Ordenes = Ordenes()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_ordenes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ordenesRecyclerView = view.findViewById(R.id.rcvOrdenes)
        ordenesRecyclerView.layoutManager = LinearLayoutManager(context)

        auth = FirebaseAuth.getInstance()
        idUsr = auth.currentUser?.uid.toString()

        ordenesList = arrayListOf<Pedidos>()
        cargarOrdenes()
    }

    fun cargarOrdenes(){
        dbref = FirebaseDatabase.getInstance().getReference("pedidos")
        val id = auth.currentUser?.uid.toString()
        dbref.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val auxList = arrayListOf<Pedidos>()
                    for (ordenSnapshot in snapshot.children) {
                        //val orden1 = ordenSnapshot.value.toString().contains("pendiente")
                        //val orden2 = ordenSnapshot.value.toString().contains("en-camino")

                        //if(orden1 || orden2){
                            val pedido = ordenSnapshot.getValue(Pedidos::class.java)
                            auxList.add(pedido!!)
                        //}
                    }
                    ordenesList = auxList
                    ordenesRecyclerView.adapter = PedidosAdapter(ordenesList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}