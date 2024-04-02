package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Offre

interface OffreDAO: DAO<Offre> {

    override fun chercherTous(): List<Offre>
    override fun chercherParCode(code: String): Offre?
    fun chercherParEmployeur(code: String): List<Offre>?
    override fun ajouter(offre: Offre): Offre?
    fun creerUneOffre(code: String, offre: Offre): Offre?
    override fun modifier(code: String, offre: Offre): Offre?
    override fun effacer(code: String)

}