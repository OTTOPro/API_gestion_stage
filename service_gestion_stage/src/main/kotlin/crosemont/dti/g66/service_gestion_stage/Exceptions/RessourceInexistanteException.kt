package crosemont.dti.g66.service_gestion_stage.Exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class RessourceInexistanteException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause) {
}