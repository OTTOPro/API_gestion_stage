package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Adresse
import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur
import org.springframework.stereotype.Repository

@Repository
interface AdresseDAO: DAO<Adresse> {

    override fun chercherTous(): List<Adresse>
    override fun chercherParCode(code: String): Adresse?
    override fun ajouter(adresse: Adresse): Adresse?
    override fun modifier(code: String, adresse: Adresse): Adresse?
    override fun effacer(code: String)

}