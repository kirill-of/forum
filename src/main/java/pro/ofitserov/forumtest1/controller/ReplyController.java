package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;
import pro.ofitserov.forumtest1.repository.ReplyRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.service.UserService;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/reply")
public class ReplyController {

    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;

    @Autowired
    private UserService userService;

    public ReplyController(TopicRepository topicRepository, ReplyRepository replyRepository) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(@RequestParam("topic_id") Long topicId, ModelMap model) {
        model.addAttribute("title", "Add reply");
        model.addAttribute("topic", topicRepository.findOne(topicId));
        model.addAttribute("reply", new Reply());
        return "reply/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public String add(@RequestParam("topic_id") Long topicId, @Valid Reply reply, BindingResult result) {

        if (result.hasErrors()) {
            return "reply/add";
        }

        Topic topic = topicRepository.findOne(topicId);
        reply.setUser(userService.getCurrentUser());
        reply.setTopic(topic);
        reply.setDateOfPublication(new Date());

        replyRepository.save(reply);

        return "redirect:/topic/" + topic.getId();
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }
}
