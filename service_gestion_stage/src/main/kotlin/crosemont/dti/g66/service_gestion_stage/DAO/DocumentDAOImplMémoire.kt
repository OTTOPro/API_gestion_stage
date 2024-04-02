package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import org.springframework.stereotype.Repository

@Repository
class DocumentDAOImplMémoire: DocumentDAO {
    override fun chercherTous(): List<Document> = SourceBidon.documents
    override fun chercherParCode(code: String): Document? = SourceBidon.documents.find { it.id == code.toInt() }
    override fun ajouter(document: Document): Document? {

        val index = SourceBidon.documents.indexOfFirst { it.id == document.id }

        if (index != -1) {

            throw ConflitAvecUneRessourceExistanteException("Le document avec l'id '${document.id}' est déjà existant.")
        }

        SourceBidon.documents.add(document)
        return document
    }

    override fun modifier(id: String, document: Document): Document? {

        val index = SourceBidon.documents.indexOfFirst { it.id == id.toInt() }

        if (index != -1) {
            SourceBidon.documents.set(index, document)
            return null
        }

        return ajouter(document)
    }

    override fun effacer(id: String) {

        val index = SourceBidon.documents.find { it.id == id.toInt() }

        if (index != null) {
            SourceBidon.documents.remove(index)
        }

        throw RessourceInexistanteException("Le document avec l'id '$id' n'existe pas.")
    }
}