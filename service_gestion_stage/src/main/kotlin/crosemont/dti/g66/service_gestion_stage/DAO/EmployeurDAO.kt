package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur

interface EmployeurDAO: DAO<Employeur>  {

    override fun chercherTous(): List<Employeur>
    override fun chercherParCode(code: String): Employeur?
    override fun ajouter(employeur: Employeur): Employeur?
    override fun modifier(code: String, element: Employeur): Employeur?
    override fun effacer(code: String)

}