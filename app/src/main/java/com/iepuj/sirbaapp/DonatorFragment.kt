package com.iepuj.sirbaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.iepuj.sirbaapp.databinding.FragmentDonatorBinding
import com.iepuj.sirbaapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class DonatorFragment : Fragment() {

    private lateinit var binding: FragmentDonatorBinding
    val args: DonatorFragmentArgs by navArgs()
    private val TAG = "DONATORFRAGMENT_LOG"
    var spanish = Locale("es", "ES")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentDonatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registro = args.registro
        Log.d(TAG, registro.toString())
        binding.apply {
            Glide.with(view).load(registro.LOGO).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(logoDonator)
            donadorInfoTv.text = registro.DONADOR
            diaTV.text =  SimpleDateFormat("EEEE dd 'de' MMMM 'del' yyyy", spanish).format(registro.FECHA?.toDate())
            horaTV.text = SimpleDateFormat("h:mm a").format(registro.FECHA?.toDate())
            conductorTV.text = registro.CONDUCTOR
            placaTV.text = registro.PLACA
            rutaTV.text = registro.RUTA
            fruverTV.text = registro.FRUVER.toString()
            panaderiaTV.text = registro.PANADERIA.toString()
            aseoTV.text = registro.ASEO.toString()
            lacteosTV.text = registro.LACTEOS.toString()

            homeDonatorButton.setOnClickListener{
                findNavController().navigateUp()
            }
        }
    }
}