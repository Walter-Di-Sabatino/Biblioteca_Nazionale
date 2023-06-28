package com.example.biblioteca_nazionale.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca_nazionale.R
import com.example.biblioteca_nazionale.activity.LoginActivity
import com.example.biblioteca_nazionale.adapter.BookListAdapter
import com.example.biblioteca_nazionale.databinding.FragmentBookListBinding
import com.example.biblioteca_nazionale.model.Book
import com.example.biblioteca_nazionale.viewmodel.BooksViewModel
import com.google.firebase.auth.FirebaseAuth

class BookListFragment : Fragment(R.layout.fragment_book_list){

    lateinit var binding: FragmentBookListBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private val model: BooksViewModel = BooksViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookListBinding.bind(view)

        var focusSearchView = arguments?.getBoolean("focusSearchView") ?: false
        if (focusSearchView) {
            binding.searchView.postDelayed({
                binding.searchView.clearFocus()
                binding.searchView.requestFocus()

                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }, 1)
        }

        /*firebaseAuth = FirebaseAuth.getInstance()
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_back -> {
                    firebaseAuth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }*/

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                println(query)
                performBookSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //performBookSearch(newText)
                return true
            }
        })

        Log.d("yolxzxzoddd",binding.searchView.hasFocus().toString())

    }

    private fun performBookSearch(query: String) {
        model.searchBooks(query)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBooks.layoutManager = layoutManager
        val adapter = BookListAdapter(model.getLibriLiveData())

        adapter.setOnBookClickListener(object : BookListAdapter.OnBookClickListener{
            override fun onBookClick(position: Int) {
                val libri = model.getLibriLiveData()
                val libro = libri.value?.items?.get(position)
                if (libro != null) {
                    val action = BookListFragmentDirections.actionBookListFragmentToBookInfoFragment(libro)
                    findNavController().navigate(action)
                } else {
                    // Gestisci il caso in cui il libro è nullo
                }
            }
        })
        binding.recyclerViewBooks.adapter = adapter
    }

}
