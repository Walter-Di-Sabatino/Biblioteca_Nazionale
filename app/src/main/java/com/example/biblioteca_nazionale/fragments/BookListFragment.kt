package com.example.biblioteca_nazionale.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca_nazionale.R
import com.example.biblioteca_nazionale.adapter.BookListAdapter
import com.example.biblioteca_nazionale.databinding.FragmentBookListBinding
import com.example.biblioteca_nazionale.viewmodel.BooksViewModel

class BookListFragment : Fragment(R.layout.fragment_book_list){

    lateinit var binding: FragmentBookListBinding
    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_info, container, false)
    }*/
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookListBinding.bind(view)

        //val model: BookRepository = BookRepository()
        //val model: BooksViewModel = ViewModelProvider(this).get(BooksViewModel::class.java)

        /*model.searchBooks("Harry potter e il prigioniero di azkaban").observe(viewLifecycleOwner, { booksResponse ->
            val adapter = BookListAdapter(booksResponse)
            binding.recyclerViewBooks.adapter = adapter
        })*/
        val model: BooksViewModel = BooksViewModel()

        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBooks.adapter = BookListAdapter(model.searchBooks("Harry potter e il prigioniero di azkaban"))
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookListBinding.bind(view)

        val model: BooksViewModel = BooksViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBooks.layoutManager = layoutManager
        val adapter = BookListAdapter(model.getLibriLiveData())

        adapter.setOnBookClickListener(object : BookListAdapter.OnBookClickListener{
            override fun onBookClick(position: Int) {
                /*val book:Book = model.getBooks()[position]
                val bookInfoFrag = BookInfoFragment()
                val bundle = Bundle()
                bundle.putSerializable("selectedBook", book);                bookInfoFrag.arguments = bundle
                val fragmentManager: FragmentManager = parentFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainer, bookInfoFrag)
                fragmentTransaction.commit()*/
            }
        })
        binding.recyclerViewBooks.adapter = adapter

        model.searchBooks("Harry Potter e il prigioniero di Azkaban")
    }

}
