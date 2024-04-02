package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Statement

@Repository
class ÉtudiantDAOImplMémoire(val db: JdbcTemplate): ÉtudiantDAO {


    override fun chercherTous(): List<Étudiant> {

        val sql = "SELECT * FROM etudiant"

        val étudiants = db.query(sql) { rs, _ ->
            val profilInformatiqueString = rs.getString("profilInformatique")
            val profilInformatique = ProfilInformatique.valueOf(profilInformatiqueString)

            Étudiant(
                    codeUtilisateur = rs.getString("codeEtudiant"),
                    nomUtilisateur = rs.getString("nomEtudiant"),
                    prénomUtilisateur = rs.getString("prenomEtudiant"),
                    courrielUtilisateur = rs.getString("courrielEtudiant"),
                    téléphoneUtilisateur = rs.getString("telephoneEtudiant"),
                    profilInformatique = profilInformatique,
                    stageIntégration = rs.getBoolean("stageIntegration"),
                    adresseÉtudiant = rs.getString("adresseEtudiant")
            )
        }

        return étudiants
    }
    override fun chercherParCode(code: String): Étudiant? {

        val sql = "SELECT * FROM etudiant WHERE codeEtudiant = ?"
        val args = arrayOf(code)

        return try {
            db.queryForObject(sql, args) { rs, _ ->

                val profilInformatiqueString = rs.getString("profilInformatique")
                val profilInformatique = ProfilInformatique.valueOf(profilInformatiqueString)

                Étudiant(
                        codeUtilisateur = rs.getString("codeEtudiant"),
                        nomUtilisateur = rs.getString("nomEtudiant"),
                        prénomUtilisateur = rs.getString("prenomEtudiant"),
                        courrielUtilisateur = rs.getString("courrielEtudiant"),
                        téléphoneUtilisateur = rs.getString("telephoneEtudiant"),
                        profilInformatique = profilInformatique,
                        stageIntégration = rs.getBoolean("stageIntegration"),
                        adresseÉtudiant = rs.getString("adresseEtudiant")
                )
            }
        } catch (ex: EmptyResultDataAccessException) {
            null
        }

    }

    override fun ajouter(étudiant: Étudiant): Étudiant? {
        val sql =  "INSERT INTO etudiant (codeEtudiant, nomEtudiant, prenomEtudiant, courrielEtudiant, telephoneEtudiant, profilInformatique, stageIntegration, adresseEtudiant) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        val args = arrayOf(
                étudiant.codeUtilisateur,
                étudiant.nomUtilisateur,
                étudiant.prénomUtilisateur,
                étudiant.courrielUtilisateur,
                étudiant.téléphoneUtilisateur,
                étudiant.profilInformatique.toString(),
                étudiant.stageIntégration,
                étudiant.adresseÉtudiant
        )

        val generatedKeyHolder = GeneratedKeyHolder()
        db.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            for ((index, value) in args.withIndex()) {
                ps.setObject(index + 1, value)
            }
            ps
        }, generatedKeyHolder)

        return if (étudiant.codeUtilisateur != null) {
            Étudiant(
                    codeUtilisateur = étudiant.codeUtilisateur,
                    nomUtilisateur = étudiant.nomUtilisateur,
                    prénomUtilisateur = étudiant.prénomUtilisateur,
                    courrielUtilisateur = étudiant.courrielUtilisateur,
                    téléphoneUtilisateur = étudiant.téléphoneUtilisateur,
                    profilInformatique =  étudiant.profilInformatique,
                    stageIntégration = étudiant.stageIntégration,
                    adresseÉtudiant = étudiant.adresseÉtudiant
            )
        } else {
            null
        }
    }

    override fun modifier(code: String, étudiant: Étudiant): Étudiant? {
        val sql = "UPDATE etudiant SET nomEtudiant = ?, prenomEtudiant = ?, courrielEtudiant = ?, telephoneEtudiant = ?, profilInformatique = ?, stageIntegration = ?, adresseEtudiant = ? WHERE codeEtudiant = ?"
        val args = arrayOf(
                étudiant.nomUtilisateur,
                étudiant.prénomUtilisateur,
                étudiant.courrielUtilisateur,
                étudiant.téléphoneUtilisateur,
                étudiant.profilInformatique.toString(),
                étudiant.stageIntégration,
                étudiant.adresseÉtudiant,
                code
        )

        try {
            db.update(sql, *args)
            return if (code.isNotEmpty()) {
                étudiant
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun effacer(code: String) {
        val sql = "DELETE FROM etudiant WHERE codeEtudiant = ?"

        val codeUtilisateur = code.toIntOrNull()

        if (codeUtilisateur != null) {
            val rangéSupprimée = db.update(sql) { preparedStatement ->
                preparedStatement.setInt(1, codeUtilisateur)
            }

            if (rangéSupprimée > 0) {
                println("Étudiant avec le codeUtilisateur $codeUtilisateur supprimé avec succès.")
            } else {
                println("Aucun étudiant trouvé avec le codeUtilisateur $codeUtilisateur.")
            }
        } else {
            println("Format de code invalide. Veuillez fournir un codeUtilisateur valide.")
        }
    }

    override fun obtenirÉtudiantParCandidature(codeCandidature: String): Étudiant? {
        TODO("Not yet implemented")
    }

    override fun obtenirDocumentsParÉtudiant(code: String): List<Document> = SourceBidon.documents.filter { it.étudiant.codeUtilisateur == code }
    override fun obtenirProfilParÉtudiant(code: String): ProfilInformatique? {
        val sql = "SELECT profilInformatique FROM etudiant WHERE codeEtudiant = ?"
        val args = arrayOf(code)

        return db.queryForObject(sql, args) { rs, _ ->
            ProfilInformatique.valueOf(rs.getString("profilInformatique"))
        }
    }
}