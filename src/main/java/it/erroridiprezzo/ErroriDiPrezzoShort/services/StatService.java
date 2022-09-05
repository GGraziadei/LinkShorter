package it.erroridiprezzo.ErroriDiPrezzoShort.services;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.UrlStat;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatService {

    @Autowired
    private UrlStatRepository urlStatRepository;

    public Map<LocalDate, Integer > clickPerDate() {
        Map<LocalDate , Integer >  localDateListMap = new TreeMap<>();
        for(UrlStat urlStat : this.urlStatRepository.findAll() ){
            LocalDate localDate = LocalDate.from(urlStat.getTs());
            if( ! localDateListMap.containsKey(localDate)){
                localDateListMap.put(localDate, 0 );
            }
            localDateListMap.put(localDate , localDateListMap.get(localDate)  + 1 ) ;
        }

        return  localDateListMap;
    }

    public Map<LocalDate, Integer > clickPerDatePerUrl(String url ){

        Map<LocalDate , List<UrlStat>>  localDateListMap = new HashMap<>();
        for(UrlStat urlStat : this.urlStatRepository.findAll() ){

            if ( ! urlStat.getUrl().equals(url)){ continue; }

            LocalDate localDate = LocalDate.from(urlStat.getTs());
            if( ! localDateListMap.containsKey(localDate)){
                localDateListMap.put(localDate, new LinkedList<>());
            }
            localDateListMap.get(localDate).add(urlStat);
        }
        return  localDateListMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e ->  e.getValue().size()));

    }

}
