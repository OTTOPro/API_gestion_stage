package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Stage

interface StageDAO : DAO<Stage> {

    override fun chercherTous(): List<Stage>
    override fun chercherParCode(code: String): Stage?
    override fun ajouter(stage: Stage): Stage?
    override fun modifier(code: String, stage: Stage): Stage?
    override fun effacer(code: String)
    fun créerUnStage(codeEmployeur: String, codeCandidature:String, stage: Stage): Stage?
    fun chercherStageParÉtudiant(code: String): List<Stage>
}