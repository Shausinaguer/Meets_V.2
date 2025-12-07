package com.example.meetsv2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.meetsv2.db.AppDatabase
import com.example.meetsv2.db.Post
import kotlinx.coroutines.launch

class ComentarioActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var editComentario: EditText
    private lateinit var btnPublicar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comentario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comentario_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVoltar = findViewById(R.id.voltar)
        editComentario = findViewById(R.id.editcomentario)
        btnPublicar = findViewById(R.id.postar)

        val postDao = AppDatabase.getDatabase(this).postDao()

        btnVoltar.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish() // Fechar para não empilhar activities
        }

        btnPublicar.setOnClickListener {
            val content = editComentario.text.toString()

            if (content.isNotBlank()) {
                lifecycleScope.launch {
                    val newPost = Post(content = content)
                    postDao.insert(newPost)
                    
                    // Voltar para o feed após publicar
                    runOnUiThread {
                        Toast.makeText(this@ComentarioActivity, "Publicado!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Escreva algo para publicar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
