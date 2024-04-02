package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import crosemont.dti.g66.service_gestion_stage.Services.OffreService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.security.Principal

@RestController
class OffreControleur(val service: OffreService) {

    @Operation(
        summary = "Obtenir la liste des offres de stage.",
        description = "Retourne la liste de toutes les offres de stage enregistrées.",
        operationId = "obtenirOffres",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste des offres de stage a été retournée.")
        ]
    )
    @GetMapping("/offres")
    fun obtenirOffres() = service.obtenirOffres()

    @Operation(
        summary = "Obtenir une offre de stage par son code.",
        description = "Retourne les informations d'une offre de stage en la cherchant par son code.",
        operationId = "obtenirOffresParCode",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'offre de stage ont été trouvées."),
            ApiResponse(responseCode = "404", description = "L'offre de stage recherchée n'existe pas.")
        ]
    )
    @GetMapping("/offres/{code}")
    fun obtenirOffresParCode(@PathVariable code: String) = service.chercherOffresParCode(code)

    @Operation(
        summary = "Obtenir les offres de stage d'un employeur.",
        description = "Retourne les informations des offres de stage associées à un employeur.",
        operationId = "obtenirOffresParEmployeur",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations des offres de stage associées à l'employeur ont été trouvées."),
            ApiResponse(responseCode = "404", description = "Aucune offre de stage n'est associée à l'employeur recherché."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
        ]
    )
    @GetMapping("/employeur/offres")
    fun obtenirOffresParEmployeur(principal: Principal) = service.chercherOffresParEmployeur(principal.name)

    @Operation(
        summary = "Modifier les informations d'une offre de stage existante.",
        description = "Modifie les informations d'une offre de stage existante.",
        operationId = "modifierOffre",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'offre de stage ont été modifiées."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec les informations modifiées."),
            ApiResponse(responseCode = "404", description = "L'offre de stage à modifier n'a pas été trouvée."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les employes dont les offres appartiennent a leur entreprise qui peuvent les modifier )")
        ]
    )
    @PutMapping("/offres/{code_offre}")
    fun modifierOffre(@PathVariable code_offre: String, @RequestBody offre: Offre, principal: Principal): ResponseEntity<Offre> {
        val nouvelleOffre = service.modifierUneOffre(code_offre, offre, principal.name)

        if (nouvelleOffre != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouvelleOffre.idOffre)
                .toUri()

            return ResponseEntity.ok().location(uri).body(nouvelleOffre)
        }

        return ResponseEntity.ok(offre)

    }

    @Operation(
        summary = "Créer une nouvelle offre de stage.",
        description = "Crée une nouvelle offre de stage enregistrée dans le service.",
        operationId = "creerOffre",
        responses = [
            ApiResponse(responseCode = "201", description = "La nouvelle offre de stage a été créée avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de la création de l'offre de stage."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les employes qui peuvent creer des offres de stage.)")
        ]
    )
    @PostMapping("/employeur/offres")
    fun créerOffre(principal: Principal, @RequestBody offre: Offre): ResponseEntity<Offre> {
        val nouvelleOffre = service.creerUneOffre(principal.name, offre)

        if (nouvelleOffre != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouvelleOffre.idOffre)
                .toUri()

            return ResponseEntity.created(uri).body(nouvelleOffre)
        }

        return ResponseEntity.internalServerError().build()

    }

    @Operation(
        summary = "Supprimer une offre de stage par son code.",
        description = "Supprime une offre de stage par son code.",
        operationId = "supprimerOffre",
        responses = [
            ApiResponse(responseCode = "204", description = "L'offre de stage a été supprimée avec succès."),
            ApiResponse(responseCode = "404", description = "L'offre de stage à supprimer n'a pas été trouvée."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les employes dont les offres appartiennent a leur entreprise qui peuvent les supprimer)")
        ]
    )
    @DeleteMapping("/offres/{code_offre}")
    fun supprimerOffre(@PathVariable code_offre: String, principal: Principal): ResponseEntity<Offre> {
        service.supprimerUneOffre(principal.name,code_offre)
        return ResponseEntity.noContent().build()
    }

}