from django.db import models
from apps.users.models import UserProfile
from apps.courses.models import Course

# Create your models here.

class UserAsk(models.Model):
    name = models.CharField(max_length=20, verbose_name='User Name')
    mobile = models.CharField(max_length=11, verbose_name='Mobile Number')
    course_name = models.CharField(max_length=50, verbose_name='Course Name')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'User Consultation'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


class CourseComments(models.Model):
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE, verbose_name='User')
    course = models.ForeignKey(Course, on_delete=models.CASCADE, verbose_name='Course')
    comments = models.CharField(max_length=200, verbose_name='Comments')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'Course Comments'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.user.username


class UserFavorite(models.Model):
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE, verbose_name='User')
    fav_id = models.IntegerField(default=0, verbose_name='Data ID')
    fav_type = models.IntegerField(choices=((1, 'Course'), (2, 'Course Organization'), (3, 'Teacher')), default=1, verbose_name='Favorite Type')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'User Favorite'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.user.username


class UserMessage(models.Model):
    user = models.IntegerField(default=0, verbose_name='User ID')
    message = models.CharField(max_length=500, verbose_name='Message Content')
    has_read = models.BooleanField(default=False, verbose_name='Read Status')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'User Message'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.user


class UserCourse(models.Model):
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE, verbose_name='User')
    course = models.ForeignKey(Course, on_delete=models.CASCADE, verbose_name='Course')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'User Course'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.user.username
