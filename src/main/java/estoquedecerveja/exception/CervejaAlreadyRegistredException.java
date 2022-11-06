package estoquedecerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CervejaAlreadyRegistredException extends Exception{

    public CervejaAlreadyRegistredException(String cervejaNome) {
        super(String.format("Beer with name %s already registered in the system.", cervejaNome));
    }
}
