package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise

interface EntrepriseDAO: DAO<Entreprise> {
    override fun chercherTous(): List<Entreprise>
    override fun chercherParCode(code: String): Entreprise?
    fun chercherParEmployeur(code: String): Entreprise?
    override fun ajouter(entreprise: Entreprise): Entreprise?
    override fun modifier(code: String, entreprise: Entreprise): Entreprise?
    override fun effacer(code: String)

}