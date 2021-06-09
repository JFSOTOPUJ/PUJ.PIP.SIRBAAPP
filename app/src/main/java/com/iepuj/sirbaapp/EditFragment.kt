package com.iepuj.sirbaapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iepuj.sirbaapp.databinding.FragmentEditBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    val args: EditFragmentArgs by navArgs()
    private val TAG = "EDITRFRAGMENT_LOG"
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registro = args.registro
        Log.d(TAG, "Se recibio $registro")
        binding.apply {
            kilosFruverTxt.setText(registro.FRUVER.toString())
            kilosLacteosTxt.setText(registro.LACTEOS.toString())
            kilosPanaderiaTxt.setText(registro.PANADERIA.toString())
            kilosAseoTxt.setText(registro.ASEO.toString())

            var newFruver = 0
            var newLacteos = 0
            var newPanaderia = 0
            var newAseo = 0

            kilosFruverTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    newFruver = 1
                    Log.i(TAG, "Se ha cambiado FRUVER")
                }

            })

            kilosLacteosTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    newLacteos = 1
                    Log.i(TAG, "Se ha cambiado LACTEOS")
                }

            })

            kilosPanaderiaTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    newPanaderia = 1
                    Log.i(TAG, "Se ha cambiado PANADERIA")
                }

            })

            kilosAseoTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    newAseo = 1
                    Log.i(TAG, "Se ha cambiado ASEO")
                }

            })

            editButton.setOnClickListener{
                var date = SimpleDateFormat("dd-MM-yyyy").format(Date())
                var docRef = db.collection("donaciones").document("fecha").collection(date).document(
                    registro.DONADOR.toString())
                try{
                    if(newFruver == 1){
                        docRef.update("FRUVER", kilosFruverTxt.text.toString().toInt()).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                    }
                    if(newLacteos == 1){
                        docRef.update("LACTEOS", kilosLacteosTxt.text.toString().toInt()).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                    }
                    if(newPanaderia == 1){
                        docRef.update("PANADERIA", kilosPanaderiaTxt.text.toString().toInt()).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                    }
                    if(newAseo == 1){
                        docRef.update("ASEO", kilosAseoTxt.text.toString().toInt()).addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
                    }
                    Toast.makeText(requireContext(), "Los datos se han actualizado", Toast.LENGTH_SHORT).show()
                    //Navigation.findNavController(view).navigate(R.id.action_editFragment_to_homeFragment)
                }catch(e: IOException){
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.action_editFragment_to_homeFragment)
                }


            }
        }
    }
}