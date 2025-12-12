package com.example.meetsv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetsv2.db.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FeedActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnComentario: ImageButton
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var searchEditText: EditText

    private val searchQuery = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.feed_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Obter DAO e ID do Usuário Logado ---
        val postDao = AppDatabase.getDatabase(this).postDao()
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentUserId = sharedPreferences.getInt("userId", -1) // -1 indica que o usuário não foi encontrado

        // --- Configuração dos Botões e da Busca ---
        btnVoltar = findViewById(R.id.voltar)
        btnComentario = findViewById(R.id.addButton)
        searchEditText = findViewById(R.id.searchEditText)

        btnVoltar.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear() // Limpa todos os dados de login
            editor.apply()

            val intent = Intent(this, AcessoActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnComentario.setOnClickListener {
            val intent = Intent(this, ComentarioActivity::class.java)
            startActivity(intent)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery.value = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // --- Configuração do RecyclerView e do Adapter com Lógica de Deleção ---
        postAdapter = PostAdapter(currentUserId) { postToDelete ->
            lifecycleScope.launch {
                postDao.delete(postToDelete)
                // A mágica do Flow fará o resto, atualizando a UI!
            }
        }
        postsRecyclerView = findViewById(R.id.postsRecyclerView)
        postsRecyclerView.adapter = postAdapter
        postsRecyclerView.layoutManager = LinearLayoutManager(this)

        // --- Observação Dinâmica de Posts (com Busca) ---
        lifecycleScope.launch {
            searchQuery
                .debounce(300) // Um pequeno atraso para evitar buscas a cada tecla
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        postDao.getPostsWithAuthors()
                    } else {
                        postDao.searchPostsWithAuthors(query)
                    }
                }
                .collect { posts ->
                    postAdapter.submitList(posts)
                }
        }
    }
}
