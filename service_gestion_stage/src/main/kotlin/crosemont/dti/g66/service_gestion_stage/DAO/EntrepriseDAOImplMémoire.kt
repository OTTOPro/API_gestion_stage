package crosemont.dti.g66.service_gestion_stage.DAO


import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class EntrepriseDAOImplMémoire(val db: JdbcTemplate ): EntrepriseDAO {

    override fun chercherTous(): List<Entreprise> = db.query("select * from entreprise") { response, _ ->
        val codeEntreprise = response.getInt("codeEntreprise")
        val nomEntreprise = response.getString("nomEntreprise")
        val adresse = response.getString("adresse")

        Entreprise(
                codeEntreprise = codeEntreprise,
                nomEntreprise = nomEntreprise,
                adresseEntreprise = adresse,
        )
    }

    override fun chercherParCode(code: String): Entreprise? {
        val sql = "SELECT * FROM entreprise WHERE codeEntreprise = ?"
        val entreprises = db.query(sql, arrayOf(code.toInt())) { rs, _ ->
            val codeEntreprise = rs.getInt("codeEntreprise")
            val nomEntreprise = rs.getString("nomEntreprise")
            val adresse = rs.getString("adresse")

            Entreprise(
                    codeEntreprise = codeEntreprise,
                    nomEntreprise = nomEntreprise,
                    adresseEntreprise = adresse,
            )
        }

        if (entreprises.isEmpty()) {
            throw RessourceInexistanteException("Aucune entreprise trouvée avec le code : $code")
        }

        return entreprises.firstOrNull()
    }

    override fun chercherParEmployeur(code: String): Entreprise? {
        val sql = "SELECT e.* FROM entreprise e " +
                "INNER JOIN employeur emp ON e.codeEntreprise = emp.entreprise_codeEntreprise " +
                "WHERE emp.codeUtilisateur = ?"

        val entreprises = db.query(sql, code) { response, _ ->
            val codeEntreprise = response.getInt("codeEntreprise")
            val nomEntreprise = response.getString("nomEntreprise")
            val adresse = response.getString("adresse")

            Entreprise(
                codeEntreprise = codeEntreprise,
                nomEntreprise = nomEntreprise,
                adresseEntreprise = adresse
            )
        }

        if (entreprises.isEmpty()) {
            throw RessourceInexistanteException("Aucune entreprise trouvée pour l'employeur avec le code : $code")
        }

        return entreprises.firstOrNull()
    }

    override fun ajouter(entreprise: Entreprise): Entreprise {

        if (entrepriseExisteParNom(entreprise.nomEntreprise)) {
            throw ConflitAvecUneRessourceExistanteException("L'entreprise ${entreprise.nomEntreprise} existe déjà.")
        }

        val sql = "INSERT INTO entreprise (nomEntreprise, adresse) VALUES (?, ?)"
        val keyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, entreprise.nomEntreprise)
            ps.setString(2, entreprise.adresseEntreprise)
            ps
        }, keyHolder)

        entreprise.codeEntreprise = keyHolder.key?.toInt()!!
        return entreprise
    }

    private fun entrepriseExisteParNom(nomEntreprise: String): Boolean {
        val sql = "SELECT COUNT(*) FROM entreprise WHERE nomEntreprise = ?"
        return db.queryForObject(sql, Int::class.java, nomEntreprise) > 0
    }

    override fun modifier(code: String, entreprise: Entreprise): Entreprise? {
        val existingEntreprise = chercherParCode(code)

        if (existingEntreprise != null) {
            val sql = "UPDATE entreprise SET nomEntreprise = ?, adresse = ? WHERE codeEntreprise = ?"
            db.update(sql, entreprise.nomEntreprise, entreprise.adresseEntreprise, code.toInt())
            return null
        } else {
            return ajouter(entreprise)
        }
    }

    override fun effacer(code: String) {
        val existingEntreprise = chercherParCode(code)

        if (existingEntreprise != null) {
            val sql = "DELETE FROM entreprise WHERE codeEntreprise = ?"
            db.update(sql, code.toInt())
        } else {
            throw RessourceInexistanteException("L'entreprise avec le code : $code n'est pas inscrite au service.")
        }
    }

}