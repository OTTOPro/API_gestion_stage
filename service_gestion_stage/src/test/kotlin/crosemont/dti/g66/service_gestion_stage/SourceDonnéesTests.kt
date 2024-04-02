package crosemont.dti.g66.service_gestion_stage

import crosemont.dti.g66.service_gestion_stage.Modèle.*
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ModeEmploi
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.StageProgression
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ÉtatCandidature

class SourceDonnéesTests {

    companion object {

        var etudiants: MutableList<Étudiant> = mutableListOf(
            Étudiant(
                codeUtilisateur = "123",
                nomUtilisateur = "Sedjari",
                prénomUtilisateur = "Othmane",
                courrielUtilisateur = "nom.prenom@example.com",
                téléphoneUtilisateur = "123456789",
                profilInformatique = ProfilInformatique.PROGRAMMATION,
                stageIntégration = true,
                adresseÉtudiant = "123 Rue de l'Étudiant"
            ),
            Étudiant(
                codeUtilisateur = "456",
                nomUtilisateur = "Tang",
                prénomUtilisateur = "Bao",
                courrielUtilisateur = "nom.prenom@example.com",
                téléphoneUtilisateur = "123456789",
                profilInformatique = ProfilInformatique.PROGRAMMATION,
                stageIntégration = true,
                adresseÉtudiant = "123 Rue de l'Étudiant"
            )
        )

        var employeurs:  MutableList<Employeur> = mutableListOf(

             Employeur(
                codeUtilisateur = "auth0|Jean",
                codeEntreprise = Entreprise(1,"CGI", "456 Rue de l'Entreprise"),
                nomUtilisateur = "NomEmployeur",
                prénomUtilisateur = "PrénomEmployeur",
                courrielUtilisateur = "nom.employeur@example.com",
                téléphoneUtilisateur = "987654321"),
             Employeur(
                codeUtilisateur = "auth0|Amine",
                codeEntreprise = Entreprise(1,"CGI", "456 Rue de l'Entreprise"),
                nomUtilisateur = "NomEmployeur",
                prénomUtilisateur = "PrénomEmployeur",
                courrielUtilisateur = "nom.employeur@example.com",
                téléphoneUtilisateur = "987654321")

        )

        var offres: MutableList<Offre> = mutableListOf(
            Offre(
                idOffre = 1,
                employeur = employeurs[0],
                titrePoste = "Développeur Android",
                modeEmploi = ModeEmploi.PRÉSENTIEL,
                description = "Description de l'offre 1",
                candidatures = mutableListOf()
            ),
            Offre(
                idOffre = 2,
                employeur = employeurs[1],
                titrePoste = "Développeur Web",
                modeEmploi = ModeEmploi.HYBRIDE,
                description = "Description de l'offre 2",
                candidatures = mutableListOf()
            )
        )

        var candidatures: MutableList<Candidature> = mutableListOf(
            Candidature(1,offres[0], etudiants[0], ÉtatCandidature.Encours ),
            Candidature(2,offres[1], etudiants[1], ÉtatCandidature.Accepté )
        )

        var stages:  MutableList<Stage> = mutableListOf(
             Stage(
                idStage = 1,
                stagiaire = etudiants[0],
                candidatureAcceptée = candidatures[0],
                superviseurAssigné = "Professeur",
                employeur = employeurs[0],
                stageProgression = StageProgression.DÉBUTÉ,
                lieu = "Emplacement du stage",
                dateDébut = "2023-01-01",
                dateFin = "2023-12-31"
        )
        )

    }

}