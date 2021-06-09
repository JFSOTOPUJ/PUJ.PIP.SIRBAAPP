package com.iepuj.sirbaapp

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Registro(
    val FECHA: Timestamp?= null,
    val DONADOR: String? = "",
    val PLACA: String? = "",
    val CONDUCTOR: String? = "",
    val FRUVER: Int = 0,
    val ASEO: Int = 0,
    val PANADERIA: Int = 0,
    val LACTEOS: Int = 0,
    val LOGO: String? = "",
    val RUTA: String? = ""
): Parcelable

data class Pesos(
    val canastilla: Int = 0,
    val estiba: Int = 0
)

data class BluetoothDevices(
    var name: String = "",
    var address: String = ""
)

data class User(
    var Nombre: String = "",
    var Apellido: String = "",
    var Documento: Int = 0,
    var Permisos: Int = 0
)