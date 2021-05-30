package com.example.heladeria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heladeria.R
import com.example.heladeria.modelos.Cliente

class ClienteAdapter(val dataset: Cliente) : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val nombre: EditText
        val direccion: EditText

        init {
            nombre = view.findViewById(R.id.cl_ev_nombre)
            direccion = view.findViewById(R.id.cl_ev_direccion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cliente_item,parent,false)

        return ClienteAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ClienteAdapter.ViewHolder, position: Int) {
        holder.nombre.setText(dataset.nombre)
        holder.direccion.setText(dataset.direccion)
    }
}