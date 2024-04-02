package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature

data class Candidature(
    val codeCandidature: Int,
    var offre: Offre?,
    val étudiant: Étudiant?,
    val étatCandidature: ÉtatCandidature = ÉtatCandidature.Encours
) {
}