from django.db import models

from apps.orgnization.models import CourseOrg
from apps.orgnization.models import Teacher

# Create your models here.

class Course(models.Model):
    course_org = models.ForeignKey(CourseOrg, on_delete=models.CASCADE, verbose_name='Organization', null=True, blank=True)
    teacher = models.ForeignKey(Teacher, on_delete=models.CASCADE, verbose_name='Teacher', null=True, blank=True)
    name = models.CharField(max_length=50, verbose_name='Course Name')
    desc = models.CharField(max_length=300, verbose_name='Course Description')
    detail = models.TextField(verbose_name='Course Details')
    is_banner = models.BooleanField(default=False, verbose_name='Is Banner')
    degree = models.CharField(choices=(('primary', 'Primary'), ('middle', 'Middle'), ('high', 'High')), max_length=10)
    learn_times = models.IntegerField(default=0, verbose_name='Learning Time (minutes)')
    students = models.IntegerField(default=0, verbose_name='Number of Students')
    fav_nums = models.IntegerField(default=0, verbose_name='Number of Favorites')
    image = models.ImageField(upload_to='courses/%Y/%m', verbose_name='Cover Image', max_length=100)
    click_nums = models.IntegerField(default=0, verbose_name='Number of Clicks')
    category = models.CharField(max_length=20, default='Back-end Development', verbose_name='Course Category')
    tag = models.CharField(default='', verbose_name='Course Tag', max_length=10)
    youneed_know = models.CharField(default='', max_length=300, verbose_name='Prerequisites')
    teacher_tell = models.CharField(default='', max_length=300, verbose_name='Teacher Advice')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Creation Time')

    class Meta:
        verbose_name = 'Course'
        verbose_name_plural = verbose_name

    def get_zj_nums(self):
        # Get the number of lessons in the course
        return self.lesson_set.all().count()

    def get_learn_users(self):
        # Get the users who have learned the course
        return self.usercourse_set.all()[:5]

    def get_course_lesson(self):
        # Get the lessons in the course
        return self.lesson_set.all()

    def __str__(self):
        return self.name


class Lesson(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE, verbose_name='Course')
    name = models.CharField(max_length=100, verbose_name='Lesson Name')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Creation Time')

    class Meta:
        verbose_name = 'Lesson'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


class Video(models.Model):
    lesson = models.ForeignKey(Lesson, on_delete=models.CASCADE, verbose_name='Lesson')
    learn_times = models.IntegerField(default=0, verbose_name='Learning Time (minutes)')
    name = models.CharField(max_length=100, verbose_name='Video Name')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Creation Time')

    class Meta:
        verbose_name = 'Video'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


class CourseResource(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE, verbose_name='Course')
    name = models.CharField(max_length=100, verbose_name='Resource Name')
    download = models.FileField(upload_to='course/resource/%Y/%m', verbose_name='Download Link', max_length=100)
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Creation Time')

    class Meta:
        verbose_name = 'Course Resource'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name
