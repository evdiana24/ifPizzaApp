package com.example.ifpizzaapp.models

data class Productos(var nombre : String ?= null,
                var precio : String ?= null,
                var imagenURL : String ?= null,
                var nombreCategoria: String ?= null,
                var cantidad: Int = 0,
                var productoId: String ?= null) {
}