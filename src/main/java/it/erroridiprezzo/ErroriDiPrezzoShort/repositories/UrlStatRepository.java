package it.erroridiprezzo.ErroriDiPrezzoShort.repositories;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.UrlStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlStatRepository extends JpaRepository<UrlStat , Long> {
    List<UrlStat> findByUrl(String url);

}
