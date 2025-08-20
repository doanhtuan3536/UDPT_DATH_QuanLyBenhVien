package com.doanth.qlbv_web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String index(Model model) {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////        CustomUserDetails loggedUser = (CustomUserDetails) auth.getPrincipal();
//
//        List<Topic> topics = topicService.listTopics();
//
//        List<Map<String, Object>> topicDataList = new ArrayList<>();
//        int currentYear = LocalDate.now().getYear();
//
//        for (Topic topic : topics) {
//            List<Paper> papers = paperService.findTop5NewPapersForTopicInYear(topic.getTopicId(), currentYear-1);
//
//            Map<String, Object> topicData = new HashMap<>();
//            topicData.put("name", topic.getTopicName());
//            topicData.put("papers", papers);
//
//            topicDataList.add(topicData);
//        }
//
//        model.addAttribute("topics", topicDataList);
        return "home";
    }
}
