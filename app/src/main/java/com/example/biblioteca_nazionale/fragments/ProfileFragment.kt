package com.example.biblioteca_nazionale.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.biblioteca_nazionale.R
import com.example.biblioteca_nazionale.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val email = currentUser?.email

        binding.currentEmail.text = email

        binding.updateButtonFrag.setOnClickListener {
            updateAll()
        }

        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Logout")
            .setMessage("Are you sure you want to disconnect?")
            .setPositiveButton("Confirm") { dialog, which ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun updateAll() {
        val user = auth.currentUser
        val newEmail = binding.editTextTextEmailAddress.text.toString()
        val newPassword = binding.editTextTextPassword.text.toString()

        if (!binding.editTextTextEmailAddress.text.isEmpty() && !binding.editTextTextPassword.text.isEmpty()) {
            user?.updateEmail(newEmail)
                ?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        Toast.makeText(context, "Operazione di cambio email completata con successo!", Toast.LENGTH_SHORT).show()

                        // Dopo aver completato l'aggiornamento dell'email, esegui l'aggiornamento della password
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { passwordTask ->
                                if (passwordTask.isSuccessful) {
                                    Toast.makeText(context, "Operazione di cambio password completata con successo!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Operazione di cambio password non completata!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        //val action = ProfileFragmentDirections.actionProfileInfoFragmentToCredentialUpdated()
                        //findNavController().navigate(action)
                        val navController = Navigation.findNavController(binding.root)
                        navController.navigate(R.id.action_profileInfoFragment_to_credentialUpdated)
                        //Navigation.findNavController(requireView()).navigate(R.id.action_profileInfoFragment_to_credentialUpdated)
                    } else {
                        Toast.makeText(context, "Operazione di cambio credenziali non completata!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else if (!binding.editTextTextEmailAddress.text.isEmpty() && binding.editTextTextPassword.text.isEmpty()) {
            user?.updateEmail(newEmail)
                ?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        Toast.makeText(context, "Operazione di cambio email completata con successo!", Toast.LENGTH_SHORT).show()
                        val action = ProfileFragmentDirections.actionProfileInfoFragmentToCredentialUpdated()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(context, "Operazione di cambio email non completata!", Toast.LENGTH_SHORT).show()
                    }
                }
        } else if (binding.editTextTextEmailAddress.text.isEmpty() && !binding.editTextTextPassword.text.isEmpty()) {
            if (binding.editTextTextPassword.text.toString() == binding.editTextTextPassword2.text.toString()) {
                user?.updatePassword(newPassword)
                    ?.addOnCompleteListener { passwordTask ->
                        if (passwordTask.isSuccessful) {
                            Toast.makeText(context, "Operazione di cambio password completata con successo!", Toast.LENGTH_SHORT).show()
                            val action = ProfileFragmentDirections.actionProfileInfoFragmentToCredentialUpdated()
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(context, "Operazione di cambio password non completata!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Le password non coincidono, riprova", Toast.LENGTH_SHORT).show()
            }
        } else if (binding.editTextTextEmailAddress.text.isEmpty() && binding.editTextTextPassword.text.isEmpty()) {
            Toast.makeText(context, "Non hai inserito nessuna nuova credenziale", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(){
        auth.signOut()
        val navController = Navigation.findNavController(binding.root)
        navController.navigate(R.id.action_profileInfoFragment_to_mainActivity)
    }
}