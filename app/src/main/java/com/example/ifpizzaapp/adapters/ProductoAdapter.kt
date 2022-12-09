package com.example.ifpizzaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ifpizzaapp.R
import com.example.ifpizzaapp.models.Carrito
import com.example.ifpizzaapp.models.Productos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext

class ProductoAdapter(private val productList: ArrayList<Productos>, var clickListener: ProductosListClickListener)
    : RecyclerView.Adapter<ProductoAdapter.MyViewHolder>(){
    private lateinit var auth: FirebaseAuth

    private lateinit var dbref: DatabaseReference
    private lateinit var context: Context
    lateinit var mAuth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.producto_item,
            parent, false)

     try {
         auth = FirebaseAuth.getInstance()
         dbref = FirebaseDatabase.getInstance().getReference("Carrito")

         mAuth = FirebaseAuth.getInstance()

     }catch (e: Exception){

    }
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productList?.get(position)!!)

    }

    override fun getItemCount(): Int {
        //return productList.size

        return if(productList == null)return 0 else productList.size
    }

    inner class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imagen : ImageView = itemView.findViewById(R.id.imgProducto)
        val nombre : TextView = itemView.findViewById(R.id.nombreProducto)
        val precio : TextView = itemView.findViewById(R.id.precioProducto)
        val categoria: TextView = itemView.findViewById(R.id.categoriaProducto)

        val btnAddACarrito: TextView = itemView.findViewById(R.id.btnAddACarrito)
        val addMoreLayout: LinearLayout = itemView.findViewById(R.id.addMoreLayout)
        val imageMinus: ImageView = itemView.findViewById(R.id.imageMinus)
        val imageAddOne: ImageView = itemView.findViewById(R.id.imageAddOne)
        val tvCount: TextView = itemView.findViewById(R.id.tvCount)

        fun bind(producto: Productos) {
            try {
                nombre.text = producto?.nombre
                precio.text = "Precio: $ ${producto?.precio}"
                categoria.text = producto?.nombreCategoria
                //validar si el producto ya esta en el carrito de firebase
                val uid = mAuth.currentUser?.uid
                try {
                    dbref = FirebaseDatabase.getInstance().getReference("carrito/"+uid+"/"+producto?.productoId)
                    dbref.get().addOnSuccessListener {
                        if(it.exists()){
                            var cantidad = it.child("cantidad").value.toString().toInt()
                            addMoreLayout.visibility = View.VISIBLE
                            btnAddACarrito.visibility = View.GONE
                            tvCount.text = cantidad.toString()
                            producto?.cantidad = cantidad
                        }else{
                            addMoreLayout.visibility = View.GONE
                            btnAddACarrito.visibility = View.VISIBLE
                        }
                    }
                }catch (e: Exception){
                    Toast.makeText(context, "Erro332r: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                btnAddACarrito.setOnClickListener {
                    producto?.cantidad = 1
                    clickListener.addToCartClickListener(producto)
                    addMoreLayout?.visibility = View.VISIBLE
                    btnAddACarrito.visibility = View.GONE
                    tvCount.text = producto?.cantidad.toString()
                }
                imageMinus.setOnClickListener {
                    var total: Int =  producto?.cantidad
                    total--

                    if(total > 0) {

                        producto?.cantidad = total
                        clickListener.updateCartClickListener(producto)
                        tvCount.text = producto?.cantidad.toString()
                        //actualizar firebase
                        val uid = mAuth.currentUser?.uid
                       if(uid!=null){
                           val productoId = producto.productoId
                           dbref = FirebaseDatabase.getInstance().getReference("carrito/");
                           if (uid != null) {
                               if (productoId != null) {
                                   dbref.child(uid).child(productoId).setValue(producto)
                               }
                           }
                       }

                    } else {
                        try {


                            producto.cantidad = total
                            clickListener.removeFromCartClickListener(producto)
                            addMoreLayout.visibility = View.GONE
                            btnAddACarrito.visibility = View.VISIBLE
                            //eliminar del carrito de firebase
                            val user = mAuth.currentUser
                            val uid = user?.uid
                            val id = producto.productoId

                            dbref = FirebaseDatabase.getInstance().getReference("carrito/"+uid+"/"+id)
                            dbref.removeValue()

                            val ref = dbref.child(uid!!).child(id!!)
                            ref.removeValue()
                        }catch (e: Exception){

                        }

                    }
                }
                imageAddOne.setOnClickListener {
                    try {
                        var total: Int  = producto?.cantidad
                        total++
                        if(total <= 10) {
                            producto.cantidad = total
                            clickListener.updateCartClickListener(producto)
                            tvCount.text = total.toString()
                            dbref = FirebaseDatabase.getInstance().getReference("carrito")
                            var id = auth.currentUser?.uid.toString()
                            var productoId = producto?.productoId.toString()


                            dbref.child(id).child(productoId).setValue(producto)
                        }
                    }catch (e: Exception){
                        Toast.makeText(context, "Errorrr: ${e.message}", Toast.LENGTH_SHORT).show()

                    }

                }
                Glide.with(imagen)
                    .load(producto.imagenURL)
                    .into(imagen)
            }catch (e: Exception){
                Toast.makeText(context, "Error33: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*fun addToCart(producto: Productos){
        val carritoUsuario = FirebaseDatabase.getInstance()
            .getReference("carrito")
            .child(mAuth.currentUser!!.uid)

        carritoUsuario.child(producto.)
    }*/

    interface ProductosListClickListener {
        fun addToCartClickListener(producto: Productos)
        fun updateCartClickListener(producto: Productos)
        fun removeFromCartClickListener(producto: Productos)
    }
}