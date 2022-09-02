package it.erroridiprezzo.ErroriDiPrezzoShort.repositories;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url , String > {

    Url findByUrl(String url);

}
