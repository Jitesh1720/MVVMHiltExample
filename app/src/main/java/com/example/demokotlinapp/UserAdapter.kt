package com.example.demokotlinapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.demokotlinapp.databinding.ItemUserBinding
import com.example.demokotlinapp.model.User
import java.util.Locale
import com.example.demokotlinapp.extensions.loadCircular

class UserAdapter(
    private val onItemClick: (User) -> Unit,
    private val onFilterChanged: (Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var originalList: List<User> = emptyList()
    private var filteredList: List<User> = emptyList()

    fun submitList(list: List<User>) {
        originalList = list
        filteredList = list
        onFilterChanged(filteredList.size)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val cleanQuery = query.trim().lowercase(Locale.getDefault())
        filteredList = if (cleanQuery.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.name.lowercase(Locale.getDefault()).contains(cleanQuery) ||
                it.email.lowercase(Locale.getDefault()).contains(cleanQuery)
            }
        }
        onFilterChanged(filteredList.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredList[position]
        holder.bind(user)
        
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            android.R.anim.fade_in
        )
    }

    override fun getItemCount(): Int = filteredList.size

    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                txtUserName.text = user.name
                txtUserEmail.text = user.email
                imgAvatar.loadCircular(user.avatarUrl)
                root.setOnClickListener { onItemClick(user) }
            }
        }
    }
}
