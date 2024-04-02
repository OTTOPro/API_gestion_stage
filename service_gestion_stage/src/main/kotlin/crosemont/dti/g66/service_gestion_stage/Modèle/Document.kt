package crosemont.dti.g66.service_gestion_stage.Modèle

import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.TypeDocument

class Document(
    val id: Int,
    val étudiant: Étudiant,
    val lien: String,
    val type: TypeDocument
) {
}