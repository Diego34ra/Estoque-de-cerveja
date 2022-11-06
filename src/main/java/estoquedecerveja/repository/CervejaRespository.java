package estoquedecerveja.repository;

import estoquedecerveja.model.Cerveja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CervejaRespository extends JpaRepository<Cerveja, Long> {

    Optional<Cerveja> findByNome(String nome);
}
