package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.CandidatureDAO
import crosemont.dti.g66.service_gestion_stage.DAO.ÉtudiantDAO
import crosemont.dti.g66.service_gestion_stage.Exceptions.DroitAccèsInsuffisantException
import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Service
class ÉtudiantService (val étudiantDAO: ÉtudiantDAO) {

    fun obtenirÉtudiants(): List<Étudiant> = étudiantDAO.chercherTous()

    fun chercherÉtudiantParCode(code: String?): Étudiant? {
         if (code !=null){
            return  étudiantDAO.chercherParCode(code)
         }else{
             return null
         }


    }
    fun ajouterÉtudiant(étudiant: Étudiant): Étudiant? = étudiantDAO.ajouter(étudiant)
    fun modifierÉtudiant(code: String, étudiant: Étudiant): Étudiant? {
        val etudiantConnecte = chercherÉtudiantParCode(code)
        if(etudiantConnecte != null){
            if(etudiantConnecte.codeUtilisateur == étudiant.codeUtilisateur) {
                var etudiantModifie = étudiantDAO.modifier(code, étudiant)
                return etudiantModifie
            }else{
                throw DroitAccèsInsuffisantException("Vous pouvez pas  modifier le profil d'un autre utilisateur.")
            }
        }else{
            throw DroitAccèsInsuffisantException("Vous n'etes pas authentifie autant qu'etudiant.")
        }

    }
    fun supprimerÉtudiant(code: String) = étudiantDAO.effacer(code)
    fun obtenirÉtudiantParCandidature(code: String) = étudiantDAO.obtenirÉtudiantParCandidature(code)
    fun obtenirProfilParÉtudiant(code: String): ProfilInformatique? = étudiantDAO.obtenirProfilParÉtudiant(code)
    fun obtenirDocumentsParÉtudiant(code: String): List<Document> = étudiantDAO.obtenirDocumentsParÉtudiant(code)
}