package crosemont.dti.g66.service_gestion_stage.Modèle

class Adresse(
    var numéroCivique: Int,
    val rue: String,
    val ville: String,
    val codePostal: String,
    val pays: String
) {
}