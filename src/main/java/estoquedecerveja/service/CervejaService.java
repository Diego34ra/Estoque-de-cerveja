package estoquedecerveja.service;

import estoquedecerveja.dto.CervejaDTO;
import estoquedecerveja.exception.CervejaAlreadyRegistredException;
import estoquedecerveja.exception.CervejaNotFoundException;
import estoquedecerveja.exception.EstoqueDeCervejaExcedidoException;
import estoquedecerveja.mapper.CervejaMapper;
import estoquedecerveja.model.Cerveja;
import estoquedecerveja.repository.CervejaRespository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CervejaService {

    @Autowired
    private final CervejaMapper cervejaMapper;

    @Autowired
    private final CervejaRespository cervejaRespository;

    public CervejaDTO createCerveja(CervejaDTO cervejaDTO) throws CervejaAlreadyRegistredException {
        verifyIfIsAlreadyRegistered(cervejaDTO.getNome());
        Cerveja cerveja = cervejaMapper.toCerveja(cervejaDTO);
        Cerveja savedCerveja = cervejaRespository.save(cerveja);
        return cervejaMapper.toCervejaDTO(savedCerveja);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws CervejaAlreadyRegistredException {
        Optional<Cerveja> optSavedCerveja = cervejaRespository.findByNome(name);
        if (optSavedCerveja.isPresent()) {
            throw new CervejaAlreadyRegistredException(name);
        }
    }

    public CervejaDTO findByNome(String name) throws CervejaNotFoundException {
        Cerveja cerveja = cervejaRespository.findByNome(name)
                .orElseThrow(() -> new CervejaNotFoundException(name));
        return cervejaMapper.toCervejaDTO(cerveja);
    }

    public List<CervejaDTO> listAll() {
        return cervejaRespository.findAll()
                .stream()
                .map(cervejaMapper::toCervejaDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws CervejaNotFoundException {
        verifyIfExists(id);
        cervejaRespository.deleteById(id);
    }

    private Cerveja verifyIfExists(Long id) throws CervejaNotFoundException {
        return cervejaRespository.findById(id)
                .orElseThrow(() -> new CervejaNotFoundException(id));
    }

    public CervejaDTO increment(Long id, int quantidadeParaIncrementar) throws CervejaNotFoundException, EstoqueDeCervejaExcedidoException {
        Cerveja cerveja = verifyIfExists(id);
        int quantidadeTotal = quantidadeParaIncrementar + cerveja.getQuantidade();
        if (quantidadeTotal <= cerveja.getMax()) {
            cerveja.setQuantidade(cerveja.getQuantidade() + quantidadeParaIncrementar);
            Cerveja estoqueAdicionado = cervejaRespository.save(cerveja);
            return cervejaMapper.toCervejaDTO(estoqueAdicionado);
        }
        throw new EstoqueDeCervejaExcedidoException(id, quantidadeParaIncrementar);
    }
}
