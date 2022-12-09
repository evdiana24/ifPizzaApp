package com.example.ifpizzaapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ifpizzaapp.repositories.CategoriaRepository

class CategoriaViewModel : ViewModel() {

    private val repository : CategoriaRepository
    private val _allCategories = MutableLiveData<List<Categoria>>()
    val allCategories : LiveData<List<Categoria>> = _allCategories


    init {
        repository = CategoriaRepository().getInstance()
        repository.loadUsers(_allCategories)
    }
}