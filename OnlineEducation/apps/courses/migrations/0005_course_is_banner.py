# Generated by Django 5.1.4 on 2025-01-13 03:44

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('courses', '0004_course_category_course_tag_course_teacher_and_more'),
    ]

    operations = [
        migrations.AddField(
            model_name='course',
            name='is_banner',
            field=models.BooleanField(default=False, verbose_name='Is Banner'),
        ),
    ]
