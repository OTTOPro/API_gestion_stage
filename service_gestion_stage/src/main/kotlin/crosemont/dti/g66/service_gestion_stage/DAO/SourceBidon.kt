package crosemont.dti.g66.service_gestion_stage.DAO

import crosemont.dti.g66.service_gestion_stage.Modèle.*
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.*

class SourceBidon {

    companion object{
        val adresse1 = Adresse(123, "Rue de l'Entreprise", "Ville1", "12345", "Pays1")
        val adresse2 = Adresse(456, "Avenue de la Compagnie", "Ville2", "67890", "Pays2")
        val adresse3 = Adresse(789, "Avenue de la Sagesse", "Ville3", "54321", "Pays3")

        val entreprises = mutableListOf(
            Entreprise(1, "NomEntreprise1", "123 Rue ABC"),
            Entreprise(2, "NomEntreprise2", "Laval")
        )

        val employeurs = mutableListOf(
            Employeur("101", "Nom1", "Prenom1", "othmanesedjari@gmail.com", "123456789", entreprises[0]),
            Employeur("102", "Nom2", "Prenom2", "email2@example.com", "987654321", entreprises[1])
        )

        val étudiants = mutableListOf(
            Étudiant("auth0|656c05ab34408e731c390be7", "othmane", "sedjari", "othmanesedjari@gmail.com", "(514) 123-4567", ProfilInformatique.PROGRAMMATION, false, "321 Rue ABC"),
            Étudiant("Nom1", "NomÉtudiant2", "PrénomÉtudiant2", "emailÉtudiant2@example.com", "(514) 321-7654", ProfilInformatique.RÉSAUTIQUE, true, "123 Rue CBA")
        )

        val offres = mutableListOf(
            Offre(
                idOffre = 1,
                employeur = employeurs[0],
                titrePoste = "Titre du poste 1",
                modeEmploi = ModeEmploi.PRÉSENTIEL,
                description = "Description de l'offre 1"
            ),
            Offre(
                idOffre = 2,
                employeur = employeurs[0],
                titrePoste = "Titre du poste 2",
                modeEmploi = ModeEmploi.ÀDISTANCE,
                description = "Description de l'offre 2"
            ),
            Offre(
                idOffre = 3,
                employeur = employeurs[1],
                titrePoste = "Titre du poste 3",
                modeEmploi = ModeEmploi.PRÉSENTIEL,
                description = "Description de l'offre 3"
            ),
            Offre(
                idOffre = 4,
                employeur = employeurs[0],
                titrePoste = "Titre du poste 4",
                modeEmploi = ModeEmploi.HYBRIDE,
                description = "Description de l'offre 4"
            )
        )

        val candidatures = mutableListOf(
                Candidature(1, offres[0], étudiants[0]),
                Candidature(2, offres[1], étudiants[1])
        )

        val stages = mutableListOf(
            Stage(1, étudiants[0], candidatures[0], "Superviseur 1", employeurs[0], StageProgression.DÉBUTÉ, "Lieu 1", "2023-01-01", "2023-06-01"),
            Stage(2, étudiants[1], candidatures[1], "Superviseur 2", employeurs[1], StageProgression.ENCOURS, "Lieu 2", "2023-02-01", "2023-07-01")
        )


        val documents = mutableListOf(
            Document(1, étudiants[0],"www.github.com/étudiant1/monProjet", TypeDocument.LettreDeMotivation),
            Document(2, étudiants[1],"www.github.com/étudiant2/monProjet", TypeDocument.CV),
        )

    }

}