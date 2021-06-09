package com.iepuj.sirbaapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.iepuj.sirbaapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dB = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            loginLink.setOnClickListener{
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }

            if(auth.currentUser != null){
                var user: User = User()
                dB.collection("Usuarios").document(auth.currentUser!!.uid).get().addOnSuccessListener { dS ->
                    user = dS.toObject<User>()!!
                    if(user.Permisos == 1){
                        findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
                    }else{
                        findNavController().navigate(R.id.action_welcomeFragment_to_homeUserFragment)
                    }
                }
            }

        }

    }


}