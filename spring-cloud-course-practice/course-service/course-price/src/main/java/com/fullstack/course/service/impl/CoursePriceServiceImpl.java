package com.fullstack.course.service.impl;

import com.fullstack.course.client.CourseListClient;
import com.fullstack.course.dao.CoursePriceMapper;
import com.fullstack.course.entity.Course;
import com.fullstack.course.entity.CourseAndPrice;
import com.fullstack.course.entity.CoursePrice;
import com.fullstack.course.service.CoursePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoursePriceServiceImpl implements CoursePriceService {
    @Autowired
    CoursePriceMapper coursePriceMapper;

    @Autowired
    private CourseListClient courseListClient;

    @Override
    public CoursePrice getCoursePrice(Integer courseId) {
        return coursePriceMapper.findCoursePrice(courseId);
    }

    @Override
    public List<CourseAndPrice> getCoursesAndPrice() {
        ArrayList<CourseAndPrice> courseAndPrices = new ArrayList<>();
        List<Course> courses = courseListClient.courseList();
        for (Course course : courses) {
            if (course != null) {
                CoursePrice coursePrice = getCoursePrice(course.getCourseId());
                CourseAndPrice courseAndPrice = new CourseAndPrice();
                courseAndPrice.setCourseId(course.getCourseId());
                courseAndPrice.setName(course.getName());
                courseAndPrice.setPrice(coursePrice.getPrice());
                courseAndPrice.setId(course.getId());
                courseAndPrices.add(courseAndPrice);
            }
        }
        return courseAndPrices;
    }
}
