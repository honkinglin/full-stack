package com.fullstack.course.client;

import com.fullstack.course.entity.Course;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "course-list")
@CircuitBreaker(name = "course-list", fallbackMethod = "courseListFallback")
public interface CourseListClient {

    @GetMapping("/courses")
    List<Course> courseList();

    // 定义回退方法签名，必须与原方法匹配
    default List<Course> courseListFallback(Throwable t) {
        // 可以根据需要进行日志记录或者其他处理
        return new CourseListClientFallback().courseList();
    }
}
