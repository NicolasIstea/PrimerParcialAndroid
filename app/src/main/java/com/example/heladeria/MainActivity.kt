package com.example.heladeria

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heladeria.adapter.ClienteAdapter
import com.example.heladeria.dao.DBHelper
import com.example.heladeria.dto.HeladosPorCliente
import com.example.heladeria.modelos.*

class MainActivity : AppCompatActivity() {

    lateinit var rgroupCajero:RadioGroup
    lateinit var rgroupHelado:RadioGroup
    lateinit var opcionCajero:RadioButton
    lateinit var opcionHelado:RadioButton
    lateinit var gustosHelados:EditText
    lateinit var comprar:Button
    lateinit var guardarVentas:Button
    lateinit var verInforme:Button

    lateinit var nombre:EditText
    lateinit var direccion:EditText

    val cajeros:ArrayList<Cajero> = ArrayList<Cajero>()
    val heladosPorCliente:ArrayList<HeladosPorCliente> = ArrayList<HeladosPorCliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargoCajeros()
        cargarCliente(Cliente("",""))

        rgroupCajero=findViewById(R.id.ma_rg_cajas)
        rgroupHelado=findViewById(R.id.ma_rg_helados)
        gustosHelados=findViewById(R.id.ma_e_gustos)
        comprar=findViewById(R.id.ma_comprar)
        guardarVentas=findViewById(R.id.ma_guardarVentasDia)
        verInforme=findViewById(R.id.ma_informeVentas)
        botonHabilitarDesabilitar(false, guardarVentas)
        botonHabilitarDesabilitar(false, verInforme)
        val db = DBHelper(this,null)

        guardarVentas.setOnClickListener(){

            heladosPorCliente.forEach() {
                db.guardarHeladoPorCliente(it)
            }

            botonHabilitarDesabilitar(false, guardarVentas)
            botonHabilitarDesabilitar(true, verInforme)

            dialogoCustomAvisosOk("Cierre del dia", "Se ha guardado exitosamente todas las ventas para el historico")
            dialogoCustomAvisosOk("Cajero", "El cajero que mas vendio fue " + cajeroQueMasVendio())

        }

        verInforme.setOnClickListener(){

            val intent: Intent = Intent(this, HistoricoActivity::class.java)

            startActivity(intent)
        }

