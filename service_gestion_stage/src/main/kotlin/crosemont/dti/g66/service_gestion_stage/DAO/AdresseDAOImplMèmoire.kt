package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Adresse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class AdresseDAOImplMèmoire(private val db: JdbcTemplate): AdresseDAO {


    override fun chercherTous(): List<Adresse> = db.query("select * from adresse") { response, _ ->
        Adresse(response.getInt("numeroCivique"), response.getString("rue"), response.getString("ville"), response.getString("codePostal"), response.getString("pays"))
    }

    override fun chercherParCode(code: String): Adresse? = try {
        db.queryForObject(
            "SELECT * FROM adresse WHERE numeroCivique = ?",
            arrayOf(code.toInt())
        ) { rs, _ ->
            Adresse(
                numéroCivique = rs.getInt("numeroCivique"),
                rue = rs.getString("rue"),
                ville = rs.getString("ville"),
                codePostal = rs.getString("codePostal"),
                pays = rs.getString("pays")
            )
        }
    } catch (e: EmptyResultDataAccessException) {
        null
    }


    override fun ajouter(adresse: Adresse): Adresse {
        val sql = "INSERT INTO adresse (numeroCivique, rue, ville, codePostal, pays) VALUES (?, ?, ?, ?, ?)"

        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setInt(1, adresse.numéroCivique)
            ps.setString(2, adresse.rue)
            ps.setString(3, adresse.ville)
            ps.setString(4, adresse.codePostal)
            ps.setString(5, adresse.pays)
            ps
        }, keyHolder)

        adresse.numéroCivique = keyHolder.key?.toInt()!!
        return adresse
    }

    override fun modifier(code: String, adresse: Adresse): Adresse? {
        TODO("Not yet implemented")
    }

    override fun effacer(code: String) {
        TODO("Not yet implemented")
    }

}