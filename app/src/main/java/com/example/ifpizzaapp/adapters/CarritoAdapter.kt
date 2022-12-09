package com.example.ifpizzaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ifpizzaapp.R
import com.example.ifpizzaapp.models.Carrito
import com.example.ifpizzaapp.models.Productos

class CarritoAdapter(val listaCarrito: ArrayList<Carrito>): RecyclerView.Adapter<CarritoAdapter.MyViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.carrito_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(listaCarrito[position])


    }

    override fun getItemCount(): Int {
        return if(listaCarrito == null) return 0 else listaCarrito.size
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgItemCarrito: ImageView = view.findViewById(R.id.imgItemCarrito)
        val itemNombre: TextView = view.findViewById(R.id.txvItemCarritoNombre)
        val itemPrecio: TextView = view.findViewById(R.id.txvItemCarritoPrecio)
        val itemCantidad: TextView = view.findViewById(R.id.txvItemCarritoCantidad)
        val itemSubtotal: TextView = view.findViewById(R.id.txvItemCarritoSubtotal)

        fun bind(carrito: Carrito) {
           try {
               itemNombre.text = carrito?.producto!!
               itemPrecio.text = "Precio: $" + String.format("%.2f",carrito?.precio)
               itemCantidad.text = "Cantidad: " + carrito?.cantidad


               if(carrito.cantidad > 1){
                   itemSubtotal.text = "Sub-total:" + (carrito?.cantidad*carrito.precio)
               }
               else{
                   itemSubtotal.visibility = View.GONE
               }

               Glide.with(imgItemCarrito)
                   .load(carrito?.url)
                   .into(imgItemCarrito)
           }catch (e: Exception){

           }
        }
    }
}