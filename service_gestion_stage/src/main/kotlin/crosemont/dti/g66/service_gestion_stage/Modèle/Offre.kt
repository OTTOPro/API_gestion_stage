package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ModeEmploi
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.TypeEmploi

data class Offre(
    var idOffre: Int,
    var employeur: Employeur?,
    val titrePoste: String,
    val modeEmploi: ModeEmploi,
    val description: String,
    var candidatures: List<Candidature>? = mutableListOf()
) {
}