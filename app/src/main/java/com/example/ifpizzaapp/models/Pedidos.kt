package com.example.ifpizzaapp.models

class Pedidos(val direccion: String ?= null,
              val total: Float = 0f,
              val fecha: String ?= null,
              val estatus: String ?= null){
}