package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.EmployeurDAO
import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur
import org.springframework.stereotype.Service

@Service
class EmployeurService(val dao: EmployeurDAO) {

    fun obtenirEmployeurs(): List<Employeur> = dao.chercherTous()

    fun chercherEmployeurParCode(code: String): Employeur? = dao.chercherParCode(code)

    fun ajouterEmployeur(employeur: Employeur): Employeur? = dao.ajouter(employeur)

    fun modifierEmployeur(code: String, employeur: Employeur): Employeur? = dao.modifier(code, employeur)

    fun supprimerEmployeur(code: String) = dao.effacer(code)
}