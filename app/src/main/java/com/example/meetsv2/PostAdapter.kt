package com.example.meetsv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meetsv2.db.Post
import com.example.meetsv2.db.PostWithAuthor

class PostAdapter(
    private val currentUserId: Int,
    private val onDeleteClicked: (Post) -> Unit
) : ListAdapter<PostWithAuthor, PostAdapter.PostViewHolder>(PostWithAuthorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postWithAuthor = getItem(position)
        holder.bind(postWithAuthor, currentUserId, onDeleteClicked)
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postAuthorTextView: TextView = itemView.findViewById(R.id.postAuthor)
        private val postContentTextView: TextView = itemView.findViewById(R.id.postContent)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(postWithAuthor: PostWithAuthor, currentUserId: Int, onDeleteClicked: (Post) -> Unit) {
            postAuthorTextView.text = postWithAuthor.user.nome
            postContentTextView.text = postWithAuthor.post.content

            if (postWithAuthor.post.userId == currentUserId) {
                deleteButton.visibility = View.VISIBLE
                deleteButton.setOnClickListener {
                    onDeleteClicked(postWithAuthor.post)
                }
            } else {
                deleteButton.visibility = View.GONE
            }
        }
    }
}

class PostWithAuthorDiffCallback : DiffUtil.ItemCallback<PostWithAuthor>() {
    override fun areItemsTheSame(oldItem: PostWithAuthor, newItem: PostWithAuthor): Boolean {
        return oldItem.post.id == newItem.post.id
    }

    override fun areContentsTheSame(oldItem: PostWithAuthor, newItem: PostWithAuthor): Boolean {
        return oldItem == newItem
    }
}
