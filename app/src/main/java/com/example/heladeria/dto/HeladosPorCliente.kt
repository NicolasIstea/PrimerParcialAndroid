package com.example.heladeria.dto

import com.example.heladeria.modelos.Cliente
import com.example.heladeria.modelos.Helado

data class HeladosPorCliente(val tipoPedido: String, val clienteDireccion: String, val cajero:String = "") {
}