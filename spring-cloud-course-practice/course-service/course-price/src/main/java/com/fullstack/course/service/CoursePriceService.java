package com.fullstack.course.service;

import com.fullstack.course.entity.CourseAndPrice;
import com.fullstack.course.entity.CoursePrice;

import java.util.List;

public interface CoursePriceService {
    CoursePrice getCoursePrice(Integer courseId);

    List<CourseAndPrice> getCoursesAndPrice();
}
