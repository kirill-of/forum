package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;
import pro.ofitserov.forumtest1.repository.ReplyRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;
import pro.ofitserov.forumtest1.service.UserService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/topic")
public class TopicController {

    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;

    @Autowired
    private UserService userService;

    public TopicController(TopicRepository topicRepository, ReplyRepository replyRepository) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(ModelMap model) {
        model.addAttribute("title", "Add topic");
        model.addAttribute("topic", new Topic());
        return "topic/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(@Valid Topic topic, BindingResult result) {

        if (result.hasErrors()) {
            return "topic/add";
        }

        topic.setUser(userService.getCurrentUser());
        topic.setDateOfPublication(new Date());

        topicRepository.save(topic);

        return "redirect:/topic/" + topic.getId();
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, ModelMap model) {
        Topic topic = topicRepository.findOne(id);

        if (topic == null) {
            throw new ResourceNotFoundException();
        }

        List<Reply> replies = replyRepository.findByTopic(topic);

        model.addAttribute("title", topic.getTitle());
        model.addAttribute("topic", topic);
        model.addAttribute("replies", replies);
        return "topic/view";
    }
}
