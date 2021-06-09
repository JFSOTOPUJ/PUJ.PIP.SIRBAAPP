package com.iepuj.sirbaapp

import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iepuj.sirbaapp.databinding.FragmentAddBinding
import java.io.IOException


class AddFragment : Fragment() {

    private val TAG = "ADDREGISTERFRAGMENT_LOG"
    private lateinit var binding: FragmentAddBinding
    private val db = Firebase.firestore
    lateinit var mHandler: Handler
    private val firestoreRepo = FirestoreRepo()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayListPlacas = ArrayList<String>()
        val arrayListConductores = ArrayList<String>()
        val arrayListRutas = ArrayList<String>()
        val arrayListDonantes = ArrayList<String>()
        val arrayListTipos = resources.getStringArray(R.array.tipos_de_donaciones)

        val arrayAdapterPlacas =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, arrayListPlacas)
        val arrayAdapterConductores =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, arrayListConductores)
        val arrayAdapterRutas =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, arrayListRutas)
        val arrayAdapterDonantes =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, arrayListDonantes)
        val arrayAdapterTipos =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, arrayListTipos)

        mHandler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when(msg.what){
                    MESSAGE_READ -> {
                        val readMessage = String(msg.obj as ByteArray, 0, msg.arg1)
                        Log.i(TAG,"El mensaje es: $readMessage")
                        binding.pesoDonTxt.setText(readMessage.toString())
                    }
                }
            }
        }

        val bluetoothService = MyBluetoothService(mHandler)
        if(bluetoothService.isConnected() == 0){
            try{
                val device = bluetoothService.retornarDevice()
                if(device != null){
                    bluetoothService.connect(device)
                }
            }catch(e: IOException){
                Toast.makeText(requireContext(), "La conexión ha sido fallida", Toast.LENGTH_LONG).show()
            }
        }







        binding.apply {

            PlacaTxt.setAdapter(arrayAdapterPlacas)
            ConductorTxt.setAdapter(arrayAdapterConductores)
            RutasTxt.setAdapter(arrayAdapterRutas)
            DonanteTxt.setAdapter(arrayAdapterDonantes)
            tipoDonTxt.setAdapter(arrayAdapterTipos)

            db.collection("BdA Cali").document("datos").get().addOnSuccessListener {
                if (it != null) {
                    arrayListPlacas.clear()
                    arrayListPlacas.addAll(it.get("placas") as ArrayList<String>)
                    Log.d(TAG, "Se encontró $arrayListPlacas")
                    arrayListConductores.clear()
                    arrayListConductores.addAll(it.get("conductores") as ArrayList<String>)
                    Log.d(TAG, "Se encontró $arrayListConductores")
                    arrayListRutas.clear()
                    arrayListRutas.addAll(it.get("rutas") as ArrayList<String>)
                    Log.d(TAG, "Se encontró $arrayListRutas")

                    arrayAdapterPlacas.notifyDataSetChanged()
                    arrayAdapterConductores.notifyDataSetChanged()
                    arrayAdapterRutas.notifyDataSetChanged()
                }
            }

            syncDbButton.setOnClickListener {
                db.collection("BdA Cali").document("datos").collection("donantes")
                    .document(RutasTxt.text.toString()).get().addOnSuccessListener {
                    if (it != null) {
                        arrayListDonantes.clear()
                        arrayListDonantes.addAll(it.get("ID") as ArrayList<String>)
                        Log.d(TAG, "Se encontró ${it.get("ID")}")
                        arrayAdapterDonantes.notifyDataSetChanged()
                    }
                }
            }

            syncValues.setOnClickListener {
                try{
                    bluetoothService.write("R")
                }catch (e: IOException){
                    Toast.makeText(requireContext(), "La conexión ha sido fallida", Toast.LENGTH_LONG).show()
                }
            }

            sendButton.setOnClickListener {
                val conductorTxt = ConductorTxt.text.toString()
                val numCanInt = Integer.parseInt(numCanTxt.text.toString())
                val pesoDonInt = Integer.parseInt(pesoDonTxt.text.toString())
                val donanteTxt = DonanteTxt.text.toString()
                val tipoDonTxt = tipoDonTxt.text.toString()
                val placaTxt = PlacaTxt.text.toString()

                try {
                    Toast.makeText(requireContext(), "Enviando información", Toast.LENGTH_SHORT)
                        .show()
                    firestoreRepo.agregarDb(
                        donanteTxt,
                        placaTxt,
                        conductorTxt,
                        tipoDonTxt,
                        pesoDonInt,
                        numCanInt,
                        RutasTxt.text.toString()
                    )
                    Toast.makeText(requireContext(), "Listo", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }

            homeAddButton.setOnClickListener {
                bluetoothService.cancel()
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
            }
        }

    }
}

