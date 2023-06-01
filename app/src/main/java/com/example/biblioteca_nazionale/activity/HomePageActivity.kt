package com.example.biblioteca_nazionale.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.biblioteca_nazionale.R
import com.example.biblioteca_nazionale.adapter.BookListAdapter
import com.example.biblioteca_nazionale.databinding.HomePageBinding
import com.example.biblioteca_nazionale.fragments.BookListFragment
import com.example.biblioteca_nazionale.fragments.ProfileFragment
import com.example.biblioteca_nazionale.viewmodel.BooksViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomePageActivity : AppCompatActivity() {

    lateinit var binding: HomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var model:BooksViewModel = BooksViewModel()
    lateinit var adapter:BookListAdapter
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        binding = HomePageBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        /*binding.bottomNavigation.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.profileIcon -> {
                    findNavController(R.id.fragmentContainer).navigate(R.id.action_bookListFragment_to_profileFragment2)
                    true
                }
                else -> false
            }
        }*/

        //      PROVA DATABASE FIREBASE

        val db = Firebase.firestore
        val user1 = hashMapOf(
            "lucabellante@gmail.com" to "email",
            "luca1234" to "password",
        )

        db.collection("utenti").document("datiUtenti")
            .set(user1)
            .addOnSuccessListener { Log.d("/HomePageActivity", "DocumentSnapshot successfully written!") }
            .addOnFailureListener {Log.d("/HomePageActivity", "Error writing document") }



        // Leggo il documento
        val docRef = db.collection("utenti").document("datiUtenti")
        docRef.get()

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "ECCO I DATI: ${document.data}")
                } else {
                    Log.d(TAG, "Nessun dato")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "ERROREEEEEEEE ", exception)
            }

//      FINE PROVA DATABASE FIREBASE

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileIcon -> replaceFragment(ProfileFragment())
                R.id.homeIcon -> {

                }
                R.id.notificationIcon -> {


                }
                R.id.bookIcon -> {

                }
                R.id.settingsIcon -> {

                }
                else -> {}
            }

            true
        }


    }

    /*override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }*/

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,  fragment)
        fragmentTransaction.commit()
    }




}