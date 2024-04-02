package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant

interface ÉtudiantDAO : DAO<Étudiant> {

    override fun chercherTous(): List<Étudiant>
    override fun chercherParCode(code: String): Étudiant?
    override fun ajouter(étudiant: Étudiant): Étudiant?
    override fun modifier(code: String, étudiant: Étudiant): Étudiant?
    override fun effacer(code: String)
    fun obtenirÉtudiantParCandidature(code: String): Étudiant?
    fun obtenirDocumentsParÉtudiant(code: String): List<Document>
    fun obtenirProfilParÉtudiant(code: String): ProfilInformatique?
}