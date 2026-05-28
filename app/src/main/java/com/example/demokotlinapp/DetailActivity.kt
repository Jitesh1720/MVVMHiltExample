package com.example.demokotlinapp

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demokotlinapp.databinding.ActivityDetailBinding
import com.example.demokotlinapp.extensions.loadCircular
import com.example.demokotlinapp.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        // Safe retrieve user extra and bind details using scope functions
        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_USER", User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_USER") as? User
        }

        user?.let { populateDetails(it) } ?: finish()
    }

    private fun populateDetails(user: User) {
        binding.apply {
            txtDetailName.text = user.fullName
            txtDetailId.text = "#${user.id}"
            txtDetailEmail.text = user.email
            txtDetailFirstName.text = user.firstName
            txtDetailLastName.text = user.lastName
            imgDetailAvatar.loadCircular(user.avatar)
        }
    }
}
