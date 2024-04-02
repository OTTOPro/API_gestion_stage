package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import crosemont.dti.g66.service_gestion_stage.Modèle.Stage
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant

interface CandidatureDAO : DAO<Candidature> {

    override fun chercherTous(): List<Candidature>
    override fun chercherParCode(code: String): Candidature?
    fun chercherParOffre(code: String): List<Candidature>?
    override fun ajouter(candidature: Candidature): Candidature?
    fun postulerOffre(code_étudiant: String, code_offre: String, candidature: Candidature): Candidature?
    override fun modifier(code: String, candidature: Candidature): Candidature?
    fun modifierÉtatOffre(code: String,candidature: Candidature): Candidature?
    override fun effacer(id: String)
    fun obtenirCandidaturesParÉtudiant(codeCandidature: String): List<Candidature>?
}