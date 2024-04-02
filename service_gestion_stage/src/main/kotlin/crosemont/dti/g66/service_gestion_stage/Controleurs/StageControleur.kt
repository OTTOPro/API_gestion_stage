package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Modèle.Entreprise
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.StageProgression
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import crosemont.dti.g66.service_gestion_stage.Modèle.Stage
import crosemont.dti.g66.service_gestion_stage.Services.StageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.security.Principal

@RestController
class StageControleur(val service: StageService) {

    @Operation(
        summary = "Obtenir la liste de tous les stages.",
        description = "Retourne la liste de tous les stages enregistrés.",
        operationId = "obtenirStages",
        responses = [
            ApiResponse(responseCode = "200", description = "La liste de tous les stages a été retournée.")
        ]
    )
    @GetMapping("/stages")
    fun obtenirStages() = service.obtenirStages()

    @Operation(
        summary = "Obtenir le stage d'un étudiant.",
        description = "Retourne les informations du stage associé à un étudiant.",
        operationId = "obtenirStageParÉtudiant",
        responses = [
            ApiResponse(responseCode = "200", description = "Les informations du stage associé à l'étudiant ont été trouvées."),
            ApiResponse(responseCode = "404", description = "Aucun stage n'est associé à l'étudiant."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(l'etudiant connecté ne peut voir que le stage qui lui appartient)")
        ]
    )
    @GetMapping("/etudiants/stage")
    fun obtenirStageParÉtudiant(principal: Principal) = service.obtenirStageParÉtudiant(principal.name)

    @Operation(
        summary = "Obtenir les stages d'un superviseur.",
        description = "Retourne les informations des stages associés à un superviseur.",
        operationId = "obtenirStageParSuperviseur",
        responses = [
            ApiResponse(responseCode = "501", description = "Cette fonctionnalité n'est pas implémentée.")
        ]
    )
    @GetMapping("/professeurs/{code_supérviseur}/stage")
    fun obtenirStageParSupérviseur(@PathVariable code_supérviseur: String): ResponseEntity<Entreprise> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)


    @Operation(
        summary = "Créer un nouveau stage lié à une candidature.",
        description = "Crée un nouveau stage lié à une candidature enregistrée dans le service.",
        operationId = "créerStage",
        responses = [
            ApiResponse(responseCode = "201", description = "Le nouveau stage a été créé avec succès."),
            ApiResponse(responseCode = "500", description = "Une erreur interne est survenue lors de la création du stage."),
            ApiResponse(responseCode = "401", description = "L'utilisateur voulant effectuer l'opération n'est pas correctement authentifié."),
            ApiResponse(responseCode = "403", description = "L'utilisateur voulant effectuer l'opération n'a pas les droits nécessaires.(Seule les employes qui peuvent creer des stages a des candidatures qui appartiennent a leur entreprise.)")
        ]
    )
    @PostMapping("/candidatures/{code_candidature}/stages")
    fun créerStage(principal: Principal, @RequestBody stage: Stage, @PathVariable code_candidature: String): ResponseEntity<Stage> {


        val nouvelleStage = service.créerUnStage( code_candidature, principal.name, stage )

        if (nouvelleStage != null ) {
            val uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(nouvelleStage.idStage)
                .toUri()

            return ResponseEntity.created(uri).body(nouvelleStage)
        }

        return ResponseEntity.internalServerError().build()

    }

    @PostMapping("/candidatures/{code_candidature}/stage")
    fun confirmerStage(@PathVariable code_candidature: String, @RequestBody stage: Stage): ResponseEntity<Stage> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

    @Operation(
        summary = "Modifier la progression d'un stage.",
        description = "Modifie la progression d'un stage existant.",
        operationId = "modifierProgressionStage",
        responses = [
            ApiResponse(responseCode = "200", description = "La progression du stage a été modifiée."),
            ApiResponse(responseCode = "201", description = "Une nouvelle ressource a été créée avec la progression modifiée."),
            ApiResponse(responseCode = "404", description = "Le stage à modifier n'a pas été trouvé.")
        ]
    )
    @PutMapping("/stages/{code}/{progression}")
    fun modifierProgressionStage(@PathVariable code_etudiant: String, @PathVariable progression: StageProgression, @RequestBody stage: Stage): ResponseEntity<Stage> {

        val progressionStage = service.modifierProgressionStage(code_etudiant, stage)

        if (progressionStage != null) {
            val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{code_etudiant}").buildAndExpand(stage.stageProgression).toUri()
            return ResponseEntity.created(uri).body(progressionStage)
        }

        return ResponseEntity.ok(stage)
    }

    @Operation(
        summary = "Affecter un superviseur a un stage d'un etudiant..",
        description = "Ajoute un superviseur au stage de l'etudiant.",
        operationId = "affecterSuperviseur",
        responses = [
            ApiResponse(responseCode = "501", description = "Cette fonctionnalité n'est pas implémentée.")
        ]
    )
    @PutMapping("/stages/{code}/professeurs/{code_supérviseur}")
    fun affecterSuperviseur(@PathVariable code: String, @PathVariable code_supérviseur: String, @RequestBody stage: Stage): ResponseEntity<Stage> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

}