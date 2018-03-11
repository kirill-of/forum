package pro.ofitserov.forumtest1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.ofitserov.forumtest1.repository.TopicRepository;

@Controller
@RequestMapping("/")
public class IndexController {

    private TopicRepository topicRepository;

    public IndexController(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @GetMapping
    public String index(ModelMap model) {
        model.addAttribute("title", "Home page - My Forum");
        model.addAttribute("topics", topicRepository.findAll());
        return "index";
    }
}