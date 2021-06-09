package com.iepuj.sirbaapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.iepuj.sirbaapp.databinding.ActivityMainBinding

const val HOME_FRAGMENT: Int = 0
const val ADD_FRAGMENT: Int = 1
const val BLUETOOTH_FRAGMENT: Int = 2
const val WELCOME_FRAGMENT: Int = 3
const val HOMEUSER_FRAGMENT: Int = 4
const val DONATOR_FRAGMENT: Int = 5
const val EDIT_FRAGMENT: Int = 6


class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private val TAG = "MAINACTIVITY_LOG"
    var lastFragment: Int = WELCOME_FRAGMENT
    var actualFragment: Int = WELCOME_FRAGMENT
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var registro: Registro = Registro()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomAppBar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.menu_logout -> logOut()
                R.id.menu_bluetooth -> {
                    navigateToBluetooth(actualFragment)
                    lastFragment = actualFragment
                }

            }
            return@setOnMenuItemClickListener true
        }
        settingNavButtons()
        onDestinationChangedListener()
    }

    private fun settingNavButtons(){
        binding.apply {
            fabHome.setOnClickListener {
                when(actualFragment){
                    HOME_FRAGMENT -> {
                        navigateToAdd()
                    }
                    ADD_FRAGMENT -> {
                        navigateToHome(actualFragment)
                    }
                    DONATOR_FRAGMENT -> {
                        navigateToEdit()
                    }
                }
            }
        }
    }

    private fun navigateToEdit() {
        Log.d(TAG, "Se va a enviar a editar $registro")
        val action = DonatorFragmentDirections.actionDonatorFragmentToEditFragment(registro)
        findNavController(R.id.fragment).navigate(action)
    }


    private fun onDestinationChangedListener() {
        binding.apply {
            findNavController(R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
                lastFragment = actualFragment
                when(destination.id){
                    R.id.homeUserFragment -> {
                        Log.d(TAG, "El fragmento actual es: HomeUSER")
                        bottomAppBar.replaceMenu(R.menu.homeuser_menu)
                        fabHome.hide()
                        bottomAppBar.visibility = View.VISIBLE
                        actualFragment = HOMEUSER_FRAGMENT
                    }
                    R.id.homeFragment -> {
                        Log.d(TAG, "El fragmento actual es: Home")
                        bottomAppBar.replaceMenu(R.menu.home_menu)
                        actualFragment = HOME_FRAGMENT
                        fabHome.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_round_add_24))
                        fabHome.show()
                        bottomAppBar.visibility = View.VISIBLE

                    }
                    R.id.addFragment -> {
                        Log.d(TAG, "El fragmento actual es: Add")
                        actualFragment = ADD_FRAGMENT
                        fabHome.hide()
                        bottomAppBar.visibility = View.GONE
                    }
                    R.id.bluetoothFragment -> {
                        actualFragment = BLUETOOTH_FRAGMENT
                        Log.d(TAG, "El fragmento actual es: Bluetooth")
                        fabHome.hide()
                        bottomAppBar.visibility = View.GONE
                    }
                    R.id.welcomeFragment -> {
                        actualFragment = WELCOME_FRAGMENT
                        Log.d(TAG, "El fragmento actual es: Welcome")
                        fabHome.hide()
                        bottomAppBar.visibility = View.GONE
                    }
                    R.id.donatorFragment -> {
                        actualFragment = DONATOR_FRAGMENT
                        registro = if (arguments == null) Registro() else DonatorFragmentArgs.fromBundle(arguments).registro
                        Log.d(TAG, "El fragmento actual es: Donator")
                        Log.d(TAG, "El registro es $registro")
                        if(lastFragment == HOMEUSER_FRAGMENT) {
                            fabHome.hide()
                            bottomAppBar.visibility = View.VISIBLE
                        }else{
                            fabHome.show()
                            bottomAppBar.visibility = View.VISIBLE
                            fabHome.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_round_edit_24))
                        }
                    }
                    R.id.editFragment -> {
                        actualFragment = EDIT_FRAGMENT
                        Log.d(TAG, "El fragmento actual es: Edit")
                        fabHome.hide()
                        bottomAppBar.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun navigateToAdd() {
        findNavController(R.id.fragment).navigate(R.id.action_homeFragment_to_addFragment)
    }
    private fun navigateToHome(actualFragment: Int) {
        when(actualFragment){
            ADD_FRAGMENT -> {
                findNavController(R.id.fragment).navigate(R.id.action_addFragment_to_homeFragment)
            }
            BLUETOOTH_FRAGMENT -> {
                findNavController(R.id.fragment).navigate(R.id.action_bluetoothFragment_to_homeFragment)
            }
        }
    }
    private fun navigateToBluetooth(actualFragment: Int) {
        when(actualFragment){
            ADD_FRAGMENT -> {
                findNavController(R.id.fragment).navigate(R.id.action_addFragment_to_bluetoothFragment)
                binding.bottomAppBar.visibility = View.GONE
                binding.fabHome.visibility = View.GONE
            }
            HOME_FRAGMENT -> {
                findNavController(R.id.fragment).navigate(R.id.action_homeFragment_to_bluetoothFragment)
                binding.bottomAppBar.visibility = View.GONE
                binding.fabHome.visibility = View.GONE
            }
        }
    }


    private fun logOut(){
        Log.d(TAG, "Cerrando Sesión")
        if(actualFragment == HOME_FRAGMENT){
            findNavController(R.id.fragment).navigate(R.id.action_homeFragment_to_welcomeFragment)
        }else{
            findNavController(R.id.fragment).navigate(R.id.action_homeUserFragment_to_welcomeFragment)
        }
        auth.signOut()
    }
}