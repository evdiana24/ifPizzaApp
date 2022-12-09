package com.example.ifpizzaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpizzaapp.R
import com.example.ifpizzaapp.models.Pedidos

class PedidosAdapter(private val pedidosList: ArrayList<Pedidos>)
    : RecyclerView.Adapter<PedidosAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pedidos_item,
            parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = pedidosList[position]

        holder.fecha.text = item.fecha
        holder.direccion.text = item.direccion
        holder.status.text = item.estatus
        holder.total.text = "$"+item.total.toString()
    }

    override fun getItemCount(): Int {
        return pedidosList.size
    }

    inner class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var fecha : TextView = itemView.findViewById(R.id.txvFechaOrden)
        val direccion : TextView = itemView.findViewById(R.id.txvDireccionOrden)
        val status : TextView = itemView.findViewById(R.id.txvStatusOrden)
        val total: TextView = itemView.findViewById(R.id.txvTotalOrden)
    }
}