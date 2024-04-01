package br.edu.mouralacerda.ml23dm03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class CadastroNomes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_nomes)

        val botaoSalvar = findViewById<Button>(R.id.btnSalvar)
        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtTel = findViewById<EditText>(R.id.edtTel)

        val edtZipCode = findViewById<EditText>(R.id.edtCep)
        val edtStreet = findViewById<EditText>(R.id.edtStreet)
        val edtNumber = findViewById<EditText>(R.id.edtNumber)
        val edtDistrict = findViewById<EditText>(R.id.edtDistrict)
        val edtCity = findViewById<EditText>(R.id.edtCity)
        val edtState = findViewById<EditText>(R.id.edtState)

        var pessoa: Pessoa? = null;

        edtZipCode.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Chama a função para pesquisar o CEP quando o foco é perdido
                val cep = edtZipCode.text.toString()
                if (cep.isNotEmpty()) {
                    pesquisarCEP(cep)
                }
            }
        }

        val pacote = intent.getBundleExtra("pacote")
        if (pacote != null) {
            val pessoaId = pacote.getInt("pessoaId")
            pessoa = Database.getInstance(this)!!.pessoaDAO().pegarPorId(pessoaId)

            if (pessoa != null && pessoa.id > 0) {
                edtNome.setText(pessoa.nome)
                edtEmail.setText(pessoa.email)
                edtTel.setText(pessoa.telephone)
                edtZipCode.setText(pessoa.zipcode)
                edtStreet.setText(pessoa.street)
                edtNumber.setText(pessoa.number)
                edtDistrict.setText(pessoa.district)
                edtCity.setText(pessoa.city)
                edtState.setText(pessoa.state)
            }
        }

        botaoSalvar.setOnClickListener {
            if (pessoa != null && pessoa.id > 0) {
                pessoa.nome = edtNome.text.toString()
                pessoa.email = edtEmail.text.toString()
                pessoa.telephone = edtTel.text.toString()
                pessoa.zipcode = edtZipCode.text.toString()
                pessoa.street = edtStreet.text.toString()
                pessoa.number = edtNumber.text.toString()
                pessoa.district = edtDistrict.text.toString()
                pessoa.city = edtCity.text.toString()
                pessoa.state = edtState.text.toString()

                Database.getInstance(this)!!.pessoaDAO().atualizar(pessoa)

                finish()
            } else {
                val p = Pessoa(
                    edtNome.text.toString(),
                    edtTel.text.toString(),
                    edtEmail.text.toString(),
                    edtZipCode.text.toString(),
                    edtStreet.text.toString(),
                    edtCity.text.toString(),
                    edtDistrict.text.toString(),
                    edtNumber.text.toString(),
                    edtState.text.toString()
                )

                Database.getInstance(this)!!.pessoaDAO().salvar(p)

                finish()
            }
        }

    }

    private fun pesquisarCEP(cep: String) {
        val url = "https://viacep.com.br/ws/$cep/json/"

        CoroutineScope(Dispatchers.IO).launch {

            val resp = URL(url).readText()

            withContext(Dispatchers.Main) {
                preencherEndereco(resp)
            }
        }
    }

    fun preencherEndereco(resp: String) {
        val addressJSON = JSONObject(resp)

        findViewById<EditText>(R.id.edtCep).setText(addressJSON.getString("cep"))
        findViewById<EditText>(R.id.edtStreet).setText(addressJSON.getString("logradouro"))
        findViewById<EditText>(R.id.edtDistrict).setText(addressJSON.getString("bairro"))
        findViewById<EditText>(R.id.edtCity).setText(addressJSON.getString("localidade"))
        findViewById<EditText>(R.id.edtState).setText(addressJSON.getString("uf"))
    }
}