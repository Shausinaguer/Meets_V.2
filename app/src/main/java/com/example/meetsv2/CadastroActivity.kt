package com.example.meetsv2

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
import com.example.meetsv2.db.User
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAcessar: TextView
    private lateinit var editNome: EditText
    private lateinit var editEmail: EditText
    private lateinit var editSenha: EditText
    private lateinit var btnCadastrar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastro_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa o banco de dados
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // Referências dos componentes de UI
        btnVoltar = findViewById(R.id.voltar)
        btnAcessar = findViewById(R.id.acesse_perfil)
        editNome = findViewById(R.id.editNome)
        editEmail = findViewById(R.id.editEmail)
        editSenha = findViewById(R.id.editSenha)
        btnCadastrar = findViewById(R.id.entrar)


        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString()
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = User(nome = nome, email = email, senha = senha)
                    userDao.insert(user)

                    // Mostra mensagem de sucesso e vai para a tela de login
                    Toast.makeText(this@CadastroActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CadastroActivity, AcessoActivity::class.java)
                    startActivity(intent)
                    finish() // Fecha a tela de cadastro
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }


        btnVoltar.setOnClickListener {
            finish() // Ação mais comum para um botão voltar
        }

        btnAcessar.setOnClickListener {
            val intent = Intent(this, AcessoActivity::class.java)
            startActivity(intent)
        }

    }
}
