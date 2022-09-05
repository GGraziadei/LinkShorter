package it.erroridiprezzo.ErroriDiPrezzoShort.controllers;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Page;
import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Url;
import it.erroridiprezzo.ErroriDiPrezzoShort.entities.UrlStat;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.PageRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlStatRepository urlStatRepository;

    @Autowired
    private PageRepository pageRepository;

    @Value("${user.delayTime}")
    private int delayTime;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/{key}")
    @ResponseBody
    public void get(@PathVariable(name = "key") String key , HttpServletRequest request , HttpServletResponse response) throws Exception {
        Optional<Url> optionalUrl = this.urlRepository.findById(key);
        if(optionalUrl.isEmpty() ) {
            response.sendError(500 , "Url not found in our database");
            return;
        }
        Url url = optionalUrl.get();
        url.setClick( url.getClick()!=null?url.getClick()+ 1:1 );
        this.urlRepository.save(url);
        UrlStat urlStat = UrlStat.builder()
                .url(url.getUrl())
                .ip(request.getRemoteAddr())
                .userAgent(request.getHeader("USER-AGENT"))
                .build();
        this.urlStatRepository.save(urlStat);

        Page page = this.pageRepository.findByEnable(true);

        String content = Files.readString(Path.of(page.getBodyPath()));

        Context context = new Context();
        context.setLocale(Locale.ITALY);
        context.setVariable("title" , page.getTitle() );
        context.setVariable("body" , content );
        context.setVariable("url" , url.getUrl());
        context.setVariable("delayTime" , this.delayTime );
        String html = templateEngine.process("index", context);

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(html);
        response.getWriter().flush();
    }

    @ResponseBody
    @GetMapping
    public void getIndex( HttpServletRequest request , HttpServletResponse response) throws Exception {
        Page page = this.pageRepository.findByEnable(true);
        String content = Files.readString(Path.of(page.getBodyPath()));
        Context context = new Context();
        context.setLocale(Locale.ITALY);
        context.setVariable("title" , page.getTitle() );
        context.setVariable("body" , content );
        context.setVariable("url" , "#hosted-by-goodgamegroup.it");
        context.setVariable("delayTime" , this.delayTime );
        String html = templateEngine.process("index", context);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(html);
        response.getWriter().flush();
    }


}
