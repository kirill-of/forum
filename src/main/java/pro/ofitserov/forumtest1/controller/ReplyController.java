package pro.ofitserov.forumtest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.ofitserov.forumtest1.entity.Reply;
import pro.ofitserov.forumtest1.entity.Topic;
import pro.ofitserov.forumtest1.repository.ReplyRepository;
import pro.ofitserov.forumtest1.repository.TopicRepository;
import pro.ofitserov.forumtest1.response.ResourceNotFoundException;
import pro.ofitserov.forumtest1.service.UserService;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

@Controller
@PreAuthorize(value = "hasRole('USER') or hasRole('MODERATOR')")
@RequestMapping("/reply")
public class ReplyController {

    private TopicRepository topicRepository;
    private ReplyRepository replyRepository;
    private UserService userService;

    @Autowired
    public ReplyController(TopicRepository topicRepository, ReplyRepository replyRepository, UserService userService) {
        this.topicRepository = topicRepository;
        this.replyRepository = replyRepository;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String add(@RequestParam("topic_id") Long topicId, @RequestParam(value = "reply_id", required = false) Long replyId, ModelMap model) {
        model.addAttribute("title", "Add reply");

        Topic topic;
        Reply replyTo;

        if (Objects.isNull(topicId) || Objects.isNull(topic = topicRepository.findOne(topicId))) {
            throw new ResourceNotFoundException();
        } else {
            model.addAttribute("topic", topic);
        }

        if (Objects.nonNull(replyId) && Objects.nonNull(replyTo = replyRepository.findOne(replyId))) {
            model.addAttribute("replyTo", replyTo);
        }

        model.addAttribute("reply", new Reply());

        return "reply/add";
    }

    @PostMapping("/add")
    public String update(@RequestParam("topic_id") Long topicId, @RequestParam(value = "reply_id", required = false) Long replyId, @Valid Reply reply, BindingResult result, ModelMap model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("title", "Update reply");

        Topic topic;
        Reply replyTo = null;

        if (Objects.isNull(topicId) || Objects.isNull(topic = topicRepository.findOne(topicId))) {
            throw new ResourceNotFoundException();
        } else {
            model.addAttribute("topic", topic);
        }

        if (Objects.nonNull(replyId) && Objects.nonNull(replyTo = replyRepository.findOne(replyId))) {
            model.addAttribute("replyTo", replyTo);
        }

        if (result.hasErrors()) {
            return "reply/add";
        }

        if (Objects.isNull(reply.getId())) {

            reply.setUser(userService.getCurrentUser());
            reply.setTopic(topic);
            reply.setDateOfPublication(new Date());

            if (Objects.nonNull(replyTo)) {
                reply.setReplyTo(replyTo);
            }

        } else {

            Reply editReply = replyRepository.findOne(reply.getId());

            if (!(userService.isCurrentUserId(editReply.getUser().getId()) || request.isUserInRole("ROLE_MODERATOR"))) {
                throw new AccessDeniedException("This user can't edit this reply");
            }

            editReply.setText(reply.getText());
            editReply.setChangedUser(userService.getCurrentUser());
            editReply.setDateOfChange(new Date());

            reply = editReply;
        }

        replyRepository.save(reply);

        return "redirect:/topic/" + topic.getId();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, ModelMap model, SecurityContextHolderAwareRequestWrapper request) {
        model.addAttribute("title", "Edit reply");

        Reply reply = replyRepository.findOne(id);

        if (Objects.isNull(reply)) {
            throw new ResourceNotFoundException();
        }

        if (!(userService.isCurrentUserId(reply.getUser().getId()) || request.isUserInRole("ROLE_MODERATOR"))) {
            throw new AccessDeniedException("This user can't edit this reply");
        }

        if (Objects.isNull(reply.getTopic())) {
            throw new RuntimeException();
        } else {
            model.addAttribute("topic", reply.getTopic());
        }

        if (Objects.nonNull(reply.getReplyTo())) {
            model.addAttribute("replyTo", reply.getReplyTo());
        }

        model.addAttribute("reply", reply);

        return "reply/add";
    }

    @GetMapping("/{id}/delete")
    public String confirmRemoval(@PathVariable Long id, ModelMap model) {
        model.addAttribute("title", "Delete reply");

        Reply reply = replyRepository.findOne(id);

        if (Objects.isNull(reply)) {
            throw new ResourceNotFoundException();
        }

        model.addAttribute("reply", reply);

        return "reply/delete";
    }

    @PostMapping("/{id}/delete")
    public String remove(@PathVariable Long id) {

        Reply reply = replyRepository.findOne(id);

        if (Objects.isNull(reply)) {
            throw new ResourceNotFoundException();
        }

        replyRepository.delete(id);

        return "redirect:/topic/" + reply.getTopic().getId();
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String view() {
        return "redirect:/";
    }
}
