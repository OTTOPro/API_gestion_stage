package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.RôleProfesseur

open class Professeur(

    val rôle: RôleProfesseur,
    val profilInformatique: ProfilInformatique,
    codeUtilisateur: String?,
    nomUtilisateur: String,
    prénomUtilisateur: String,
    courrielUtilisateur: String,
    téléphoneUtilisateur: String

) : Utilisateur(codeUtilisateur, nomUtilisateur, prénomUtilisateur, courrielUtilisateur, téléphoneUtilisateur) {
}