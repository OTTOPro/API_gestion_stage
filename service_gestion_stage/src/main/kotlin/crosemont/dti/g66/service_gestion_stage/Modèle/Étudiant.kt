package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import java.sql.Blob

data class Étudiant(
    override val codeUtilisateur: String,
    override val nomUtilisateur: String,
    override val prénomUtilisateur: String,
    override val courrielUtilisateur: String,
    override val téléphoneUtilisateur: String,
    val profilInformatique: ProfilInformatique,
    val stageIntégration: Boolean,
    val adresseÉtudiant: String

) : Utilisateur(codeUtilisateur, nomUtilisateur, prénomUtilisateur, courrielUtilisateur, téléphoneUtilisateur) {
}
