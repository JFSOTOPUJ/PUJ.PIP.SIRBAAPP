package com.iepuj.sirbaapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.UUID.fromString

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
// ... (Add other message types here as needed.)

class MyBluetoothService(private val handler: Handler) {

    private var mConnectThread: MyBluetoothService.ConnectThread? = null

    companion object {
        var UUID: UUID = fromString("00001101-0000-1000-8000-00805F9B34FB")
        var deviceArduino: BluetoothDevice? = null
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        var mmSocket: BluetoothSocket? = null
        private val TAG = "MY_BLUETOOTH_SERVICE"
        private var mConnectedThread: MyBluetoothService.ConnectedThread? = null
    }

    // Se conecta el dispositivo

    fun guardarDevice(device: BluetoothDevice){
        deviceArduino = device
    }

    fun retornarDevice(): BluetoothDevice?{
        Log.i(TAG, "Existe dispositivo: $deviceArduino")
        return deviceArduino
    }

    fun cancel(){
        mConnectThread?.cancel()
        mConnectedThread?.cancel()
    }

    fun isConnected(): Int{
        if(mmSocket != null){
            if(mmSocket!!.isConnected){
                return 1
            }else{
                return 0
            }
        }else
            return 0
    }


    fun connect(device: BluetoothDevice){
        Log.i(TAG, "Conectando dispositivo: ${device.name}")
        mmSocket = device.createRfcommSocketToServiceRecord(UUID)
        mmSocket!!.connect()
        Log.i(TAG, "El dispositivo ${device.name} ha sido conectado: ${mmSocket!!.isConnected}")
        connected(mmSocket!!)
    }


    private fun connected(mmSocket: BluetoothSocket){
        Log.i(TAG, "Creando hilo de servicios para el socket: $mmSocket")
        mConnectedThread = ConnectedThread(mmSocket)
        mConnectedThread?.start()
        Log.d(TAG, "El hilo de servicio esta conectado: ${mConnectedThread?.isAlive}")
    }

    fun write(strMsg: String) {
        Log.d(TAG, "Enviando: $strMsg")
        if(mConnectedThread?.isAlive != true){
            Log.d(TAG, "Se esta reconectando el hilo de servicios")
            mConnectedThread = ConnectedThread(mmSocket!!)
            mConnectedThread?.start()
        }
        Log.d(TAG, "El hilo de servicio esta conectado: ${mConnectedThread?.isAlive}")
        Log.d(TAG, "El socket sigue conectado: ${mmSocket!!.isConnected}")

        var r: ConnectedThread?

        synchronized(this) {
            r = mConnectedThread
        }
        val msg = strMsg.toByteArray()
        Log.d(TAG, "Se envia en bytes: $msg")
        Log.d(TAG, "Se envia en string: ${String(msg)}")
        r?.write(msg)
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(UUID)
        }

        override fun run() {
            Log.i(TAG, "Begin connecting Socket")
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.use { socket ->
                socket.connect()
                Log.i(TAG, "Socket ha sido conetado correctamente: ${socket.isConnected}")
                MyBluetoothService.mmSocket = socket
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    private inner class ConnectedThread(mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            Log.i(TAG, "El hilo del socket esta conectado: ${mConnectedThread?.isAlive.toString()}")

            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
                Log.i(TAG, "Ha recibido: $numBytes byte")

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer)

                readMsg.sendToTarget()

            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                    MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}