        comprar.setOnClickListener(){

            opcionCajero=findViewById(rgroupCajero.checkedRadioButtonId)
            opcionHelado=findViewById(rgroupHelado.checkedRadioButtonId)

            nombre=findViewById(R.id.cl_ev_nombre)
            direccion=findViewById(R.id.cl_ev_direccion)

            if(nombre.text.isEmpty() || direccion.text.isEmpty() ) {
                dialogoCustomAvisosOk("Datos de cliente incompleto","Falta o bien completar el nombre o la direccion del cliente, completelo")
                return@setOnClickListener
            }

            if(!opcionCajero.isEnabled()) {
                dialogoCustomAvisosOk("Cajero Cerrado","El cajero seleccionado ya ha llegado a su tope de pedidos, se cerrara, por favor, elija otra caja")
                return@setOnClickListener
            }

            when(opcionCajero.text) {
                "Cajero 1"-> {
                    if(cajeros[0].helados.count() < 4) {
                        CagarHeladoAlCajero(0)
                    } else {
                        dialogoCustomAvisosOk("Cajero Cerrado","El cajero seleccionado ya ha llegado a su tope de pedidos, se cerrara, por favor, elija otra caja")
                        opcionCajero.isEnabled=false
                    }
                }
                "Cajero 2"-> {
                    if(cajeros[1].helados.count() < 9) {
                        CagarHeladoAlCajero(1)

                    } else {
                        dialogoCustomAvisosOk("Cajero Cerrado","El cajero seleccionado ya ha llegado a su tope de pedidos, se cerrara, por favor, elija otra caja")
                        opcionCajero.isEnabled=false
                    }
                }
                "Cajero 3"-> {
                    if(cajeros[2].helados.count() < 14) {
                        CagarHeladoAlCajero(2)

                    } else {
                        dialogoCustomAvisosOk("Cajero Cerrado","El cajero seleccionado ya ha llegado a su tope de pedidos, se cerrara, por favor, elija otra caja")
                        opcionCajero.isEnabled=false
                    }
                }
            }


        }
    }

    private fun botonHabilitarDesabilitar(habilitar:Boolean, boton:Button){

        if(habilitar){
            boton.isEnabled = true
            return
        }

        boton.isEnabled = false
    }

    @SuppressLint("WrongConstant")
    private fun cargarCliente(cliente:Cliente){
        val recycleViewCliente: RecyclerView = findViewById(R.id.ma_recycleViewCliente)

        recycleViewCliente.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        val adapterCliente = ClienteAdapter(cliente)

        recycleViewCliente.adapter=adapterCliente

        var view = LayoutInflater.from(this).inflate(R.layout.cliente_item,null)

        nombre =  view.findViewById(R.id.cl_ev_nombre)
        direccion = view.findViewById(R.id.cl_ev_direccion)

    }

    private fun CagarHeladoAlCajero(idCajero:Int){
        when(opcionHelado.text) {
            "Cono"-> {
                val gustos = seleccionGustos()
                val esValido = verificarGustosPorTipoHelado("Cono", gustos)

                if(esValido){
                    var helado:Helado = Helado(gustos,"Cono")
                    cajeros[idCajero].helados.add(helado)

                    var cliente:Cliente = Cliente(nombre.text.toString(), direccion.text.toString())
                    var heladoPorCliente: HeladosPorCliente = HeladosPorCliente(helado.tipoHelado, cliente.direccion, cajeros[idCajero].nombre)
                    heladosPorCliente.add(heladoPorCliente)
                    Toast.makeText(this,"Cono Helado agregado correctamente", Toast.LENGTH_LONG).show()

                    botonHabilitarDesabilitar(true, guardarVentas)
                }
            }
            "1/4 Helado" -> {
                val gustos = seleccionGustos()
                val esValido = verificarGustosPorTipoHelado("1/4 Helado",gustos)

                if(esValido){
                    var helado:Helado = Helado(gustos,"1/4 Helado")

                    cajeros[idCajero].helados.add(helado)

                    var cliente:Cliente = Cliente(nombre.text.toString(), direccion.text.toString())
                    var heladoPorCliente: HeladosPorCliente = HeladosPorCliente(helado.tipoHelado, cliente.direccion, cajeros[idCajero].nombre)
                    heladosPorCliente.add(heladoPorCliente)

                    Toast.makeText(this,"1/4 Helado agregado correctamente", Toast.LENGTH_LONG).show()

                    botonHabilitarDesabilitar(true, guardarVentas)
                }
            }
            "1 Kg Helado" -> {
                val gustos = seleccionGustos()

                val esValido = verificarGustosPorTipoHelado("1 Kg Helado", gustos)

                if(esValido) {
                    var helado:Helado = Helado(gustos,"1 Kg Helado")
                    cajeros[idCajero].helados.add(helado)

                    var cliente:Cliente = Cliente(nombre.text.toString(), direccion.text.toString())
                    var heladoPorCliente: HeladosPorCliente = HeladosPorCliente(helado.tipoHelado, cliente.direccion, cajeros[idCajero].nombre)
                    heladosPorCliente.add(heladoPorCliente)

                    Toast.makeText(this,"1 Kg Helado agregado correctamente", Toast.LENGTH_LONG).show()

                    botonHabilitarDesabilitar(true, guardarVentas)
                }
            }
        }

    }

    private  fun cajeroQueMasVendio():String{

        val cajerosAgrupadosPorId = heladosPorCliente.groupingBy { it.cajero }.eachCount()

        val cajeroQueMasVendio = cajerosAgrupadosPorId.maxBy { it.value }

        if (cajeroQueMasVendio != null) {
            return  cajeroQueMasVendio.key
        }

        return ""
    }

    private fun dialogoCustomAvisosOk(titulo:String, mensaje:String) {

        var builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)

        builder.setPositiveButton("Ok"){dialogInterface, i ->
        }

        val alerta : AlertDialog = builder.create()
        alerta.show()
    }

    private fun verificarGustosPorTipoHelado(tipoHelado:String, gustos:ArrayList<Gustos>):Boolean {

        when(tipoHelado) {
            "Cono" -> {
                if(gustos.count() > 2) {
                    Toast.makeText(this,"El cono solo debe tener 2 gustos", Toast.LENGTH_LONG).show()
                    return false
                } else if (gustos[0].gusto == ""){
                    Toast.makeText(this,"Debe elegir al menos dos gustos para el cono de helado", Toast.LENGTH_LONG).show()
                    return false
                }
            }
            "1/4 Helado" -> {
                if(gustos.count() > 3) {
                    Toast.makeText(this,"El 1/4 solo debe tener 3 gustos", Toast.LENGTH_LONG).show()
                    return false
                } else if (gustos[0].gusto == ""){
                    Toast.makeText(this,"Debe elegir al menos 3 gustos para el 1/4 de helado", Toast.LENGTH_LONG).show()
                    return false
                }
            }
            "1 Kg Helado" -> {
                if(gustos.count() > 4) {
                    Toast.makeText(this,"El Kg de Helado solo debe tener 4 gustos", Toast.LENGTH_LONG).show()
                    return false
                } else if (gustos[0].gusto == ""){
                    Toast.makeText(this,"Debe elegir al menos 4 gustos para el Kg de helado", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }

        return true
    }

    //No se me ocurrio otra forma de hacerlo, mi idea original era poner una lista de checkboxes en el recycle view
    //Pero no creo que vimos una especie de checkbox group o algo asi (tipo pedidos ya)
    private fun seleccionGustos():ArrayList<Gustos> {
        val gustos:ArrayList<Gustos> = ArrayList<Gustos>()

        var gustosEscritos: List<String> = gustosHelados.text.split(",").map { it.trim() }

        gustosEscritos.forEach() {
            val gusto:Gustos = Gustos(it)
            gustos.add(gusto)
        }

        return gustos;
    }

    private  fun cargoCajeros(){
        val heladosCajero1:ArrayList<Helado> = ArrayList<Helado>()
        val heladosCajero2:ArrayList<Helado> = ArrayList<Helado>()
        val heladosCajero3:ArrayList<Helado> = ArrayList<Helado>()

        cajeros.add(Cajero(1, "Cajero1", heladosCajero1))
        cajeros.add(Cajero(2, "Cajero2", heladosCajero2))
        cajeros.add(Cajero(3, "Cajero3", heladosCajero3))
    }

}