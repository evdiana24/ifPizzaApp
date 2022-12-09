package com.example.ifpizzaapp.repositories

import androidx.lifecycle.MutableLiveData
import com.example.ifpizzaapp.models.Categoria
import com.google.firebase.database.*

class CategoriaRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("categorias")

    @Volatile private var INSTANCE : CategoriaRepository ?= null

    fun getInstance() : CategoriaRepository{
        return INSTANCE ?: synchronized(this){

            val instance = CategoriaRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadUsers(categoriaList : MutableLiveData<List<Categoria>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _categoriaLists : List<Categoria> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Categoria::class.java)!!
                    }
                    categoriaList.postValue(_categoriaLists)
                }catch (e : Exception){
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}