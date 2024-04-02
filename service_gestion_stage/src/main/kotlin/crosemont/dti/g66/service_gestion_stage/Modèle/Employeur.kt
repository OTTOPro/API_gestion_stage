package crosemont.dti.g66.service_gestion_stage.Modèle

data class Employeur (
    val codeUtilisateur: String,
    val nomUtilisateur: String,
    val prénomUtilisateur: String,
    val courrielUtilisateur: String,
    val téléphoneUtilisateur: String,
    val codeEntreprise: Entreprise?,
    val listeStagiaires: MutableList<Stage> = mutableListOf()
) {
}