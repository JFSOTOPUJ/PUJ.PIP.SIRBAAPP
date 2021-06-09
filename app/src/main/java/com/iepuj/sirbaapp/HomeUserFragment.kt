package com.iepuj.sirbaapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.iepuj.sirbaapp.databinding.FragmentHomeBinding
import com.iepuj.sirbaapp.databinding.FragmentHomeUserBinding

class HomeUserFragment : Fragment() {

    private val TAG = "HOMEUSERFRAGMENT_LOG"
    private val firestoreRepo = FirestoreRepo()
    private lateinit var binding: FragmentHomeUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
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
    }
}