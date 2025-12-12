package com.example.meetsv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.meetsv2.db.AppDatabase
import kotlinx.coroutines.launch

class AcessoActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnCadastrar: TextView
    private lateinit var btnEntrar: Button
    private lateinit var emailEditText: EditText
    private lateinit var senhaEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_acesso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.acesso_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVoltar = findViewById(R.id.voltar)
        btnCadastrar = findViewById(R.id.cadastrar)
        btnEntrar = findViewById(R.id.entrar)
        emailEditText = findViewById(R.id.editEmail)
        senhaEditText = findViewById(R.id.editSenha)


        btnVoltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        btnEntrar.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            if (email.isNotBlank() && senha.isNotBlank()) {
                lifecycleScope.launch {
                    val userDao = AppDatabase.getDatabase(applicationContext).userDao()
                    val user = userDao.findByEmailAndSenha(email, senha)

                    if (user != null) {
                        // --- SALVA O ESTADO DE LOGIN E O ID ---
                        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.putInt("userId", user.id) // SALVA O ID DO USUÁRIO
                        editor.apply()
                        // -------------------------------------

                        val intent = Intent(this@AcessoActivity, FeedActivity::class.java)
                        startActivity(intent)
                        finish() // Opcional: fecha a tela de login para que o usuário não volte para ela
                    } else {
                        // Login falhou
                        Toast.makeText(this@AcessoActivity, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
