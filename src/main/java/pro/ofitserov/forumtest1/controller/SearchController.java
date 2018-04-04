package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.ofitserov.forumtest1.repository.ReplyRepository;
import pro.ofitserov.forumtest1.repository.SectionRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.util.ForumConstants;

@Controller
@PreAuthorize("isAuthenticated()")
public class SearchController {

    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;
    private SectionRepository sectionRepository;

    @Autowired
    public SearchController(TopicRepository topicRepository, ReplyRepository replyRepository, SectionRepository sectionRepository) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/search")
    public String search(ModelMap model) {
        model.addAttribute("title", "Search");
        return "search/search";
    }

    @PostMapping("/search")
    public String searchResult(@RequestParam("search_word") String searchWord, @RequestParam("search_in") String searchIn, ModelMap model, @PageableDefault(sort = {"dateOfPublication"}, value = ForumConstants.PAGE_DEFAULT_SIZE, direction = Sort.Direction.ASC) Pageable pageable) {
        model.addAttribute("title", "Search");

        switch (searchIn) {
            case ForumConstants.SEARCH_IN_SECTIONS:
                model.addAttribute("sections", sectionRepository.findByTitleContainingOrTextContaining(searchWord, searchWord, pageable));
                break;
            case ForumConstants.SEARCH_IN_TOPICS:
                model.addAttribute("topics", topicRepository.findByTitleContainingOrTextContaining(searchWord, searchWord, pageable));
                break;
            case ForumConstants.SEARCH_IN_REPLIES:
                model.addAttribute("replies", replyRepository.findByTextContaining(searchWord, pageable));
                break;
        }

        return "search/search";
    }
}
