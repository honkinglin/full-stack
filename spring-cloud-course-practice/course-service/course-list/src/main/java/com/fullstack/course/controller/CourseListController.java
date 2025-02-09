package com.fullstack.course.controller;

import com.fullstack.course.entity.Course;
import com.fullstack.course.service.CourseListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseListController {
    @Autowired
    private CourseListService courseListService;

    @GetMapping("/courses")
    public List<Course> courseList() {
        return courseListService.getCourseList();
    }
}
