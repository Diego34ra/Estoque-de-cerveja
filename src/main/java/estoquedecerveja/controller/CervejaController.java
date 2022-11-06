package estoquedecerveja.controller;

import estoquedecerveja.dto.CervejaDTO;
import estoquedecerveja.dto.QuantidadeDTO;
import estoquedecerveja.exception.CervejaAlreadyRegistredException;
import estoquedecerveja.exception.CervejaNotFoundException;
import estoquedecerveja.exception.EstoqueDeCervejaExcedidoException;
import estoquedecerveja.model.Cerveja;
import estoquedecerveja.service.CervejaService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor
public class CervejaController {

    @Autowired
    private CervejaService cervejaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO createBeer(@RequestBody @Valid CervejaDTO cervejaDTO) throws CervejaAlreadyRegistredException {
        return cervejaService.createCerveja(cervejaDTO);
    }

    @GetMapping("/{nome}")
    public CervejaDTO findByName(@PathVariable String nome) throws CervejaNotFoundException {
        return cervejaService.findByNome(nome);
    }

    @GetMapping
    public List<CervejaDTO> listBeers() {
        return cervejaService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws CervejaNotFoundException {
        cervejaService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public CervejaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws CervejaNotFoundException, EstoqueDeCervejaExcedidoException {
        return cervejaService.increment(id, quantidadeDTO.getQuantidade());
    }
}
