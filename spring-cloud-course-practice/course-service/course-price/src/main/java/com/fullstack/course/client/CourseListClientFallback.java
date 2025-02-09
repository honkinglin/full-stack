package com.fullstack.course.client;

import com.fullstack.course.entity.Course;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseListClientFallback implements CourseListClient {
    @Override
    public List<Course> courseList() {
        List<Course> defaultCourses = new ArrayList<>();
        Course course = new Course();
        course.setId(1);
        course.setCourseId(1);
        course.setValid(1);
        course.setName("default course");
        defaultCourses.add(course);
        return defaultCourses;
    }
}
