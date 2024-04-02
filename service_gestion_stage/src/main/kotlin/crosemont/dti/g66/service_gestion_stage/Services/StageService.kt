package crosemont.dti.g66.service_gestion_stage.Services

import crosemont.dti.g66.service_gestion_stage.DAO.SourceBidon
import crosemont.dti.g66.service_gestion_stage.DAO.StageDAO
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import crosemont.dti.g66.service_gestion_stage.Modèle.Stage
import org.springframework.stereotype.Service

@Service
class StageService (val stageDAO: StageDAO) {

    fun obtenirStages(): List<Stage> {
        var stages = stageDAO.chercherTous()

        return stages
    }
    fun créerUnStage(codeEmployeur: String, codeCandidature: String, stage: Stage) = stageDAO.créerUnStage(codeCandidature, codeEmployeur, stage)

    fun obtenirStageParÉtudiant(codeÉtudiant: String): List<Stage> = stageDAO.chercherStageParÉtudiant(codeÉtudiant)
    fun modifierProgressionStage(codeÉtudiant: String, stage: Stage): Stage? = stageDAO.modifier(codeÉtudiant, stage)
}