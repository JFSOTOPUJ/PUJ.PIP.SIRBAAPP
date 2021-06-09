package com.iepuj.sirbaapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*


class Adapter(var options: FirestoreRecyclerOptions<Registro>, var context: Context): FirestoreRecyclerAdapter<Registro, Adapter.ViewHolder>(options){

    var spanish = Locale("es", "ES")
    private val TAG = "ADAPTER_LOG"

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.registro_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Registro) {
        val tvFechaTxt = holder.itemView.findViewById<TextView>(R.id.fechaTxt)
        val tvFundacionTxt = holder.itemView.findViewById<TextView>(R.id.fundacionTxt)
        val tvPlacaTxt = holder.itemView.findViewById<TextView>(R.id.placaTxt)
        val tvConductorTxt = holder.itemView.findViewById<TextView>(R.id.conductorTxt)
        val tvLogo = holder.itemView.findViewById<ImageView>(R.id.tvLogo)



        holder.itemView.setOnClickListener {

            if(position != RecyclerView.NO_POSITION){
                holder.view.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
                    when(destination.id){
                        R.id.homeUserFragment -> {
                            val action = HomeUserFragmentDirections.actionHomeUserFragmentToDonatorFragment(model)
                            holder.view.findNavController().navigate(action)
                        }
                        R.id.homeFragment -> {
                            val action = HomeFragmentDirections.actionHomeFragmentToDonatorFragment(model)
                            holder.view.findNavController().navigate(action)
                        }
                    }
                }
            }
        }





        tvFechaTxt.text =  SimpleDateFormat("EEEE dd 'de' MMMM 'del' yyyy", spanish).format(model.FECHA?.toDate())
        tvFundacionTxt.text = model.DONADOR
        tvPlacaTxt.text = model.PLACA
        tvConductorTxt.text = model.CONDUCTOR
        Glide.with(context).load(model.LOGO).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(tvLogo)
    }

    fun deleteItem(position: Int){
        snapshots.getSnapshot(position).reference.delete()
    }

}