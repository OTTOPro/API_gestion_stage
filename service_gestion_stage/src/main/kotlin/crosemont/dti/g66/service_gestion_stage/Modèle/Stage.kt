package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.StageProgression

class Stage(

    val idStage: Int,
    var stagiaire: Étudiant?,
    var candidatureAcceptée: Candidature?,
    val superviseurAssigné: String,
    var employeur: Employeur?,
    val stageProgression: StageProgression,
    val lieu: String,
    val dateDébut: String,
    val dateFin: String

) {
}