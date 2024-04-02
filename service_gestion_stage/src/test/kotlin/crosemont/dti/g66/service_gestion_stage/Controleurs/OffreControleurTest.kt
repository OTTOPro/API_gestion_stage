package crosemont.dti.g66.service_gestion_stage.Controleurs

import com.fasterxml.jackson.databind.ObjectMapper
import crosemont.dti.g66.service_gestion_stage.Modèle.Enum.ModeEmploi
import crosemont.dti.g66.service_gestion_stage.Modèle.Offre
import crosemont.dti.g66.service_gestion_stage.Services.OffreService
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
class OffreControleurTest {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    lateinit var service: OffreService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    // @GetMapping("/offres")
    fun `Étant donné un utilisateur non-authentifié, lorsque l'utilisateur effectue une requête GET pour obtenir la liste des offres de stage, alors il obtient la liste des offres et un code de retour 200` (){
        Mockito.`when`(service.obtenirOffres()).thenReturn(SourceDonnéesTests.offres)

        mockMvc.perform(MockMvcRequestBuilders.get("/offres"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]['idOffre']").value("1"))
    }

    @Test
    // @GetMapping("/offres/{code}")
    fun `Étant donné un utilisateur non-authentifié et le code d'une offre de stage existante, lorsque l'utilisateur effectue une requête GET pour obtenir une offre de stage par code, alors il obtient un JSON qui contient une offre de stage avec le code correspondant et un code de retour 200`() {
        Mockito.`when`(service.chercherOffresParCode("1")).thenReturn(SourceDonnéesTests.offres[0])

        mockMvc.perform(MockMvcRequestBuilders.get("/offres/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.idOffre").value("1"))
    }


    @Test
    //@PutMapping("/offres/{code_offre}")
    fun `Étant donné un utilisateur non-authentifié, lorsque l'utilisateur non authentifié effectue une requête PUT pour modifier une offre de stage, alors il obtient un code de retour 401` (){
        var offre = Offre(
            idOffre = 1,
            employeur = SourceDonnéesTests.employeurs[0],
            titrePoste = "Développeur Android",
            modeEmploi = ModeEmploi.PRÉSENTIEL,
            description = "Description de l'offre 1",
            candidatures = mutableListOf()
        )
        mockMvc.perform(
            MockMvcRequestBuilders.put("/offres/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(offre)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    //@PostMapping("/employeur/offres")
    fun `Étant donné un utilisateur non-authentifié, lorsque l'utilisateur effectue une requête POST pour créer une nouvelle offre de stage, alors il obtient un code de retour 401` (){
        var offre = Offre(
            idOffre = 1,
            employeur = SourceDonnéesTests.employeurs[0],
            titrePoste = "Développeur Android",
            modeEmploi = ModeEmploi.PRÉSENTIEL,
            description = "Description de l'offre 1",
            candidatures = mutableListOf()
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post("/employeur/offres").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(offre)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @WithMockUser("auth0|Jean")
    @Test
    //@PostMapping("/employeur/offres")
    fun `Étant donné un employeur authentifié, lorsque l'utilisateur effectue une requête POST pour créer une nouvelle offre de stage, alors il obtient un JSON qui contient une offre avec un code, un code de retour 201 et l'URI de la ressource ajoutée` (){
        var offre = Offre(
            idOffre = 3,
            employeur = null ,
            titrePoste = "Développeur Android",
            modeEmploi = ModeEmploi.PRÉSENTIEL,
            description = "Description de l'offre 3"
        )
        Mockito.`when`(service.creerUneOffre("auth0|Jean", offre)).thenReturn(offre)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/employeur/offres").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(offre)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(
                MockMvcResultMatchers.header().string("Location", CoreMatchers.containsString("/offres/3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.idOffre").value("3"))

        Mockito.verify(service).creerUneOffre("auth0|Jean", offre)
    }

    @WithMockUser("auth0|Jean")
    @Test
    //@PutMapping("/offres/{code_offre}")
    fun `Étant donné un employeur authentifié, lorsque l'utilisateur effectue une requête PUT pour modifier une offre de stage existante, alors il obtient un JSON qui contient l'offre modifiée, un code de retour 200` (){
        val offre = Offre(
            idOffre = 3,
            employeur = null,
            titrePoste = "Développeur Android",
            modeEmploi = ModeEmploi.PRÉSENTIEL,
            description = "Description de l'offre 3"
        )

        Mockito.`when`(service.modifierUneOffre("3", offre, "auth0|Jean")).thenReturn(offre)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/offres/3").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(offre)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.idOffre").value("3"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.titrePoste").value("Développeur Android"))

        Mockito.verify(service).modifierUneOffre("3", offre, "auth0|Jean")
    }

}