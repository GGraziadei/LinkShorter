package it.erroridiprezzo.ErroriDiPrezzoShort.forms;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddPageForm {
    private String title;
    private MultipartFile file;
}
