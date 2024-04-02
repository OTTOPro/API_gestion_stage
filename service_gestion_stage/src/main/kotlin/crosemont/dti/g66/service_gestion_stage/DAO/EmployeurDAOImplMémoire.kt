package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur
import org.springframework.context.annotation.Lazy
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Statement

@Repository
class EmployeurDAOImplMémoire(val db: JdbcTemplate,  val daoEntreprise: EntrepriseDAOImplMémoire): EmployeurDAO {

    override fun chercherTous(): List<Employeur> {
        val sql = "SELECT * FROM employeur"
        return db.query(sql) { rs, _ ->
            val codeUtilisateur = rs.getString("codeUtilisateur")
            val nomUtilisateur = rs.getString("nomUtilisateur")
            val prénomUtilisateur = rs.getString("prénomUtilisateur")
            val courrielUtilisateur = rs.getString("courrielUtilisateur")
            val téléphoneUtilisateur = rs.getString("téléphoneUtilisateur")
            val entrepriseCode = rs.getInt("entreprise_codeEntreprise")

            val entreprise = daoEntreprise.chercherParCode(entrepriseCode.toString())

            Employeur(
                codeUtilisateur = codeUtilisateur,
                nomUtilisateur = nomUtilisateur,
                prénomUtilisateur = prénomUtilisateur,
                courrielUtilisateur = courrielUtilisateur,
                téléphoneUtilisateur = téléphoneUtilisateur,
                codeEntreprise = entreprise
            )
        }
    }
    override fun chercherParCode(code: String): Employeur? {
        val sql = "SELECT * FROM employeur WHERE codeUtilisateur = ?"
        return try {
            db.queryForObject(sql, arrayOf(code)) { rs, _ ->
                val codeUtilisateur = rs.getString("codeUtilisateur")
                val nomUtilisateur = rs.getString("nomUtilisateur")
                val prénomUtilisateur = rs.getString("prénomUtilisateur")
                val courrielUtilisateur = rs.getString("courrielUtilisateur")
                val téléphoneUtilisateur = rs.getString("téléphoneUtilisateur")
                val entrepriseCode = rs.getInt("entreprise_codeEntreprise")

                val entreprise = daoEntreprise.chercherParCode(entrepriseCode.toString())

                Employeur(
                    codeUtilisateur = codeUtilisateur,
                    nomUtilisateur = nomUtilisateur,
                    prénomUtilisateur = prénomUtilisateur,
                    courrielUtilisateur = courrielUtilisateur,
                    téléphoneUtilisateur = téléphoneUtilisateur,
                    codeEntreprise = entreprise,
                )
            }
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    @Transactional
    override fun ajouter(employeur: Employeur): Employeur? {
        if (employeurExisteParEmail(employeur.courrielUtilisateur)) {
            throw ConflitAvecUneRessourceExistanteException("Un employeur avec le même e-mail existe déjà.")
        }

        employeur.codeEntreprise?.let { daoEntreprise.ajouter(it) }

        val sql = "INSERT INTO employeur (codeUtilisateur, nomUtilisateur, prénomUtilisateur, courrielUtilisateur, téléphoneUtilisateur, entreprise_codeEntreprise) VALUES (?, ?, ?, ?, ?, ?)"
        val args = arrayOf(
            employeur.codeUtilisateur,
            employeur.nomUtilisateur,
            employeur.prénomUtilisateur,
            employeur.courrielUtilisateur,
            employeur.téléphoneUtilisateur,
            employeur.codeEntreprise?.codeEntreprise
        )

        db.update(sql, *args)

        return Employeur(
            codeUtilisateur = employeur.codeUtilisateur,
            nomUtilisateur = employeur.nomUtilisateur,
            prénomUtilisateur = employeur.prénomUtilisateur,
            courrielUtilisateur = employeur.courrielUtilisateur,
            téléphoneUtilisateur = employeur.téléphoneUtilisateur,
            codeEntreprise = employeur.codeEntreprise
        )
    }

    private fun employeurExisteParEmail(email: String): Boolean {
        val sql = "SELECT COUNT(*) FROM employeur WHERE courrielUtilisateur = ?"
        return db.queryForObject(sql, Int::class.java, email) > 0
    }
    override fun modifier(code: String, employeur: Employeur): Employeur? {
        val existingEmployeur = chercherParCode(code)

        if (existingEmployeur != null) {
            val sql = "UPDATE employeur SET nomUtilisateur = ?, prénomUtilisateur = ?, " +
                    "courrielUtilisateur = ?, téléphoneUtilisateur = ?, entreprise_codeEntreprise = ? " +
                    "WHERE codeUtilisateur = ?"

            val args = arrayOf(
                employeur.nomUtilisateur,
                employeur.prénomUtilisateur,
                employeur.courrielUtilisateur,
                employeur.téléphoneUtilisateur,
                employeur.codeEntreprise?.codeEntreprise,
                code
            )

            db.update(sql, args)

            return employeur
        } else {
            throw RessourceInexistanteException("L'employeur avec le code ${employeur.codeUtilisateur} n'est pas inscrit au service")
        }
    }
    override fun effacer(code: String) {
        val existingEmployeur = chercherParCode(code)

        if (existingEmployeur != null) {
            val sql = "DELETE FROM employeur WHERE codeUtilisateur = ?"
            db.update(sql, code.toInt())
        } else {
            throw RessourceInexistanteException("L'employeur avec le code : $code n'est pas inscrit au service.")
        }
    }
}