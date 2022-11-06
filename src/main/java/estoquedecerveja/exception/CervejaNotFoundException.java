package estoquedecerveja.exception;

public class CervejaNotFoundException extends Exception{

    public CervejaNotFoundException(String beerName) {
        super(String.format("Beer with name %s not found in the system.", beerName));
    }

    public CervejaNotFoundException(Long id) {
        super(String.format("Beer with id %s not found in the system.", id));
    }
}
