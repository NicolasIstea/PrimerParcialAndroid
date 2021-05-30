package com.example.heladeria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heladeria.R
import com.example.heladeria.dto.HeladosPorCliente

class HistoricoAdapter(val dataset: List<HeladosPorCliente>) : RecyclerView.Adapter<HistoricoAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val numeroPedido: TextView
        val tipoPedido: TextView
        val clienteDireccion: TextView

        init {
            numeroPedido = view.findViewById(R.id.hi_numeroPedido)
            tipoPedido = view.findViewById(R.id.hi_tipoPedido)
            clienteDireccion = view.findViewById(R.id.hi_direccion)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.historico_item,parent,false)

        return HistoricoAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.count()
    }

    override fun onBindViewHolder(holder: HistoricoAdapter.ViewHolder, position: Int) {
        holder.numeroPedido.setText("Pedido numero: " + position)
        holder.tipoPedido.setText("El tipo de pedido fue: " + dataset[position].tipoPedido)
        holder.clienteDireccion.setText("Direccion a la que fue enviada: " + dataset[position].clienteDireccion)
    }
}