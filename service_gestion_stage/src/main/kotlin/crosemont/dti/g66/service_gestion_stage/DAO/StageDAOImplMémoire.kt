package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.CandidatureNonAcceptéeException
import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.DroitAccèsInsuffisantException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ModeEmploi
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.StageProgression
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import crosemont.dti.g66.service_gestion_stage.Modèle.Stage
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class StageDAOImplMémoire(val db: JdbcTemplate, val etudiantDAO: ÉtudiantDAOImplMémoire, val candidatureDAO: CandidatureDAOImplMémoire, val daoEmployeur: EmployeurDAOImplMémoire): StageDAO{
    override fun chercherTous(): List<Stage> = db.query("SELECT * FROM stage") { rs, _ ->
        Stage(
            idStage = rs.getInt("idStage"),
            stagiaire = candidatureDAO.chercherParCode(rs.getInt("candidature_codeCandidature").toString())!!.étudiant,
            candidatureAcceptée = candidatureDAO.chercherParCode(rs.getInt("candidature_codeCandidature").toString()),
            superviseurAssigné = rs.getString("superviseur_assigne"),
            employeur = daoEmployeur.chercherParCode(rs.getString("employeur_codeEmployeur")),
            stageProgression = StageProgression.valueOf(rs.getString("stage_progression")),
            lieu = rs.getString("lieu"),
            dateDébut = rs.getString("date_debut"),
            dateFin = rs.getString("date_fin")

        )
    }
    override fun chercherParCode(code: String): Stage? = SourceBidon.stages.find { it.idStage == code.toInt() }
    override fun ajouter(stage: Stage): Stage? {
        TODO("Not yet implemented")
    }

    override fun modifier(code: String, stage: Stage): Stage? {

        val index = SourceBidon.stages.indexOfFirst { it.idStage == code.toInt() }

        if (index != -1) {
            SourceBidon.stages.set(index, stage)
            return null
        }

        return ajouter(stage)
    }

    override fun effacer(code: String) {

        val sql = "DELETE FROM stage WHERE idStage = ?"
        db.update(sql, arrayOf(code.toInt()))
    }

    override fun créerUnStage(codeEmployeur: String, codeCandidature:String, stage: Stage): Stage? {

        if (stageExiste(stage.idStage)) {
            throw ConflitAvecUneRessourceExistanteException("Le stage que vous essayez de créer existe déjà.")
        }

        val candidature = candidatureDAO.chercherParCode(codeCandidature)
        val employeur = daoEmployeur.chercherParCode(codeEmployeur)

        if (candidature!= null) {
            stage.candidatureAcceptée = candidature
        } else {
            throw RessourceInexistanteException("La candidature avec le code $codeCandidature n'a pas été trouvé.")
        }

        if (employeur!= null) {
            stage.employeur = employeur
        } else {
            throw DroitAccèsInsuffisantException("Cet utilisateur n'a pas les droits suffisants pour éffectuer cette requête")
        }

        val satgeExistant = chercherStageParÉtudiant(candidature.étudiant!!.codeUtilisateur)

//        if (satgeExistant != null ) {
//            throw ConflitAvecUneRessourceExistanteException("Le stage avec le code ${stage.idStage} est déjà inscrite au service.")
//        }

        if (candidature.étatCandidature != ÉtatCandidature.Accepté) {
            throw CandidatureNonAcceptéeException("Vous ne pouvez pas ajouter un stage puisqu'il n'a pas de candidature.")
        }

        val sql =
            "INSERT INTO stage (stagiaire_codeEtudiant, candidature_codeCandidature, superviseur_assigne, employeur_codeEmployeur, stage_progression, lieu, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

        val args = arrayOf(
            candidature.étudiant.codeUtilisateur,
            candidature.codeCandidature,
            stage.superviseurAssigné,
            employeur.codeUtilisateur,
            stage.stageProgression.toString(),
            stage.lieu,
            stage.dateDébut,
            stage.dateFin
        )

        val generatedKeyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            for ((index, value) in args.withIndex()) {
                ps.setObject(index + 1, value)
            }
            ps
        }, generatedKeyHolder)

        return stage
    }

    private fun stageExiste(stageId: Int): Boolean {
        val sql = "SELECT COUNT(*) FROM stage WHERE idstage = ?"
        return db.queryForObject(sql, Int::class.java, stageId) > 0
    }

    override fun chercherStageParÉtudiant(code: String): List<Stage> {
        val sql = "SELECT * FROM stage WHERE stagiaire_codeEtudiant = ?"

        val stages = db.query(sql, arrayOf(code)) { rs, _ ->
            Stage(
                idStage = rs.getInt("idStage"),
                stagiaire = candidatureDAO.chercherParCode(rs.getString("candidature_codeCandidature"))!!.étudiant,
                candidatureAcceptée = candidatureDAO.chercherParCode(rs.getString("candidature_codeCandidature")),
                superviseurAssigné = rs.getString("superviseur_assigne"),
                employeur = daoEmployeur.chercherParCode(rs.getString("employeur_codeEmployeur")),
                stageProgression = StageProgression.valueOf(rs.getString("stage_progression")),
                lieu = rs.getString("lieu"),
                dateDébut = rs.getString("date_debut"),
                dateFin = rs.getString("date_fin")

            )
        }

        //throw RessourceInexistanteException("Aucun stage trouvé pour l'étudiant avec le code : $code")


        return stages
    }
}