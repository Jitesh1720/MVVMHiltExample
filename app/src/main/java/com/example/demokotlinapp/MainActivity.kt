package com.example.demokotlinapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demokotlinapp.databinding.ActivityMainBinding
import com.example.demokotlinapp.extensions.gone
import com.example.demokotlinapp.extensions.visible
import com.example.demokotlinapp.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        setupRecyclerView()
        setupSearch()
        observeViewModel()
        
        // Fetch users through ViewModel
        userViewModel.fetchUsers()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            onItemClick = { user ->
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("EXTRA_USER", user)
                }
                startActivity(intent)
            },
            onFilterChanged = { count ->
                binding.txtEmptyState.apply {
                    if (count == 0) visible() else gone()
                }
            }
        )

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }

    private fun setupSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userAdapter.filter(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun observeViewModel() {
        userViewModel.apply {
            users.observe(this@MainActivity) { list ->
                userAdapter.submitList(list)
            }

            isLoading.observe(this@MainActivity) { loading ->
                binding.progressBar.apply {
                    if (loading) visible() else gone()
                }
            }

            errorMessage.observe(this@MainActivity) { error ->
                error?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}