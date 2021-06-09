package com.iepuj.sirbaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.iepuj.sirbaapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val TAG = "HOMEFRAGMENT_LOG"
    private val firestoreRepo = FirestoreRepo()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = FirestoreRecyclerOptions.Builder<Registro>().setQuery(firestoreRepo.getQuery(), Registro::class.java)
            .setLifecycleOwner(this).build()
        mostrarRegistro(options)

    }

    private fun mostrarRegistro(options: FirestoreRecyclerOptions<Registro>){
        binding.rvRegistro.layoutManager = LinearLayoutManager(context)
        val adapter = Adapter(options, requireContext())
        binding.rvRegistro.adapter = adapter
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.deleteItem(viewHolder.adapterPosition)
                }
            }
        ).attachToRecyclerView(binding.rvRegistro)
    }
}