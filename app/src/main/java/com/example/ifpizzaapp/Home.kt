package com.example.ifpizzaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpizzaapp.adapters.CategoriaAdapter
import com.example.ifpizzaapp.models.Categoria
import com.example.ifpizzaapp.models.CategoriaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.NotNull
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment() {

    private lateinit var viewModel : CategoriaViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    lateinit var adapter: CategoriaAdapter

    lateinit var txvNombreUsuario: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    //ADDED FOR CATEGORY FILTER
    lateinit var txvCategoria: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        fun newInstance(): Home = Home()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            mAuth = FirebaseAuth.getInstance()
            mDatabase = FirebaseDatabase.getInstance().reference
            txvNombreUsuario = view.findViewById(R.id.txvNombreUsuario)

            obtenerInfoUsuario()

            categoryRecyclerView = view.findViewById(R.id.recyclerView)
            categoryRecyclerView.layoutManager = LinearLayoutManager(context)

            adapter = CategoriaAdapter()
            categoryRecyclerView.adapter = adapter

            //added
            adapter.setOnItemClickListener(object: CategoriaAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    //ADDED FOR CATEGORY FILTER
                    var categoryItem: TextView? = categoryRecyclerView.findViewHolderForAdapterPosition(position)?.itemView?.findViewById(R.id.txvNombreCategoria)
                    var categoryName: String = categoryItem?.text.toString()



                    val intent = Intent(activity, ListaProductos::class.java)
                    intent.putExtra("categoria", categoryName)
                    startActivity(intent)
                }
            })

            viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)

            viewModel.allCategories.observe(viewLifecycleOwner, Observer {

                adapter.updateCategoryList(it)

            })
        }catch (e: Exception){

        }
    }

    private fun obtenerInfoUsuario(){
        try{
            var id: String =  mAuth.currentUser!!.uid
            mDatabase.child("usuarios").child(id).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(@NotNull dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        var nombre: String = dataSnapshot.child("nombre").value.toString()

                        txvNombreUsuario.text = "¡Bienvenido/a " +nombre +"!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Database", error.toString())
                } })
        }catch (e: Exception){
            Log.e("Error", e.toString())
        }

    }
}