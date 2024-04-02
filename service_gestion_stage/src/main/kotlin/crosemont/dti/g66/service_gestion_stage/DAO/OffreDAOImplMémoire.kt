package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Exceptions.ConflitAvecUneRessourceExistanteException
import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ModeEmploi
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class OffreDAOImplMémoire(val db: JdbcTemplate, val employeurDAO: EmployeurDAOImplMémoire): OffreDAO {

    override fun chercherTous(): List<Offre> = db.query("SELECT * FROM offre") { rs, _ ->
        Offre(
            idOffre = rs.getInt("idOffre"),
            employeur = employeurDAO.chercherParCode(rs.getString("employeur_codeEmployeur")),
            titrePoste = rs.getString("titrePoste"),
            modeEmploi = ModeEmploi.valueOf(rs.getString("modeEmploi")),
            description = rs.getString("description")
        )
    }

    override fun chercherParCode(code: String): Offre? {
        val sql = "SELECT * FROM offre WHERE idOffre = ?"

        val result = db.query(sql, arrayOf(code.toInt())) { rs, _ ->
            Offre(
                idOffre = rs.getInt("idOffre"),
                employeur = employeurDAO.chercherParCode(rs.getString("employeur_codeEmployeur").toString()),
                titrePoste = rs.getString("titrePoste"),
                modeEmploi = ModeEmploi.valueOf(rs.getString("modeEmploi")),
                description = rs.getString("description")
            )
        }

        return result.firstOrNull() ?: throw RessourceInexistanteException("Aucune offre trouvée avec l'ID : $code")
    }

    override fun chercherParEmployeur(code: String): List<Offre> {
        val sql = "SELECT * FROM offre WHERE employeur_codeEmployeur = ?"

        val offres = db.query(sql, arrayOf(code)) { rs, _ ->
            Offre(
                idOffre = rs.getInt("idOffre"),
                employeur = employeurDAO.chercherParCode(rs.getString("employeur_codeEmployeur").toString()),
                titrePoste = rs.getString("titrePoste"),
                modeEmploi = ModeEmploi.valueOf(rs.getString("modeEmploi")),
                description = rs.getString("description")
            )
        }
        if (offres.isEmpty()) {
            throw RessourceInexistanteException("Aucune offre trouvée pour l'employeur avec le code : $code")
        }

        return offres
    }

    override fun creerUneOffre(code: String, offre: Offre): Offre? {
        val employeur = employeurDAO.chercherParCode(code)

        if (employeur != null) {
            offre.employeur = employeur
        } else {
            throw RessourceInexistanteException("L'employeur avec le code $code n'a pas été trouvé.")
        }

        val offreExistante = chercherParEmployeurEtTitre(code, offre.titrePoste)

        if (offreExistante != null ) {
            throw ConflitAvecUneRessourceExistanteException("L'offre ${offre.titrePoste} est déjà inscrite au service.")
        }

        val sql =
            "INSERT INTO offre (employeur_codeEmployeur, titrePoste, modeEmploi, description) VALUES (?, ?, ?, ?)"

        val args = arrayOf(
            employeur.codeUtilisateur,
            offre.titrePoste,
            offre.modeEmploi.toString(),
            offre.description
        )

        val generatedKeyHolder = GeneratedKeyHolder()

        db.update({ connection ->
            val ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            for ((index, value) in args.withIndex()) {
                ps.setObject(index + 1, value)
            }
            ps
        }, generatedKeyHolder)

        return offre
    }

    private fun chercherParEmployeurEtTitre(code: String, titre: String): Offre? {
        val sql = "SELECT * FROM offre WHERE employeur_codeEmployeur = ? AND titrePoste = ?"

        val result = try {
            db.query(sql, arrayOf(code, titre)) { rs, _ ->
                Offre(
                    idOffre = rs.getInt("idOffre"),
                    employeur = employeurDAO.chercherParCode(rs.getInt("employeur_codeEmployeur").toString()),
                    titrePoste = rs.getString("titrePoste"),
                    modeEmploi = ModeEmploi.valueOf(rs.getString("modeEmploi")),
                    description = rs.getString("description")
                )
            }
        } catch (e: RessourceInexistanteException) {
            null
        }

        return result?.firstOrNull()
    }



    override fun modifier(code: String, offre: Offre): Offre? {
        val offreExistante = chercherParCode(code)

        val id = offre.idOffre

        if (offreExistante != null) {
            val sql = "UPDATE offre SET titrePoste = ?, modeEmploi = ?, description = ? WHERE idOffre = ?"
            val args = arrayOf(
                offre.titrePoste,
                offre.modeEmploi.toString(),
                offre.description,
                id
            )

            db.update(sql, *args)

            return offre
        } else {
            throw RessourceInexistanteException("L'offre avec le code $code n'a pas été trouvé.")
        }
    }

    override fun effacer(code: String) {
        try {
            val sql = "DELETE FROM offre WHERE idOffre = ?"
            db.update(sql, *arrayOf(code.toInt()))
        } catch (e: DataAccessException){

        }

    }
    override fun ajouter(offre: Offre): Offre? {
        TODO("Not yet implemented")
    }

}