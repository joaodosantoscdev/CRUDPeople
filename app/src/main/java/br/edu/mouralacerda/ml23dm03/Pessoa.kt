package br.edu.mouralacerda.ml23dm03

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pessoa(
    var nome: String,
    var telephone: String,
    var email: String,
    var zipcode: String,
    var street: String,
    var city: String,
    var district: String,
    var number: String,
    var state: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Serializable {

    override fun toString(): String {
        return "ID: $id / NOME: $nome"
    }

}