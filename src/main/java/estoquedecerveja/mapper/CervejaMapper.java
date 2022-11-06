package estoquedecerveja.mapper;

import estoquedecerveja.dto.CervejaDTO;
import estoquedecerveja.model.Cerveja;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CervejaMapper {


    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

//    public Cerveja toCervejaCreate(PessoaCreateDTO dto) {
//        return MODEL_MAPPER.map(dto, Pessoa.class);
//    }

    public CervejaDTO toCervejaDTO (Cerveja cerveja) {
        return MODEL_MAPPER.map(cerveja,CervejaDTO.class);
    }

    public List<CervejaDTO> toCervejaDTOList(List<Cerveja> cervejaList) {
        return cervejaList.stream().map(this::toCervejaDTO).collect(Collectors.toList());

    }
    public Cerveja toCerveja(CervejaDTO cervejaDTO) {
        return MODEL_MAPPER.map(cervejaDTO,Cerveja.class);
    }
}
