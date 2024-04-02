package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Employeur
import crosemont.dti.g66.service_gestion_stage.Services.EmployeurService
import crosemont.dti.g66.service_gestion_stage.Services.EntrepriseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.security.Principal

@RestController
@RequestMapping("\${api.base-path:}")
class EmployeurControleur(val service: EmployeurService) {

    @Operation(
        summary = "Obtenir la liste des employeurs.",
        description = "Retourne la liste de tous les employeurs inscrits au service.",
        operationId = "obtenirRestaurants",
        responses = [
            ApiResponse(responseCode = "200", description = "Une liste d'employeurs, potentiellement vide, a été retournée."),
        ])
    @GetMapping("/employeurs")
    fun obtenirEmployeurs() = service.obtenirEmployeurs()

    @Operation(
        summary = "Obtenir un employeur par son code.",
        description = "Retourne les informations d'un employeur en le cherchant par son code.",
        operationId = "obtenirEmployeurParCode",
        responses = [
            ApiResponse(responseCode = "200", description = "Le restaurant a été trouvé."),
            ApiResponse(responseCode = "404", description = "Le restaurant recherché n'existe pas dans le service."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.")
        ])
    @GetMapping("/employeur")
    fun obtenirEmployeurParCode(principal: Principal) = service.chercherEmployeurParCode(principal.name) ?: throw RessourceInexistanteException("L'employeur avec le code :  ${principal.name} n'est pas inscrit comme employeur.")

    @Operation(
        summary = "Inscrire un nouvel employeur.",
        description = "Inscrit un nouvel employeur au service.",
        operationId = "inscrireEmployeur",
        responses = [
            ApiResponse(responseCode = "201", description = "L'employeur a été inscrit avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de l'inscription de l'employeur.")
        ]
    )
    @PostMapping("/employeurs")
    fun inscrireEmployeur(@RequestBody employeur: Employeur): ResponseEntity<Employeur> {
        val nouveauEmployeur = service.ajouterEmployeur(employeur)

        if (nouveauEmployeur != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouveauEmployeur.codeUtilisateur)
                .toUri()

            return ResponseEntity.created(uri).body(nouveauEmployeur)
        }

        return ResponseEntity.internalServerError().build()
    }
    @Operation(
        summary = "Modifier les informations d'un employeur existant.",
        description = "Modifie les informations d'un employeur existant.",
        operationId = "modifierEmployeur",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'employeur ont été modifiées."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec les informations modifiées."),
            ApiResponse(responseCode = "404", description = "L'employeur à modifier n'a pas été trouvé.")
        ]
    )
    @PutMapping("/employeurs/{code}")
    fun modifierEmployeur(@PathVariable code: String, @RequestBody employeur: Employeur): ResponseEntity<Employeur> {
        val nouveauEmployeur = service.modifierEmployeur(code,employeur)

        if (nouveauEmployeur != null) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouveauEmployeur.codeUtilisateur)
                .toUri()

            return ResponseEntity.created(uri).body(nouveauEmployeur)
        }
        return ResponseEntity.ok(employeur)
    }

    @Operation(
        summary = "Supprimer un employeur par son code.",
        description = "Supprime un employeur par son code.",
        operationId = "supprimerEmployeur",
        responses = [
            ApiResponse(responseCode = "204", description = "L'employeur a été supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "L'employeur à supprimer n'a pas été trouvé.")
        ]
    )
    @DeleteMapping("/employeurs/{code}")
    fun supprimerEmployeur(@PathVariable code: String): ResponseEntity<Employeur> {
        service.supprimerEmployeur(code)
        return ResponseEntity.noContent().build()
    }

}