package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.RôleProfesseur

class Coordinateur(

    rôle: RôleProfesseur,
    profilInformatique: ProfilInformatique,
    codeUtilisateur: String?,
    nomUtilisateur: String,
    prénomUtilisateur: String,
    courrielUtilisateur: String,
    téléphoneUtilisateur: String

) : Professeur(
    rôle,
    profilInformatique,
    codeUtilisateur,
    nomUtilisateur,
    prénomUtilisateur,
    courrielUtilisateur,
    téléphoneUtilisateur
) {
}