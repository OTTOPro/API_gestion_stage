package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.CandidatureDAO
import crosemont.dti.g66.service_gestion_stage.DAO.EmployeurDAO
import crosemont.dti.g66.service_gestion_stage.DAO.OffreDAO
import crosemont.dti.g66.service_gestion_stage.Exceptions.DroitAccèsInsuffisantException
import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import org.springframework.stereotype.Service

@Service
class OffreService(val dao: OffreDAO, val daoCandidature:CandidatureDAO, val daoEmployeur: EmployeurDAO) {

    fun obtenirOffres(): List<Offre> {
        var offres = dao.chercherTous()
        for (offre in offres){
            offre.candidatures = daoCandidature.chercherParOffre(offre.idOffre.toString())
        }
        return offres
    }

    fun chercherOffresParCode(code: String): Offre? = dao.chercherParCode(code)

    fun chercherOffresParEmployeur(code: String): List<Offre>? = dao.chercherParEmployeur(code)

    fun creerUneOffre(code_employeur: String, offre: Offre) = dao.creerUneOffre(code_employeur, offre)

    fun modifierUneOffre(code_offre: String, offre: Offre, code_employeur: String): Offre? {
        var offreModifie = dao.modifier(code_offre, offre)
        var employeur = daoEmployeur.chercherParCode(code_employeur)
        if(employeur != null){
            if(offre.employeur?.codeUtilisateur == employeur.codeUtilisateur){
                return offreModifie
            }
            else{
                throw DroitAccèsInsuffisantException("l'utilisateur n'a pas les droits necessaires pour modifier cette offre specifique.")
            }
        }else{
            throw DroitAccèsInsuffisantException("l'utilisateur n'est pas authorisé.")
        }
    }

    fun supprimerUneOffre(code_employeur: String, code: String) {
        var offreSupprime = dao.chercherParCode(code)
        var employeur = daoEmployeur.chercherParCode(code_employeur)
        if(employeur != null){
            if (offreSupprime != null) {
                if(offreSupprime.employeur?.codeUtilisateur == employeur.codeUtilisateur){
                    dao.effacer(code)
                } else{
                    throw DroitAccèsInsuffisantException("l'utilisateur n'a pas les droits necessaires pour modifier cette offre specifique.")
                }
            }
        }else{
            throw DroitAccèsInsuffisantException("l'utilisateur n'est pas authorisé.")
        }

    }

}