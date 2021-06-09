package com.iepuj.sirbaapp

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FirestoreRepo() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val date: String = SimpleDateFormat("dd-MM-yyyy").format(Date())
    private val TAG = "FIRESTOREREPO_LOG"

    fun getUser(keyUID: String): User?{
        val docRef = db.collection("Usuarios").document(keyUID)
        var user: User? = User()
        Log.d(TAG, "ADIOSSS")
        docRef.get().addOnSuccessListener {
            user = it.toObject<User>()
            Log.d(TAG, "HOLAAAA")
        }
        return user
    }

    fun createUser(): Task<AuthResult> {
        return firebaseAuth.signInAnonymously()
    }

    fun getQuery(): Query {
        return db.collection("donaciones")
            .document("fecha")
            .collection(date)
            .orderBy("FECHA", Query.Direction.ASCENDING)
    }


    fun agregarDb(donadorTxt: String, placaTxt: String, conductorTxt: String, tipoDonTxt: String, value: Int, numCanInt: Int, rutaTxt: String){

        val registro = hashMapOf(
            "FECHA" to FieldValue.serverTimestamp(),
            "DONADOR" to donadorTxt,
            "PLACA" to placaTxt,
            "CONDUCTOR" to conductorTxt,
            "RUTA" to rutaTxt,
            "FRUVER" to 0,
            "ASEO" to 0,
            "PANADERIA" to 0,
            "LACTEOS" to 0,
            "LOGO" to ""
        )

        val docRef = db.collection("donaciones").document("fecha").collection(date).document(donadorTxt)
        val docRef1 = db.collection("datos").document("peso")
        val docRef2 = db.collection("datos").document("logos")
        docRef1.get().addOnSuccessListener { documentSnapshot ->
            val pesos = documentSnapshot.toObject<Pesos>()
            Log.d(TAG, "Se consiguio correctamente el peso")
            val pesoTot = value - (numCanInt * pesos!!.canastilla) - pesos.estiba
            docRef.get().addOnSuccessListener{
                Log.d(TAG, it.toString())
                if (it != null) {
                    if (!it.exists()) {
                        docRef2.get().addOnSuccessListener {it2 ->
                            if(it2 != null){
                                if(it2.exists()){
                                    registro.replace(tipoDonTxt, pesoTot)
                                    if(it2.contains(donadorTxt)){
                                        registro.replace("LOGO", it2[donadorTxt]!!)
                                    }
                                    docRef.set(registro)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "Se escribio un registro nuevo")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error escribiendo un registro nuevo", e)
                                        }
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "ENTRO AKULLA")
                        docRef.update(tipoDonTxt, pesoTot)
                            .addOnSuccessListener {
                                Log.d(TAG, "Se actualizo un registro")

                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error actualizando un registro", e)
                            }
                    }
                }
            }
        }
            .addOnFailureListener { e -> Log.w(TAG, "Error consiguiendo el peso", e)}
    }
}