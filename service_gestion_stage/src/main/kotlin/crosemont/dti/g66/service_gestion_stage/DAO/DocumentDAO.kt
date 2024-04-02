package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.Document


interface DocumentDAO : DAO<Document> {

    override fun chercherTous(): List<Document>
    override fun chercherParCode(code: String): Document?
    override fun ajouter(document: Document): Document?
    override fun modifier(id: String, document: Document): Document?
    override fun effacer(id: String)
}