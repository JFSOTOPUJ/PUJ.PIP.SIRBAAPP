package com.iepuj.sirbaapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.iepuj.sirbaapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LOGIN_LOG"
    private val dB = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.apply {
              loginButton.setOnClickListener{
                auth.signInWithEmailAndPassword(emailTxt.text.toString(),
                    passwordTxt.text.toString()
                    ).addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.d(TAG, "signInWithEmail:success")
                        val userID = it.result?.user?.uid
                        Log.d(TAG, userID.toString())
                        var user: User? = User()
                        dB.collection("Usuarios").document(userID!!).get().addOnSuccessListener {dS ->
                            Log.d(TAG, "HOLAAA")
                            user = dS.toObject<User>()
                            Log.d(TAG, user.toString())
                            if(user?.Permisos == 1){
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }else{
                                findNavController().navigate(R.id.action_loginFragment_to_homeUserFragment)
                            }
                        }


                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}