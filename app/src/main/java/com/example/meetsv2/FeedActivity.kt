package com.example.meetsv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.meetsv2.db.AppDatabase
import kotlinx.coroutines.launch

class FeedActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnComentario: ImageButton
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feed_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Configuração dos Botões (existente) ---
        btnVoltar = findViewById(R.id.voltar)
        btnComentario = findViewById(R.id.addButton)

        btnVoltar.setOnClickListener {
            val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val intent = Intent(this, AcessoActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnComentario.setOnClickListener {
            val intent = Intent(this, ComentarioActivity::class.java)
            startActivity(intent)
        }
        
        // --- Configuração do RecyclerView e do Adapter ---
        postAdapter = PostAdapter()
        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        postsRecyclerView.adapter = postAdapter

        // --- Observar os Posts do Banco de Dados ---
        val postDao = AppDatabase.getDatabase(this).postDao()
        lifecycleScope.launch {
            postDao.getAllPosts().collect { posts ->
                postAdapter.submitList(posts)
            }
        }
    }
}
