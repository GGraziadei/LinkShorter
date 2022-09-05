package it.erroridiprezzo.ErroriDiPrezzoShort.controllers;

import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Page;
import it.erroridiprezzo.ErroriDiPrezzoShort.entities.Url;
import it.erroridiprezzo.ErroriDiPrezzoShort.entities.UrlStat;
import it.erroridiprezzo.ErroriDiPrezzoShort.forms.AddPageForm;
import it.erroridiprezzo.ErroriDiPrezzoShort.forms.AddShortLinkForm;
import it.erroridiprezzo.ErroriDiPrezzoShort.forms.SearchForm;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.PageRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.repositories.UrlStatRepository;
import it.erroridiprezzo.ErroriDiPrezzoShort.services.CheckUrlService;
import it.erroridiprezzo.ErroriDiPrezzoShort.services.ReportService;
import it.erroridiprezzo.ErroriDiPrezzoShort.services.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CheckUrlService checkUrlService;

    @Autowired
    private StatService statService;

    @Autowired
    private UrlStatRepository urlStatRepository;

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ModelAndView index(){
        ModelAndView mav =  new ModelAndView("admin/index"  );
        mav.addObject("searchForm" , new SearchForm());
        mav.addObject("STAT" , statService.clickPerDate() );
        mav.addObject("URL_STAT" , urlStatRepository.findAll().stream().sorted(Comparator.comparing(UrlStat::getTs).reversed()).collect(Collectors.toList()) );
        return mav;
    }

    @GetMapping("/short")
    public ModelAndView shortLink(){
        ModelAndView mav = new ModelAndView("admin/short");
        mav.addObject("URL" , urlRepository.findAll());
        mav.addObject("addShortLinkForm" , new AddShortLinkForm());
        mav.addObject("searchForm" , new SearchForm());
        return mav;
    }

    @GetMapping("/page")
    public ModelAndView page(){
        ModelAndView mav = new ModelAndView("admin/page");
        mav.addObject("PAGE" , this.pageRepository.findAll() );
        mav.addObject("addPageForm" , new AddPageForm() );
        mav.addObject("searchForm" , new SearchForm());
        return mav;
    }

    @RequestMapping(value = "/short/saveShortLink", method=RequestMethod.POST)
    public RedirectView saveShortLink(@ModelAttribute AddShortLinkForm addShortLinkForm) throws IOException {
        if(this.urlRepository.findByUrl(addShortLinkForm.getUrl()) == null  ){
            if (this.checkUrlService.checkUrl(addShortLinkForm.getUrl())){
                return new RedirectView("/admin/short");
            }
            UUID uuid = UUID.randomUUID();
            String key = uuid.toString().toUpperCase().substring(0 , 5);
            Url urlObj = Url.builder()
                    .url(addShortLinkForm.getUrl())
                    .key(key)
                    .build();
            System.out.println("New link saved: " + addShortLinkForm.getUrl() );
            this.urlRepository.save(urlObj);
        }
        return new RedirectView("/admin/short");
    }

    @RequestMapping(value = "/page/savePage", method=RequestMethod.POST)
    public RedirectView savePage(@ModelAttribute AddPageForm addPageForm) throws IOException {
        File file = new File("src/main/resources/pages/" + UUID.randomUUID());
        String path = file.getAbsolutePath();
        addPageForm.getFile().transferTo(Paths.get(path) );
        Page page = Page.builder()
                .title(addPageForm.getTitle())
                .enable(false)
                .bodyPath(file.getAbsolutePath())
                .build();
        this.pageRepository.save(page);
        return new RedirectView("/admin/page");
    }

    @PostMapping("/page/activate/{pageId}")
    public RedirectView activatePage(@PathVariable Long pageId) throws IOException {
        this.pageRepository.findAll().forEach( p -> {
            p.setEnable(false);
            this.pageRepository.save(p);
        });
        Page page = this.pageRepository.findById(pageId).orElseThrow();
        page.setEnable(true);
        this.pageRepository.save(page);
        return new RedirectView("/admin/page");
    }

    @GetMapping("/page/download/{pageId}")
    public ResponseEntity<Resource> pageDownload(@PathVariable Long pageId ) throws FileNotFoundException {
        Page page = this.pageRepository.findById(pageId).orElseThrow();
        File file = new File(page.getBodyPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute SearchForm searchForm ){
        ModelAndView mav = new ModelAndView("/admin/search");
        Url url ;
        List<Url> urls = new ArrayList<>();
        String search = searchForm.getSearch();
        if( search.length() > 5 ){
            url = this.urlRepository.findByUrl(search);
        }else{
            url = this.urlRepository.findById(search).orElse(new Url());
        }
        urls.add(url);
        List<UrlStat> urlStatList = this.urlStatRepository.findByUrl(url.getUrl());
        mav.addObject("URL" , urls);
        mav.addObject("URL_STAT" , urlStatList);
        mav.addObject("searchForm" , new SearchForm());
        return mav;
    }

    @GetMapping("/report/all/{days}")
    public ResponseEntity<Resource> reportAll(@PathVariable(name = "days") int days ) throws IOException {
        File file = new File(this.reportService.generateReport(days));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @GetMapping("/report/{urlId}/{days}")
    public ResponseEntity<Resource> reportUrl(@PathVariable(name = "days") int days , @PathVariable(name = "urlId") String urlId ) throws IOException {
        Url url = this.urlRepository.findById(urlId).orElseThrow();
        File file = new File(this.reportService.generateReport(days , url.getUrl() ));
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
