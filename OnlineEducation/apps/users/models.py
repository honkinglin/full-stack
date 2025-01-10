from django.db import models
from django.contrib.auth.models import AbstractUser
from datetime import datetime

# Create your models here.
class UserProfile(AbstractUser):
    nick_name = models.CharField(max_length=50, verbose_name='Nick Name', default='')
    birthday = models.DateField(verbose_name='Birthday', null=True, blank=True)
    gender = models.CharField(max_length=6, choices={'M': 'Male', 'F': 'Female'}, default='male')
    address = models.CharField(max_length=100, default='')
    mobile = models.CharField(max_length=11, null=True, blank=True)
    image = models.ImageField(upload_to='image/%Y/%m', default='image/default.png', max_length=100)

    class Meta:
        verbose_name = 'User Profile'
        verbose_name_plural = verbose_name
        db_table = 'UserProfile'

    def __str__(self):
        return self.username

class EmailVerifyRecord(models.Model):
    code = models.CharField(max_length=20, verbose_name='Verification Code')
    email = models.EmailField(max_length=50, verbose_name='Email')
    send_type = models.CharField(max_length=20, choices={'register': 'Register', 'forget': 'Forget Password', 'update': 'Update Email'})
    send_time = models.DateTimeField(auto_now_add=True, default=datetime.now)

    class Meta:
        verbose_name = 'Email Verification Code'
        verbose_name_plural = verbose_name
        db_table = 'EmailVerifyRecord'

    def __str__(self):
        return '{0}({1})'.format(self.code, self.email)

class Banner(models.Model):
    title = models.CharField(max_length=100, verbose_name='Title')
    image = models.ImageField(upload_to='banner/%Y/%m', verbose_name='Banner Image', max_length=100)
    url = models.URLField(max_length=200, verbose_name='URL')
    index = models.IntegerField(default=100, verbose_name='Index')
    add_time = models.DateTimeField(auto_now_add=True, default=datetime.now)

    class Meta:
        verbose_name = 'Banner'
        verbose_name_plural = verbose_name
        db_table = 'Banner'

    def __str__(self):
        return self.title