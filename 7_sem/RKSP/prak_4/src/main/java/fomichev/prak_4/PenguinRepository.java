package fomichev.prak_4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenguinRepository extends JpaRepository<Penguin, Long> {
    Penguin findPenguinById(Long id);
}
