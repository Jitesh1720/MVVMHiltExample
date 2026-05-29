package com.example.demokotlinapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demokotlinapp.databinding.ActivityMainBinding
import com.example.demokotlinapp.extensions.gone
import com.example.demokotlinapp.extensions.visible
import com.example.demokotlinapp.ui.viewmodel.UserUiState
import com.example.demokotlinapp.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val userViewModel: UserViewModel by viewModels()
    private val searchQuery = MutableStateFlow("")

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

    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery.value = s?.toString().orEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        lifecycleScope.launch {
            searchQuery
                .debounce(300) // 300ms debounce
                .collect { query ->
                    userAdapter.filter(query)
                }
        }
    }

    private fun observeViewModel() {
        userViewModel.uiState.observe(this@MainActivity) { state ->
            when (state) {
                is UserUiState.Loading -> {
                    binding.progressBar.visible()
                }
                is UserUiState.Success -> {
                    binding.progressBar.gone()
                    userAdapter.submitList(state.users)
                }
                is UserUiState.Error -> {
                    binding.progressBar.gone()
                    Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}