package com.example.heladeria

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heladeria.adapter.ClienteAdapter
import com.example.heladeria.adapter.HistoricoAdapter
import com.example.heladeria.dao.DBHelper
import com.example.heladeria.dto.HeladosPorCliente
import kotlin.system.exitProcess

class HistoricoActivity : AppCompatActivity() {

    lateinit var cerrar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        cerrar = findViewById(R.id.ah_b_cerrar)

        val pedidos = traerPedidosHistorico()
        setearRecycleViewHistorico(pedidos)

        cerrar.setOnClickListener(){
            finish();
            exitProcess(0);
        }

    }

    private fun traerPedidosHistorico():ArrayList<HeladosPorCliente> {

        val db = DBHelper(this,null)

        return db.obtenerHeladosPorCliente()
    }

    @SuppressLint("WrongConstant")
    private fun setearRecycleViewHistorico(pedidos:ArrayList<HeladosPorCliente>) {

        val recycleViewHistorico: RecyclerView = findViewById(R.id.ah_rw_historico)
        recycleViewHistorico.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        val adapterHistorico = HistoricoAdapter(pedidos)

        recycleViewHistorico.adapter=adapterHistorico
    }
}