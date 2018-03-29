package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
import pro.ofitserov.forumtest1.repository.UserRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;
import pro.ofitserov.forumtest1.service.UserService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private UserService userService;

    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;
    private UserRepository userRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, ReplyRepository replyRepository, UserRepository userRepository, UserService userService) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/add")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String add(ModelMap model) {
        model.addAttribute("title", "Add topic");
        model.addAttribute("topic", new Topic());
        return "topic/add";
    }

    @PostMapping("/add")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String add(@Valid Topic topic, BindingResult result, ModelMap model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("title", "Update topic");

        if (result.hasErrors()) {
            return "topic/add";
        }

        if (Objects.isNull(topic.getId())) {
            topic.setUser(userService.getCurrentUser());
            topic.setDateOfPublication(new Date());
        } else {
            Topic editTopic = topicRepository.findOne(topic.getId());

            if (!(userService.isCurrentUserId(editTopic.getUser().getId()) || request.isUserInRole("ROLE_MODERATOR"))) {
                throw new AccessDeniedException("This user can't edit this topic");
            }

            editTopic.setTitle(topic.getTitle());
            editTopic.setText(topic.getText());
            editTopic.setChangedUser(userService.getCurrentUser());
            editTopic.setDateOfChange(new Date());
            topic = editTopic;
        }

        topicRepository.save(topic);

        return "redirect:/topic/" + topic.getId();
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
        model.addAttribute("userId", userService.getCurrentUserId());

        return "topic/view";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String edit(@PathVariable Long id, ModelMap model, SecurityContextHolderAwareRequestWrapper request) {

        Topic topic = topicRepository.findOne(id);

        if (topic == null) {
            throw new ResourceNotFoundException();
        }

        if (!(userService.isCurrentUserId(topic.getUser().getId()) || request.isUserInRole("ROLE_MODERATOR"))) {
            throw new AccessDeniedException("This user can't edit this topic");
        }

        model.addAttribute("title", "Edit topic");
        model.addAttribute("topic", topic);

        return "topic/add";
    }

    @GetMapping("/{id}/delete")
    public String confirmRemoval(@PathVariable Long id, ModelMap model) {
        model.addAttribute("title", "Delete topic");

        Topic topic = topicRepository.findOne(id);

        if (Objects.isNull(topic)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("topic", topic);

        return "topic/delete";
    }

    @PostMapping("/{id}/delete")
    public String remove(@PathVariable Long id) {

        Topic topic = topicRepository.findOne(id);

        if (Objects.isNull(topic)) {
            throw new ResourceNotFoundException();
        }

        topicRepository.delete(id);

        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }
}
