from django.db import models

# Create your models here.

class CityDict(models.Model):
    name = models.CharField(max_length=20, verbose_name='City Name')
    desc = models.CharField(max_length=200, verbose_name='City Description')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'City'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


class CourseOrg(models.Model):
    name = models.CharField(max_length=50, verbose_name='Organization Name')
    desc = models.TextField(verbose_name='Organization Description')
    tag = models.CharField(default='National', max_length=10, verbose_name='Organization Tag')
    category = models.CharField(default='School', max_length=20, verbose_name='Organization Category')
    click_nums = models.IntegerField(default=0, verbose_name='Click Number')
    fav_nums = models.IntegerField(default=0, verbose_name='Favorite Number')
    image = models.ImageField(upload_to='org/%Y/%m', verbose_name='Organization Image')
    address = models.CharField(max_length=150, verbose_name='Organization Address')
    city = models.CharField(max_length=20, verbose_name='City')
    students = models.IntegerField(default=0, verbose_name='Number of Students')
    course_nums = models.IntegerField(default=0, verbose_name='Number of Courses')
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'Organization'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name


class Teacher(models.Model):
    org = models.ForeignKey(CourseOrg, on_delete=models.CASCADE, verbose_name='Organization')
    name = models.CharField(max_length=50, verbose_name='Teacher Name')
    work_years = models.IntegerField(default=0, verbose_name='Working Years')
    work_company = models.CharField(max_length=50, verbose_name='Company')
    work_position = models.CharField(max_length=50, verbose_name='Position')
    points = models.CharField(max_length=50, verbose_name='Teaching Characteristics')
    click_nums = models.IntegerField(default=0, verbose_name='Click Number')
    fav_nums = models.IntegerField(default=0, verbose_name='Favorite Number')
    age = models.IntegerField(default=18, verbose_name='Age')
    image = models.ImageField(upload_to='teacher/%Y/%m', verbose_name='Teacher Image', max_length=100)
    add_time = models.DateTimeField(auto_now_add=True, verbose_name='Add Time')

    class Meta:
        verbose_name = 'Teacher'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.name

    def get_course_nums(self):
        return self.course_set.all().count()