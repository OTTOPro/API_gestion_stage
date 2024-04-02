package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise
import crosemont.dti.g66.service_gestion_stage.Services.EntrepriseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class EntrepriseController(val service: EntrepriseService) {

    @Operation(
        summary = "Obtenir la liste des entreprises.",
        description = "Retourne la liste de toutes les entreprises enregistrées.",
        operationId = "obtenirEntreprises",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste des entreprises a été retournée.")
        ]
    )
    @GetMapping("/entreprises")
    fun obtenirEntreprises() = service.obtenirEntreprises()

    @Operation(
        summary = "Obtenir une entreprise par son code.",
        description = "Retourne les informations d'une entreprise en la cherchant par son code.",
        operationId = "obtenirEntrepriseParCode",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'entreprise ont été trouvées."),
            ApiResponse(responseCode = "404", description = "L'entreprise recherchée n'existe pas.")
        ]
    )
    @GetMapping("/entreprises/{code}")
    fun obtenirEntrepriseParCode(@PathVariable code: String) = service.chercherEntrepriseParCode(code)


    @Operation(
        summary = "Obtenir une entreprise par le code de son employeur.",
        description = "Retourne les informations de l'entreprise associée à un employeur par son code.",
        operationId = "obtenirEntrepriseParEmployeur",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'entreprise associée à l'employeur ont été trouvées."),
            ApiResponse(responseCode = "404", description = "Aucune entreprise n'est associée à l'employeur recherché.")
        ]
    )
    @GetMapping("/employeurs/{code}/entreprises")
    fun obtenirEntrepriseParEmployeur(@PathVariable code: String) = service.chercherEntrepriseParEmployeur(code)

    @Operation(
        summary = "Créer une nouvelle entreprise.",
        description = "Crée une nouvelle entreprise enregistrée dans le service.",
        operationId = "creerEntreprise",
        responses = [
            ApiResponse(responseCode = "201", description = "La nouvelle entreprise a été créée avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de la création de l'entreprise.")
        ]
    )
    @PostMapping("/entreprises")
    fun créerEntreprise(@RequestBody entreprise: Entreprise): ResponseEntity<Entreprise> {
        val nouveauEmployeur = service.ajouter(entreprise)

        if (nouveauEmployeur != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouveauEmployeur.codeEntreprise)
                .toUri()

            return ResponseEntity.created(uri).body(nouveauEmployeur)
        }

        return ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Modifier les informations d'une entreprise existante.",
        description = "Modifie les informations d'une entreprise existante.",
        operationId = "modifierEntreprise",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'entreprise ont été modifiées."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec les informations modifiées."),
            ApiResponse(responseCode = "404", description = "L'entreprise à modifier n'a pas été trouvée.")
        ]
    )
    @PutMapping("/entreprises/{code_entreprise}")
    fun modifierEntreprise(@PathVariable code_entreprise: String, @RequestBody entreprise: Entreprise): ResponseEntity<Entreprise> {
        val nouveauEmployeur = service.modifier(code_entreprise, entreprise)

        if (nouveauEmployeur != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouveauEmployeur.codeEntreprise)
                .toUri()

            return ResponseEntity.created(uri).body(nouveauEmployeur)
        }

        return ResponseEntity.ok(entreprise)
    }

    @Operation(
        summary = "Supprimer une entreprise par son code.",
        description = "Supprime une entreprise par son code.",
        operationId = "supprimerEntreprise",
        responses = [
            ApiResponse(responseCode = "204", description = "L'entreprise a été supprimée avec succès."),
            ApiResponse(responseCode = "404", description = "L'entreprise à supprimer n'a pas été trouvée.")
        ]
    )
    @DeleteMapping("entreprises/{code_entreprise}")
    fun supprimerEntreprise(@PathVariable code_entreprise: String): ResponseEntity<Entreprise> {
        service.effacer(code_entreprise)
        return ResponseEntity.noContent().build()
    }

}