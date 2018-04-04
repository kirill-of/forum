package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.ofitserov.forumtest1.entity.Section;
import pro.ofitserov.forumtest1.repository.SectionRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;
import pro.ofitserov.forumtest1.util.ForumConstants;

@Controller
@RequestMapping("/section")
public class SectionController {


    private SectionRepository sectionRepository;
    private TopicRepository topicRepository;

    @Autowired
    public SectionController(SectionRepository sectionRepository, TopicRepository topicRepository) {
        this.sectionRepository = sectionRepository;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, ModelMap model, @PageableDefault(sort = {"dateOfPublication"}, value = ForumConstants.PAGE_DEFAULT_SIZE, direction = Sort.Direction.DESC) Pageable pageable) {

        if (!sectionRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }

        Section section = sectionRepository.findOne(id);

        model.addAttribute("title", section.getTitle());
        model.addAttribute("section", section);
        model.addAttribute("topics", topicRepository.findBySection(section, pageable));
        return "section/view";
    }

    @GetMapping("/")
    public String redirect() {
        return "redirect:/";
    }
}
