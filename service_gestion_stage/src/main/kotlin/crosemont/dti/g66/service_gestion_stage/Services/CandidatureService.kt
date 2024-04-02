package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.CandidatureDAO
import crosemont.dti.g66.service_gestion_stage.DAO.StageDAO
import crosemont.dti.g66.service_gestion_stage.DAO.EmployeurDAO
import crosemont.dti.g66.service_gestion_stage.DAO.ÉtudiantDAO
import crosemont.dti.g66.service_gestion_stage.Exceptions.DroitAccèsInsuffisantException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.StageEnCoursException
import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Stage
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class CandidatureService (val candidatureDAO: CandidatureDAO, val employeurDAO: EmployeurDAO, val stageDAO: StageDAO, val étudiantDao: ÉtudiantDAO) {


    fun chercherTousCandidatures(): List<Candidature> = candidatureDAO.chercherTous()

    fun chercherCandidatureParCode(code: String): Candidature? = candidatureDAO.chercherParCode(code)

    fun chercherCadidatureParOffre(code_Employeur: String,code: String): List<Candidature>? {
        var candidatures = candidatureDAO.chercherParOffre(code)
        var employeur = employeurDAO.chercherParCode(code_Employeur)

        if(employeur != null) {
            if (candidatures != null) {
                for (candidature in candidatures) {
                    if(candidature.offre?.employeur?.codeUtilisateur != employeur.codeUtilisateur){
                        throw DroitAccèsInsuffisantException("L'utilisateur authentifié n'est pas autorisé a effectuer cette requete pour cette canidature.")
                    }
                }
            }
        }else {
            throw DroitAccèsInsuffisantException("L'utilisateur n'a pas les droits necessaires.")
        }
        return candidatures

    }

    fun postulerAUneOffre(code_étudiant: String, code_offre: String, candidature: Candidature): Candidature? {
        var etudiant = étudiantDao.chercherParCode(code_étudiant)
        if(etudiant != null) {
            var candidatureAjoute = candidatureDAO.postulerOffre(code_étudiant, code_offre, candidature)
            return candidatureAjoute
        } else{
            throw DroitAccèsInsuffisantException("Seule les etudiants inscrits au service qui peuvent postuler a des offres.")
        }

    }

    fun modifierEtatOffre(code: String, candidature: Candidature): Candidature? = candidatureDAO.modifier(code, candidature)
    fun modifierÉtatCandidature(code: String, candidature: Candidature, stage: Stage): Pair<Candidature?, Stage?> {

        var candidatureModifiée = candidatureDAO.modifier(code, candidature)
        var stageCrée: Stage? = null

        if (candidatureModifiée!!.étatCandidature == ÉtatCandidature.Accepté) {

            stageCrée = stageDAO.créerUnStage(
                candidatureModifiée.offre!!.employeur!!.codeUtilisateur,
                code,
                stage
            )
        }

        return if (stageCrée != null) Pair(candidature, stageCrée)
        else Pair(candidature, null)
    }

    fun modifierEtatOffre(code_Employeur: String, code: String, candidature: Candidature): Candidature? {
        var employeur = employeurDAO.chercherParCode(code_Employeur)
        var candidatureAvantModification = chercherCandidatureParCode(candidature.codeCandidature.toString())

        if (employeur != null) {
            if (candidatureAvantModification != null) {
                if (candidatureAvantModification.offre?.employeur?.codeUtilisateur == employeur.codeUtilisateur) {
                    var candidatureModifie = candidatureDAO.modifier(code, candidature)
                    return candidatureModifie
                } else {
                    throw DroitAccèsInsuffisantException("L'utilisateur authentifié n'est pas autorisé a effectuer cette requete pour cette canidature.")
                }
            }

        } else {
            throw DroitAccèsInsuffisantException("L'utilisateur n'a pas les droits necessaires.")
        }
        return null
    }

    fun obtenirCandidaturesParÉtudiant(code: String): List<Candidature>? =
        candidatureDAO.obtenirCandidaturesParÉtudiant(code)

    fun supprimerCandidature(code_Candidature: String, code_étudiant: String) {

        var étudiant = étudiantDao.chercherParCode(code_étudiant)
        var candidature = chercherCandidatureParCode(code_Candidature)
        var stage = stageDAO.chercherStageParÉtudiant(code_étudiant)

        if(étudiant != null) {

            if (candidature != null) {

                if (étudiant.codeUtilisateur == candidature.étudiant!!.codeUtilisateur) {

                    if (stage != null) {

                       // if (stage[0].candidatureAcceptée!!.codeCandidature != candidature.codeCandidature) {

                            candidatureDAO.effacer(code_Candidature)
                       // } else {

                            //throw StageEnCoursException("Impossible de supprimer. Le stage est déjà en cours.")
                       // }
                    }

                } else {

                    throw DroitAccèsInsuffisantException("L'étudiant connecté ne peut pas supprimé une candidature qui lui n'appartient pas.")
                }
            }

        }  else{

            throw DroitAccèsInsuffisantException("Seule les etudiants inscrits au service qui peuvent supprimer une candidature.")
        }

    }
}
