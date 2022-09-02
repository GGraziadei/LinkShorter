package it.erroridiprezzo.ErroriDiPrezzoShort.repositories;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page , Long> {

    Page findByEnable(boolean enable);

}
