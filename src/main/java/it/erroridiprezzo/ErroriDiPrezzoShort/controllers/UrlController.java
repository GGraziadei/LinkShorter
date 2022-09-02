package it.erroridiprezzo.ErroriDiPrezzoShort.controllers;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Url;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.services.CheckUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/short")
public class UrlController {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private CheckUrlService checkUrlService;

    @PostMapping
    public Map<String, String > post(@RequestBody String url ) throws IOException {
        Map<String, String > result = new LinkedHashMap<>();
        String hash = null;
        if(this.urlRepository.findByUrl(url) == null  ){
            if (this.checkUrlService.checkUrl(url)){
                result.put("code" , String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
                result.put("hash" , null );
                result.put("message", "check verification failed");
                return result;
            }

            UUID uuid = UUID.randomUUID();
            String key = uuid.toString().toUpperCase().substring(0 , 5);
            Url urlObj = Url.builder()
                    .url(url)
                    .key(key)
                    .build();
            this.urlRepository.save(urlObj);
            hash =  urlObj.getKey();
        }else{
            hash =  this.urlRepository.findByUrl(url).getKey();
        }

        result.put("code" , String.valueOf(HttpStatus.OK));
        result.put("hash" , hash );
        return result;
    }

    @DeleteMapping("/{key}")
    public Url delete(@PathVariable(name = "key") String key){
        Url url = this.urlRepository.findById(key).orElseThrow();
        this.urlRepository.delete(url);
        return url;
    }

}
