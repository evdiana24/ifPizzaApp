package com.example.ifpizzaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ifpizzaapp.R
import com.example.ifpizzaapp.models.Categoria
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class CategoriaAdapter : RecyclerView.Adapter<CategoriaAdapter.MyViewHolder>() {

    private lateinit var context: Context
    private val categoriaList = ArrayList<Categoria>()


    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.categorias_item,
            parent,false
        )
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = categoriaList[position]

        holder.nombre.text = currentitem.nombre

        Glide.with(context)
            .load(currentitem.imagenURL)
            .into(holder.imagen)
    }

    override fun getItemCount(): Int {
        return categoriaList.size
    }

    fun updateCategoryList(categoriaList : List<Categoria>){

        this.categoriaList.clear()
        this.categoriaList.addAll(categoriaList)
        notifyDataSetChanged()

    }



    class  MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val imagen : ImageView = itemView.findViewById(R.id.imgItemCategorias)
        val nombre : TextView = itemView.findViewById(R.id.txvNombreCategoria)

        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}