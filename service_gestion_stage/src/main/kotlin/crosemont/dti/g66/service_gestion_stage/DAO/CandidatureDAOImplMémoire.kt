package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.DroitAccèsInsuffisantException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement

@Repository
class  CandidatureDAOImplMémoire(val db: JdbcTemplate,val offreDAO: OffreDAOImplMémoire, val étudiantDAO: ÉtudiantDAOImplMémoire) : CandidatureDAO {

    override fun chercherTous(): List<Candidature> {
        val sql = "SELECT * FROM candidature"
        return db.query(sql) { rs, _ ->
            mapCandidature(rs)
        }
    }

    override fun chercherParCode(code: String): Candidature? {
        val sql = "SELECT * FROM candidature WHERE codeCandidature = ?"

        val result = db.query(sql, arrayOf(code)) { rs, _ ->
            mapCandidature(rs)
        }

//        if (result.isEmpty()) {
//            throw RessourceInexistanteException("Aucune candidature trouvée avec le code : $code")
//        }

        return result.firstOrNull()
    }

    override fun chercherParOffre(code: String): List<Candidature>? {
        val sql = "SELECT * FROM candidature WHERE offre_idOffre = ?"

        val result = db.query(sql, arrayOf(code.toInt())) { rs, _ ->
            mapCandidature(rs)
        }


        return result
    }

    override fun postulerOffre(code_étudiant: String, code_offre: String, candidature: Candidature): Candidature? {
        val offre = offreDAO.chercherParCode(code_offre)
            ?: throw RessourceInexistanteException("L'offre n'existe pas dans le service.")
        val étudiant = étudiantDAO.chercherParCode(code_étudiant)
            ?: throw DroitAccèsInsuffisantException("L'étudiant n'existe pas dans le service.")

        val sqlCandidature = "INSERT INTO candidature (offre_idOffre, etudiant_codeEtudiant, étatCandidature) VALUES (?, ?, ?)"
        val argsCandidature = arrayOf(
            offre.idOffre,
            étudiant.codeUtilisateur,
            candidature.étatCandidature.name
        )
        val candidatureRetourné = Candidature(candidature.codeCandidature, offre, étudiant)

        db.update({ connection ->
            val ps = connection.prepareStatement(sqlCandidature, Statement.RETURN_GENERATED_KEYS)
            for ((index, value) in argsCandidature.withIndex()) {
                ps.setObject(index + 1, value)
            }
            ps
        })

        return candidatureRetourné
    }


    override fun modifier(codeCandidature: String, candidature: Candidature): Candidature? {

        val candidatureExistante = chercherParCode(codeCandidature)

        if (candidatureExistante == null) {
            throw RessourceInexistanteException("La candidature avec le code $codeCandidature n'a pas été trouvée.")
        }

        val sql = "UPDATE candidature SET étatCandidature = ? WHERE codeCandidature = ?"
        val args = arrayOf(
            candidature.étatCandidature.name,
            codeCandidature.toInt()
        )

        db.update(sql, *args)

        return candidature
    }

    override fun effacer(id: String) {
        val sql = "DELETE FROM candidature WHERE codeCandidature = ?"
        val candidatureExistante = db.update(sql, *arrayOf(id.toInt()))

        if (candidatureExistante == 0) {
            throw RessourceInexistanteException("La candidature avec l'id '$id' n'existe pas.")
        }
    }

    private fun mapCandidature(rs: ResultSet): Candidature {
        return Candidature(
            codeCandidature = rs.getInt("codeCandidature"),
            offre = offreDAO.chercherParCode(rs.getInt("offre_idOffre").toString()),
            étudiant = étudiantDAO.chercherParCode(rs.getString("etudiant_codeEtudiant")),
            étatCandidature = ÉtatCandidature.valueOf(rs.getString("étatCandidature"))
        )
    }

    override fun obtenirCandidaturesParÉtudiant(code: String): List<Candidature> {

        val sql = "SELECT * FROM candidature WHERE etudiant_codeEtudiant = ?"

        val result = db.query(sql, arrayOf(code)) { rs, _ ->
            mapCandidature(rs)
        }

        if (result.isEmpty()) {
            throw RessourceInexistanteException("Aucune candidature trouvée pour l'étudiant avec le code : $code")
        }

        return result
    }

    override fun ajouter(candidature: Candidature): Candidature? {
        TODO("Not yet implemented")
    }

    override fun modifierÉtatOffre(code: String, candidature: Candidature): Candidature? {
        TODO("Not yet implemented")
    }

}