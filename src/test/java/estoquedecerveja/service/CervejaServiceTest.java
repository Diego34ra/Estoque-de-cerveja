package estoquedecerveja.service;

import estoquedecerveja.dto.CervejaDTO;
import estoquedecerveja.exception.CervejaAlreadyRegistredException;
import estoquedecerveja.exception.CervejaNotFoundException;
import estoquedecerveja.exception.EstoqueDeCervejaExcedidoException;
import estoquedecerveja.mapper.CervejaMapper;
import estoquedecerveja.model.Cerveja;
import estoquedecerveja.repository.CervejaRespository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@AllArgsConstructor
public class CervejaServiceTest {


    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private final CervejaRespository cervejaRespository;

    @Autowired
    private final CervejaMapper cervejaMapper;

    @InjectMocks
    private final CervejaService cervejaService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws CervejaAlreadyRegistredException {
        // given
        CervejaDTO expectedBeerDTO = CervejaDTO.builder().build();
        Cerveja expectedSavedBeer = cervejaMapper.toCerveja(expectedBeerDTO);

        // when
        when(cervejaRespository.findByNome(expectedBeerDTO.getNome())).thenReturn(empty());
        when(cervejaRespository.save(expectedSavedBeer)).thenReturn(expectedSavedBeer);

        //then
        CervejaDTO createdBeerDTO = cervejaService.createCerveja(expectedBeerDTO);

//        assertThat(createdBeerDTO.getId(), is(equalTo(Integer.valueOf(expectedBeerDTO.getId())));
//        assertThat(createdBeerDTO.getNome(), is(equalTo(Integer.valueOf(expectedBeerDTO.getNome()))));
//        assertThat(createdBeerDTO.getQuantidade(), is(equalTo(expectedBeerDTO.getQuantidade())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        // given
        CervejaDTO cervejaDTO = CervejaDTO.builder().build();
        Cerveja duplicatedBeer = cervejaMapper.toCerveja(cervejaDTO);

        // when
        when(cervejaRespository.findByNome(cervejaDTO.getNome())).thenReturn(Optional.of(duplicatedBeer));

        // then
        assertThrows(CervejaAlreadyRegistredException.class, () -> cervejaService.createCerveja(cervejaDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws CervejaNotFoundException {
        // given
        CervejaDTO cervejaDTO = CervejaDTO.builder().build();
        Cerveja expectedFoundBeer = cervejaMapper.toCerveja(cervejaDTO);

        // when
        when(cervejaRespository.findByNome(expectedFoundBeer.getNome())).thenReturn(Optional.of(expectedFoundBeer));

        // then
        CervejaDTO foundBeerDTO = cervejaService.findByNome(cervejaDTO.getNome());

        //assertThat(foundBeerDTO, is(equalTo(cervejaDTO)));
    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        // given
        CervejaDTO cervejaDTO = CervejaDTO.builder().build();

        // when
        when(cervejaRespository.findByNome(cervejaDTO.getNome())).thenReturn(empty());

        // then
        assertThrows(CervejaNotFoundException.class, () -> cervejaService.findByNome(cervejaDTO.getNome()));
    }

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        // given
        CervejaDTO cervejaDTO = CervejaDTO.builder().build();
        Cerveja expectedFoundBeer = cervejaMapper.toCerveja(cervejaDTO);

        //when
        when(cervejaRespository.findAll()).thenReturn(Collections.singletonList(expectedFoundBeer));

        //then
        List<CervejaDTO> foundListBeersDTO = cervejaService.listAll();

//        assertThat(foundListBeersDTO, is(not(empty())));
//        assertThat(foundListBeersDTO.get(0), is(equalTo(cervejaDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        //when
        when(cervejaRespository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<CervejaDTO> foundListBeersDTO = cervejaService.listAll();

//        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws CervejaNotFoundException{
        // given
        CervejaDTO cervejaDTODelet = CervejaDTO.builder().build();
        Cerveja expectedDeletedBeer = cervejaMapper.toCerveja(cervejaDTODelet);

        // when
        when(cervejaRespository.findById(cervejaDTODelet.getId())).thenReturn(Optional.of(expectedDeletedBeer));
        doNothing().when(cervejaRespository).deleteById(cervejaDTODelet.getId());

        // then
        cervejaService.deleteById(cervejaDTODelet.getId());

//        verify(cervejaRespository, times(1)).findById(cervejaDTODelet.getId());
//        verify(cervejaRespository, times(1)).deleteById(cervejaDTODelet.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws CervejaNotFoundException, EstoqueDeCervejaExcedidoException {
        //given
        CervejaDTO expectedBeerDTO = CervejaDTO.builder().build();
        Cerveja expectedBeer = cervejaMapper.toCerveja(expectedBeerDTO);

        //when
        when(cervejaRespository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
        when(cervejaRespository.save(expectedBeer)).thenReturn(expectedBeer);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedBeerDTO.getQuantidade() + quantityToIncrement;

        // then
        CervejaDTO incrementedBeerDTO = cervejaService.increment(expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedBeerDTO.getQuantidade()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        CervejaDTO expectedBeerDTO = CervejaDTO.builder().build();
        Cerveja expectedBeer = cervejaMapper.toCerveja(expectedBeerDTO);

        when(cervejaRespository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 80;
        assertThrows(EstoqueDeCervejaExcedidoException.class, () -> cervejaService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        CervejaDTO expectedBeerDTO = CervejaDTO.builder().build();
        Cerveja expectedBeer = cervejaMapper.toCerveja(expectedBeerDTO);

        when(cervejaRespository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 45;
        assertThrows(EstoqueDeCervejaExcedidoException.class, () -> cervejaService.increment(expectedBeerDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(cervejaRespository.findById(INVALID_BEER_ID)).thenReturn(empty());

        assertThrows(CervejaNotFoundException.class, () -> cervejaService.increment(INVALID_BEER_ID, quantityToIncrement));
    }
//
//    @Test
//    void whenDecrementIsCalledThenDecrementBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws BeerNotFoundException, BeerStockExceededException {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        BeerDTO incrementedBeerDTO = beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
//        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);
//
//        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//
//        int quantityToDecrement = 80;
//        assertThrows(BeerStockExceededException.class, () -> beerService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(BeerNotFoundException.class, () -> beerService.decrement(INVALID_BEER_ID, quantityToDecrement));
//    }
}
