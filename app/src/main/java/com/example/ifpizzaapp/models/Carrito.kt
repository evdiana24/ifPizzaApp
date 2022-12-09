package com.example.ifpizzaapp.models

class Carrito(val producto: String?,
              val precio: Float,
              val url: String?,
              val cantidad: Int,
              val productoId: String?,
              var totalCarrito: Float = 0f)  {
}