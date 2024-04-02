package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Services.CandidatureService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.security.Principal

@RestController
class CandidatureControleur(val service: CandidatureService) {

    @Operation(
        summary = "Obtenir les candidatures d'un étudiant.",
        description = "Retourne la liste des candidatures d'un étudiant.",
        operationId = "obtenirCandidaturesParEtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste des candidatures de l'étudiant a été retournée."),
            ApiResponse(responseCode = "404", description = "Aucune candidature n'est associée à l'étudiant."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.")

        ]
    )
    @GetMapping("/etudiant/candidatures")
    fun obtenirCandidaturesParEtudiant(principal: Principal) = service.obtenirCandidaturesParÉtudiant(principal.name)

    @Operation(
        summary = "Obtenir les candidatures pour une offre de stage.",
        description = "Retourne la liste des candidatures pour une offre de stage.",
        operationId = "obtenirCandidaturesParOffre",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste des candidatures pour l'offre de stage a été retournée."),
            ApiResponse(responseCode = "404", description = "Aucune candidature n'est associée à l'offre de stage.") ,
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.")
    ]
    )
    @GetMapping("/offres/{code_offre}/candidatures")
    fun obtenirCandidaturesParOffre(@PathVariable code_offre: String,principal: Principal) = service.chercherCadidatureParOffre(principal.name ,code_offre)

    @Operation(
        summary = "Postuler à une offre de stage.",
        description = "Postule à une offre de stage enregistrée dans le service.",
        operationId = "postulerOffre",
        responses = [
            ApiResponse(responseCode = "201", description = "La candidature à l'offre de stage a été créée avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de la création de la candidature à l'offre de stage."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les etudiants connectes peuvent postuler a des offres.)")

        ]
    )
    @PostMapping("/offres/{code_offre}/candidatures")
    fun postulerOffre(@PathVariable code_offre: String, @RequestBody candidature: Candidature, principal: Principal): ResponseEntity<Candidature> {
        val nouvelleCandidature = service.postulerAUneOffre(principal.name, code_offre, candidature)

        if (nouvelleCandidature != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouvelleCandidature.codeCandidature)
                .toUri()

            return ResponseEntity.created(uri).body(nouvelleCandidature)
        }

        return ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Modifier l'état d'une candidature.",
        description = "Modifie l'état d'une candidature existante.",
        operationId = "modifierÉtatCandidature",
        responses = [
            ApiResponse(responseCode = "200", description = "L'état de la candidature a été modifié."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec l'état modifié."),
            ApiResponse(responseCode = "404", description = "La candidature à modifier n'a pas été trouvée."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les employes dont les candidatures appartiennent a des offres de leurs entreprise qui peuvent modifier l'etat des candidatures)")
        ]
    )
    @PutMapping("/candidatures/{code}")
    fun modifierÉtatCandidature(@PathVariable code: String, @RequestBody candidature: Candidature,principal: Principal): ResponseEntity<Candidature> {
        val nouveauCandidature = service.modifierEtatOffre(principal.name,code, candidature)
        if (nouveauCandidature != null) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouveauCandidature.codeCandidature)
                .toUri()

            return ResponseEntity.created(uri).body(nouveauCandidature)
        }
        return ResponseEntity.ok(candidature)
    }


    @Operation(
        summary = "Supprimer une candidature par son code.",
        description = "Supprime une candidature par son code.",
        operationId = "supprimerCandidature",
        responses = [
            ApiResponse(responseCode = "204", description = "La candidature a été supprimée avec succès."),
            ApiResponse(responseCode = "404", description = "La candidature à supprimer n'a pas été trouvée."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seul l'etudiant dont la candidature appartient qui peut la supprimer .")
        ]
    )
    @DeleteMapping("/candidatures/{code}")
    fun supprimerCandidatures(@PathVariable code: String, principal: Principal): ResponseEntity<Candidature> {
        service.supprimerCandidature(code, principal.name)
        return ResponseEntity.noContent().build()
    }

}