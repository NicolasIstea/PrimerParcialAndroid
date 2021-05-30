package com.example.heladeria.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.heladeria.dto.HeladosPorCliente

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME: String = "Heladeria.db"
        private val DATABASE_VERSION: Int = 2
        val TABLA_PEDIDOSPORCLIENTE = "HeladosPorCliente"
        val COLUMN_ID = "id"
        val COLUMN_TIPOHELADO="tipohelado"
        val COLUMN_DIRECCIONCLIENTE="direccion"
        val COLUMN_ATENDIDOPOR="cajero"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        // las tablas en nuestra base de datos
        val CREATE_PEDIDOSPORCLIENTE_TABLE = ("CREATE TABLE " +
                TABLA_PEDIDOSPORCLIENTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TIPOHELADO + " TEXT,"
                + COLUMN_DIRECCIONCLIENTE + " TEXT"
                + ")")

        db?.execSQL(CREATE_PEDIDOSPORCLIENTE_TABLE);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        if(oldVersion != newVersion){
            db?.execSQL("DROP TABLE "+ TABLA_PEDIDOSPORCLIENTE )
            onCreate(db)
        }
    }

    fun guardarHeladoPorCliente(p: HeladosPorCliente){

        val db = this.writableDatabase


        val values = ContentValues()
        values.put(COLUMN_TIPOHELADO, p.tipoPedido)
        values.put(COLUMN_DIRECCIONCLIENTE, p.clienteDireccion)

        db.insert(TABLA_PEDIDOSPORCLIENTE,null,values)
    }


    fun obtenerHeladosPorCliente():ArrayList<HeladosPorCliente>{

        val query = "SELECT * FROM "+ TABLA_PEDIDOSPORCLIENTE
        val heladosPorCliente: ArrayList<HeladosPorCliente> = ArrayList<HeladosPorCliente>()
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(query,null)

        if(cursor != null && cursor.moveToFirst()){

            do {
                val tipohelado = cursor.getString(cursor.getColumnIndex("tipohelado"))
                val direccion = cursor.getString(cursor.getColumnIndex("direccion"))
                heladosPorCliente.add(HeladosPorCliente(tipohelado, direccion))
            }while(cursor.moveToNext())
        }

        return heladosPorCliente
    }


}