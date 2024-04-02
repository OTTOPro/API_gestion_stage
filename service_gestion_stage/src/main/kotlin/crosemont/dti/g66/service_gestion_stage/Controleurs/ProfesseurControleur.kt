package crosemont.dti.g66.service_gestion_stage.Controleurs

import crosemont.dti.g66.service_gestion_stage.Modèle.Professeur
import crosemont.dti.g66.service_gestion_stage.Services.ProfesseurService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProfesseurControleur(val service: ProfesseurService) {

    @GetMapping("/professeurs")
    fun obtenirProfesseurs(): ResponseEntity<Professeur> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

    @GetMapping("/professeurs/{role}")
    fun obtenirProfesseurParRole(@PathVariable role: String): ResponseEntity<Professeur> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

    @PostMapping("/professeurs")
    fun inscrireProfesseur(@RequestBody professeur: Professeur): ResponseEntity<Professeur> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

    @PutMapping("/professeurs/{code}")
    fun modifierProfesseur(@PathVariable code: String, @RequestBody professeur: Professeur): ResponseEntity<Professeur> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

    @DeleteMapping("/professeurs/{code}")
    fun supprimerProfesseur(@PathVariable code: String): ResponseEntity<Professeur> = ResponseEntity(
        HttpStatus.NOT_IMPLEMENTED)

}