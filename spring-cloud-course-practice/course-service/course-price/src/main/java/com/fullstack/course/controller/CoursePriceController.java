package com.fullstack.course.controller;

import com.fullstack.course.client.CourseListClient;
import com.fullstack.course.entity.Course;
import com.fullstack.course.entity.CourseAndPrice;
import com.fullstack.course.entity.CoursePrice;
import com.fullstack.course.service.CoursePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CoursePriceController {
    @Autowired
    private CoursePriceService coursePriceService;

    @Autowired
    CourseListClient courseListClient;

    @GetMapping("price")
    public Integer getCoursePrice(@RequestParam("courseId") Integer courseId) {
        CoursePrice coursePrice = coursePriceService.getCoursePrice(courseId);
        return coursePrice.getPrice();
    }

    @GetMapping("/coursesInPrice")
    public List<Course> getCourseListInPrice() {
        List<Course> courses = courseListClient.courseList();
        return courses;
    }

    @GetMapping("/coursesAndPrice")
    public List<CourseAndPrice> getCourseListAndPrice() {
        List<CourseAndPrice> courseAndPrices = coursePriceService.getCoursesAndPrice();
        return courseAndPrices;
    }
}
