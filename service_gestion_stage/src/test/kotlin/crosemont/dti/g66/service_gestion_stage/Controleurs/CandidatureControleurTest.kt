package crosemont.dti.g66.service_gestion_stage.Controleurs

import com.fasterxml.jackson.databind.ObjectMapper
import crosemont.dti.g66.service_gestion_stage.Modèle.Candidature
import crosemont.dti.g66.service_gestion_stage.Services.CandidatureService
import crosemont.dti.g66.service_gestion_stage.SourceDonnéesTests
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class CandidatureControleurTest {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    lateinit var service: CandidatureService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @WithMockUser("auth0|Jean")
    //@GetMapping("/offres/{code_offre}/candidatures")
    fun `Étant donné un employeur authentifié, lorsque l'utilisateur effectue une requête GET pour obtenir les candidatures pour une offre de stage, alors il obtient la liste des candidatures et un code de retour 200` () {
        Mockito.`when`(service.chercherCadidatureParOffre("auth0|Jean", "1")).thenReturn(SourceDonnéesTests.candidatures)

        mockMvc.perform(MockMvcRequestBuilders.get("/offres/1/candidatures"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]['codeCandidature']").value("1"))
    }

    @Test
    @WithMockUser("auth0|Othmane")
    //@GetMapping("/etudiant/candidatures")
    fun `Étant donné un etudiant authentifié, lorsque l'utilisateur effectue une requête GET pour obtenir les candidatures pour qu'il a effectue, alors il obtient la liste des candidatures et un code de retour 200` () {
        Mockito.`when`(service.obtenirCandidaturesParÉtudiant("auth0|Othmane")).thenReturn(SourceDonnéesTests.candidatures)

        mockMvc.perform(MockMvcRequestBuilders.get("/etudiant/candidatures"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]['codeCandidature']").value("1"))
    }

    @Test
    //@PutMapping("/candidatures/{code}")
    fun `Étant donné un utilisateur non-authentifié, lorsque l'utilisateur non authentifié effectue une requête PUT pour modifier une candidature, alors il obtient un code de retour 401` (){
        val candidature = SourceDonnéesTests.candidatures[0]
        mockMvc.perform(
            MockMvcRequestBuilders.put("/candidatures/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(candidature)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    //@PostMapping("/offres/{code_offre}/candidatures")
    fun `Étant donné un utilisateur non-authentifié, lorsque l'utilisateur effectue une requête POST pour créer une nouvelle candidature à une offre de stage, alors il obtient un code de retour 401` (){
        val candidature = Candidature(
            codeCandidature = 3,
            null,
            null
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/offres/1/candidatures").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(candidature)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    @WithMockUser("auth0|Othmane")
    //@PostMapping("/offres/{code_offre}/candidatures")
    fun `Étant donné un étudiant authentifié, lorsque l'utilisateur effectue une requête POST pour postuler à une offre de stage, alors il obtient un JSON qui contient une candidature avec un code, un code de retour 201 et l'URI de la ressource ajoutée` (){
        val candidature = Candidature(
            codeCandidature = 3,
            null,
            null
        )

        Mockito.`when`(service.postulerAUneOffre("auth0|Othmane", "1", candidature)).thenReturn(candidature)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/offres/1/candidatures").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(candidature)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.header().string("Location", CoreMatchers.containsString("/candidatures/3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.codeCandidature").value("3"))
        Mockito.verify(service).postulerAUneOffre("auth0|Othmane", "1", candidature)
    }


    @Test
    @WithMockUser("auth0|Jean")
    //@PutMapping("/candidatures/{code}")
    fun `Étant donné un employeur authentifié, lorsque l'utilisateur effectue une requête PUT pour modifier l'état d'une candidature, alors il obtient un JSON qui contient la candidature modifiée et un code de retour 200` () {
        val candidature = SourceDonnéesTests.candidatures[0]

        Mockito.`when`(service.modifierEtatOffre("auth0|Jean", "1", candidature)).thenReturn(candidature)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/candidatures/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(candidature)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.codeCandidature").value("1"))
    }

    @Test
    @WithMockUser("auth0|Othmane")
    //@DeleteMapping("/candidatures/{code}")
    fun `Étant donné un employeur authentifié, lorsque l'utilisateur effectue une requête DELETE pour supprimer une candidature, alors il obtient un code de retour 204` () {
        Mockito.doNothing().`when`(service).supprimerCandidature("1", "auth0|Othmane")

        mockMvc.perform(MockMvcRequestBuilders.delete("/candidatures/1").with(csrf()))
            .andExpect(MockMvcResultMatchers.status().isNoContent())
    }

}
