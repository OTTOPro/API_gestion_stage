package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Exceptions.RessourceInexistanteException
import crosemont.dti.g66.service_gestion_stage.Modèle.Document
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ProfilInformatique
import crosemont.dti.g66.service_gestion_stage.Modèle.Étudiant
import crosemont.dti.g66.service_gestion_stage.Services.ÉtudiantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Principal

@RestController
class ÉtudiantControleur(val service: ÉtudiantService) {

    @Operation(
        summary = "Obtenir la liste de tous les étudiants.",
        description = "Retourne la liste de tous les étudiants inscrits au service.",
        operationId = "obtenirÉtudiants",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste de tous les étudiants a été retournée.")
        ]
    )
    @GetMapping("/étudiants")
    fun obtenirÉtudiants() = service.obtenirÉtudiants()

    @Operation(
        summary = "Obtenir les informations d'un étudiant par son code.",
        description = "Retourne les informations d'un étudiant en le cherchant par son code.",
        operationId = "obtenirÉtudiantParCode",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'étudiant ont été trouvées."),
            ApiResponse(responseCode = "404", description = "Aucun étudiant n'a été trouvé avec le code spécifié."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié.")
        ]
    )
    @GetMapping("/étudiant")
    fun obtenirÉtudiantParCode(principal: Principal?): ResponseEntity<*> {
        if (principal != null) {
            val étudiant = service.chercherÉtudiantParCode(principal.name)
            return if (étudiant != null) {
                ResponseEntity.ok(étudiant)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("L'étudiant avec le code : ${principal.name} n'est pas inscrit au service")
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vous n'etes pas connecte")
    }


    @Operation(
        summary = "Obtenir le profil informatique d'un étudiant.",
        description = "Retourne le profil informatique d'un étudiant en le cherchant par son code.",
        operationId = "obtenirProfilParÉtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "Le profil informatique de l'étudiant a été trouvé."),
            ApiResponse(responseCode = "404", description = "Aucun étudiant n'a été trouvé avec le code spécifié.")
        ]
    )
    @GetMapping("/étudiants/profil")
    fun obtenirProfilParÉtudiant(principal: Principal): ProfilInformatique? {

        return service.obtenirProfilParÉtudiant(principal.name)
    }

    @Operation(
        summary = "Obtenir les candidatures associées à un étudiant.",
        description = "Retourne les candidatures associées à un étudiant en le cherchant par son code.",
        operationId = "obtenirCandidaturesParÉtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "Les candidatures associées à l'étudiant ont été trouvées."),
            ApiResponse(responseCode = "404", description = "Aucun étudiant n'a été trouvé avec le code spécifié.")
        ]
    )
    @GetMapping("/candidatures/{code_candidature}/étudiant")
    fun obtenirCandidaturesParÉtudiant(@PathVariable code_candidature: String) = service.obtenirÉtudiantParCandidature(code_candidature)

    @Operation(
        summary = "Obtenir les documents d'un étudiant.",
        description = "Retourne les documents associés à un étudiant en le cherchant par son code.",
        operationId = "obtenirDocumentsParÉtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "Les documents associés à l'étudiant ont été trouvés."),
            ApiResponse(responseCode = "404", description = "Aucun étudiant n'a été trouvé avec le code spécifié.")
        ]
    )
    @GetMapping("/étudiants/documents")
    fun obtenirDocumentsParÉtudiant(principal: Principal): List<Document> {

        return service.obtenirDocumentsParÉtudiant(principal.name)
    }

    @Operation(
        summary = "Inscrire un nouvel étudiant.",
        description = "Inscrit un nouvel étudiant au service.",
        operationId = "inscrireÉtudiant",
        responses = [
            ApiResponse(responseCode = "201", description = "Le nouvel étudiant a été inscrit avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de l'inscription de l'étudiant."),
        ]
    )
    @PostMapping("/étudiants")
    fun inscrireÉtudiant(@RequestBody étudiant: Étudiant): ResponseEntity<Étudiant> {

        val nouveauÉtudiant = service.ajouterÉtudiant(étudiant)

        if (nouveauÉtudiant != null){
            val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{code}").buildAndExpand(nouveauÉtudiant.codeUtilisateur).toUri()
            return ResponseEntity.created(uri).body(nouveauÉtudiant)
        }

        return ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Modifier les informations d'un étudiant.",
        description = "Modifie les informations d'un étudiant existant.",
        operationId = "modifierÉtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations de l'étudiant ont été modifiées."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec les informations modifiées."),
            ApiResponse(responseCode = "404", description = "L'étudiant à modifier n'a pas été trouvé."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(l'etudiant connecté ne peut modifier que son profil a lui.")
        ]
    )
    @PutMapping("/étudiants")
    fun modifierÉtudiant(principal: Principal, @RequestBody étudiant: Étudiant): ResponseEntity<Étudiant> {

        val étudiantÀmodifier = service.modifierÉtudiant(principal.name,étudiant)

        if (étudiantÀmodifier != null) {
            val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{code_etudiant}").buildAndExpand(étudiantÀmodifier.codeUtilisateur).toUri()
            return ResponseEntity.created(uri).body(étudiantÀmodifier)
        }
        return ResponseEntity.ok(étudiant)
    }

    @Operation(
        summary = "Supprimer un étudiant.",
        description = "Supprime un étudiant par son code.",
        operationId = "supprimerÉtudiant",
        responses = [
            ApiResponse(responseCode = "204", description = "L'étudiant a été supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "L'étudiant à supprimer n'a pas été trouvé.")
        ]
    )
    @DeleteMapping("/étudiants")
    fun supprimerÉtudiant(principal: Principal): ResponseEntity<Étudiant> {

        service.supprimerÉtudiant(principal.name)
        return ResponseEntity.noContent().build()
    }
}