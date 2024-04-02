package crosemont.dti.g66.service_gestion_stage.Modèle

import java.io.Serializable

class Entreprise(
    var codeEntreprise: Int,
    val nomEntreprise: String,
    val adresseEntreprise: String
) : Serializable {
}