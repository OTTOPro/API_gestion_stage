package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.EntrepriseDAO
import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise
import org.springframework.stereotype.Service

@Service
class EntrepriseService(val dao:EntrepriseDAO) {

    fun obtenirEntreprises(): List<Entreprise> = dao.chercherTous()

    fun chercherEntrepriseParCode(code: String): Entreprise? = dao.chercherParCode(code)

    fun chercherEntrepriseParEmployeur(code: String): Entreprise? = dao.chercherParEmployeur(code)

    fun ajouter(entreprise: Entreprise): Entreprise? = dao.ajouter(entreprise)

    fun modifier(code: String, entreprise: Entreprise): Entreprise? = dao.modifier(code, entreprise)

    fun effacer(code: String) = dao.effacer(code)


}