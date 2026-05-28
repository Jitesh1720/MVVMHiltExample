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

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_USER", User::class.java) // 1
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_USER") as? User // 2
        }

        user?.let { populateDetails(it) } ?: finish()
    }

    private fun populateDetails(user: User) {
        binding.apply {
            txtDetailName.text = user.name
            txtDetailId.text = "ID: #${user.id} (username: ${user.username})"
            txtDetailEmail.text = user.email
            txtDetailPhone.text = user.phone
            txtDetailWebsite.text = user.website
            txtDetailAddress.text = "${user.address.suite}, ${user.address.street}, ${user.address.city}, ${user.address.zipcode}"
            txtDetailCompany.text = "${user.company.name}\n\"${user.company.catchPhrase}\""
            imgDetailAvatar.loadCircular(user.avatarUrl)
        }
    }
}
