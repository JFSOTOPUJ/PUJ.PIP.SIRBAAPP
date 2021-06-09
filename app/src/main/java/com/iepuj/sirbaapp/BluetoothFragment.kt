package com.iepuj.sirbaapp

import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import com.iepuj.sirbaapp.databinding.FragmentAddBinding
import com.iepuj.sirbaapp.databinding.FragmentBluetoothBinding
import java.io.IOException
import kotlin.collections.ArrayList

class BluetoothFragment : Fragment() {

    private lateinit var binding: FragmentBluetoothBinding
    private val REQUEST_ENABLE_BT = 1
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val TAG = "BLUETOOTHFRAGMENT_LOG"
    private lateinit var handler: Handler


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.i(TAG,"HOLA")
            }
        }

        val bluetoothService = MyBluetoothService(handler)


        if(bluetoothAdapter == null) {
            Toast.makeText(requireContext(), "Bluetooth no disponible", Toast.LENGTH_SHORT).show()
            return
        }else{
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
        binding.syncButton.setOnClickListener {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
            val pairedDevicesNameList : ArrayList<String> = ArrayList()
            val pairedDevicesArray : ArrayList<BluetoothDevice> = ArrayList()
            pairedDevices?.forEach { device ->
                pairedDevicesNameList.add(device.name)
                pairedDevicesArray.add(device)
            }

            val listAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, pairedDevicesNameList)
            binding.deviceListTv.adapter = listAdapter

            binding.deviceListTv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val device = pairedDevicesArray[position]
                Log.i(TAG, "Existe dispositivo: $device")
                try{
                    bluetoothService.guardarDevice(device)
                    //bluetoothService.connect(device)
                    Toast.makeText(requireContext(), "La conexión ha sido exitosa", Toast.LENGTH_LONG).show()
                    //bluetoothService.write("R")
                }catch(e: IOException){
                    Toast.makeText(requireContext(), "La conexión ha sido fallida", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.backButton.setOnClickListener{
            findNavController().navigateUp()
        }
    }
}
