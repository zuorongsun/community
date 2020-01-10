package com.community.sun.community.controller;

import com.community.sun.community.dto.QuestionDTO;
import com.community.sun.community.mapper.QuestionMapper;
import com.community.sun.community.mapper.UserMapper;
import com.community.sun.community.model.Question;
import com.community.sun.community.model.User;
import com.community.sun.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Long id,
                        Model model){
        QuestionDTO question=questionService.getById(id);
        model.addAttribute("tag",question.getTag());
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        return "publish";
    }


    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false) String title,
                             @RequestParam(value ="description",required = false) String description,
                             @RequestParam(value ="tag",required = false) String tag,
                             @RequestParam(value ="id",required = false) Long id,
                             HttpServletRequest request,
                             Model model
    ){
        model.addAttribute("tag",tag);
        System.out.println(title+"*"+description+"*");
        if(title==null||title==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        model.addAttribute("title",title);
        if(description==null||description==""){
            model.addAttribute("error","问题描述不能为空");
            return "publish";
        }
        model.addAttribute("description",description);

       User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question=new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
