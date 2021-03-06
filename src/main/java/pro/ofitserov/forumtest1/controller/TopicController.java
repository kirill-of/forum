package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.ofitserov.forumtest1.entity.Section;
import pro.ofitserov.forumtest1.entity.Topic;
import pro.ofitserov.forumtest1.repository.ReplyRepository;
import pro.ofitserov.forumtest1.repository.SectionRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;
import pro.ofitserov.forumtest1.service.UserService;
import pro.ofitserov.forumtest1.util.ForumConstants;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

@Controller
@RequestMapping("/topic")
public class TopicController {

    private UserService userService;
    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;
    private SectionRepository sectionRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, ReplyRepository replyRepository, UserService userService, SectionRepository sectionRepository) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
        this.userService = userService;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/add")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String add(ModelMap model, @RequestParam("section_id") Long sectionId) {

        if (Objects.isNull(sectionId) || !sectionRepository.exists(sectionId)) {
            throw new ResourceNotFoundException();
        } else {
            model.addAttribute("section", sectionRepository.findOne(sectionId));
        }

        model.addAttribute("title", "Add topic");
        model.addAttribute("topic", new Topic());
        return "topic/add";
    }

    @PostMapping("/add")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String add(@Valid Topic topic, BindingResult result, ModelMap model, @RequestParam("section_id") Long sectionId) {
        model.addAttribute("title", "Update topic");

        Section section;

        if (Objects.isNull(sectionId) || !sectionRepository.exists(sectionId)) {
            throw new ResourceNotFoundException();
        } else {
            section = sectionRepository.findOne(sectionId);
            model.addAttribute("section", section);
        }

        if (result.hasErrors()) {
            return "topic/add";
        }

        if (Objects.isNull(topic.getId())) {
            topic.setUser(userService.getCurrentUser());
            topic.setDateOfPublication(new Date());
            topic.setSection(section);
        } else {
            Topic editTopic = topicRepository.findOne(topic.getId());

            if (!(userService.isCurrentUserId(editTopic.getUser().getId()) || userService.hasRole("ROLE_MODERATOR"))) {
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
    public String view(@PathVariable Long id, ModelMap model, @PageableDefault(sort = {"dateOfPublication"}, value = ForumConstants.PAGE_DEFAULT_SIZE, direction = Sort.Direction.ASC) Pageable pageable) {
        Topic topic = topicRepository.findOne(id);

        if (topic == null) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("title", topic.getTitle());
        model.addAttribute("topic", topic);
        model.addAttribute("replies", replyRepository.findByTopic(topic, pageable));
        model.addAttribute("userId", userService.getCurrentUserId());

        return "topic/view";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
    public String edit(@PathVariable Long id, ModelMap model) {

        Topic topic = topicRepository.findOne(id);

        if (Objects.isNull(topic)) {
            throw new ResourceNotFoundException();
        }

        if (!(userService.isCurrentUserId(topic.getUser().getId()) || userService.hasRole("ROLE_MODERATOR"))) {
            throw new AccessDeniedException("This user can't edit this topic");
        }

        model.addAttribute("title", "Edit topic");
        model.addAttribute("topic", topic);
        model.addAttribute("section", topic.getSection());

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
    public String redirect() {
        return "redirect:/";
    }
}
