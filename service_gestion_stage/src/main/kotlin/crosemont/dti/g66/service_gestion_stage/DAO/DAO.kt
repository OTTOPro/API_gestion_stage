package crosemont.dti.g66.service_gestion_stage.DAO

interface DAO<T> {

    fun chercherTous(): List<T>
    fun chercherParCode(code: String): T?
    fun ajouter(element: T): T?
    fun modifier(id: String, element: T): T?
    fun effacer(id: String)

}