from django.db import models
from django.core.validators import MaxValueValidator
from django.contrib.auth.models import User
from django.contrib import admin


class Course(models.Model):
    course_code   = models.CharField(max_length=16)
    course_name   = models.CharField(max_length=256)
    description   = models.TextField()
    preconditions = models.ManyToManyField(to='self', symmetrical=False, blank=True)

    def __str__(self):
        return f'{self.course_code}: {self.course_name}'


class Lecture(models.Model):
    course         = models.ForeignKey(Course, on_delete=models.CASCADE)
    lecture_number = models.PositiveIntegerField(default=1)
    lecture_name   = models.CharField(max_length=256)
    content        = models.TextField()

    def __str__(self):
        return f'{self.course.course_code} | Lecture #{self.lecture_number} {self.lecture_name}'


class Test(models.Model):
    course       = models.ForeignKey(Course, on_delete=models.CASCADE)
    test_number  = models.PositiveIntegerField(default=1)
    test_name    = models.CharField(max_length=256)
    pass_score   = models.PositiveIntegerField('pass score %', default=100, validators=[MaxValueValidator(100)])
    enabled      = models.BooleanField(default=False)

    stats_passed = models.PositiveIntegerField(default=0)
    stats_failed = models.PositiveIntegerField(default=0)

    def __str__(self):
        return f'{self.course.course_code} | Test #{self.test_number} {self.test_name}'


class Task(models.Model):
    test      = models.ForeignKey(Test, on_delete=models.CASCADE)
    task_text = models.CharField(max_length=1024)

    def __str__(self):
        return self.task_text


class Option(models.Model):
    task        = models.ForeignKey(Task, on_delete=models.CASCADE)
    option_text = models.CharField(max_length=256)
    correct     = models.BooleanField(default=False)

    def __str__(self):
        return self.option_text


class CompletedCourses(models.Model):
    user   = models.ForeignKey(User, on_delete=models.CASCADE)
    course = models.ForeignKey(Course, on_delete=models.CASCADE)


class TestResult(models.Model):
    user  = models.ForeignKey(User, on_delete=models.CASCADE)
    test  = models.ForeignKey(Test, on_delete=models.CASCADE)
    score = models.PositiveIntegerField(default=0, validators=[MaxValueValidator(100)])
    dt    = models.DateTimeField('datetime')

    @admin.display(
        boolean=True,
    )
    def passed(self):
        return self.score >= self.test.pass_score
    
