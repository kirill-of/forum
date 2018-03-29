package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.ofitserov.forumtest1.entity.Photo;
import pro.ofitserov.forumtest1.repository.PhotoRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PhotoController {

    private PhotoRepository photoRepository;

    @Autowired
    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @GetMapping("/photo")
    public void showImage(@RequestParam("id") Long imageId, HttpServletResponse response) throws IOException {

        Photo photo = photoRepository.findOne(imageId);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(photo.getPhoto());
        response.getOutputStream().close();
    }
}
