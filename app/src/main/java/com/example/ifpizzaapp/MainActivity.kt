package com.example.ifpizzaapp

import androidx.fragment.app.Fragment

class MainActivity : Fragment() {
/*
    //lateinit var dref: DatabaseReference
    //lateinit var categoriasRecyclerView: RecyclerView
    //lateinit var categoriasArrayList: ArrayList<Categoria>

    /*private lateinit var viewModel: CategoriaViewModel
    private lateinit var categoriaRecyclerView: RecyclerView
    private lateinit var adapter: categoriaAdapter*/

    lateinit var txvNombreUsuario: TextView

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference

    companion object {
        fun newInstance(): MainActivity = MainActivity()
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.activity_main, container, false)*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_main, container, false)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        //txvNombreUsuario = view.findViewById(R.id.txvNombreUsuario)

        return view

        obtenerInfoUsuario()
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriaRecyclerView = view.findViewById(R.id.categoriasRecyclerView)
        adapter = categoriaAdapter()
        categoriaRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)
        viewModel.allCategories.observe(viewLifecycleOwner, Observer {
            adapter.updateCategoriesList(it)
        })

    }*/

    /*fun getCategoriaData(){
        dref = FirebaseDatabase.getInstance().getReference("categorias")

        dref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(categoriaSnapshot in snapshot.children){
                        val categoria = categoriaSnapshot.getValue(Categoria::class.java)
                        categoriasArrayList.add(categoria!!)
                    }

                    categoriasRecyclerView.adapter = categoriaAdapter(categoriasArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }*/

    fun obtenerInfoUsuario(){
        var id: String =  mAuth.currentUser!!.uid
        mDatabase.child("usuarios").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NotNull dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    var nombre: String = dataSnapshot.child("nombre").value.toString()

                    txvNombreUsuario.text = nombre
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Database", error.toString())
            }
        })
    }*/
}