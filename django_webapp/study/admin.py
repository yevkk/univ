from django.contrib import admin
from django.contrib.auth.models import Group

from nested_inline.admin import NestedStackedInline, NestedModelAdmin

from .models import *


class CourseAdmin(admin.ModelAdmin):
    model = Course
    list_display  = ['course_name', 'course_code']
    search_fields = ['course_name']


class LectureAdmin(admin.ModelAdmin):
    model = Lecture
    list_display  = ['lecture_name', 'lecture_number', 'course']
    list_filter   = ['course']


class OptionInline(NestedStackedInline):
    model = Option
    extra = 1


class TaskInline(NestedStackedInline):
    model   = Task
    extra   = 1 
    inlines = [OptionInline]


class TestAdmin(NestedModelAdmin):
    fields = [
        'course',
        'test_number',
        'test_name',
        'pass_score',
        'enabled',
        ]

    inlines       = [TaskInline]
    list_display  = ['test_name', 'test_number', 'course']
    list_filter   = ['course']


class TestResultAdmin(admin.ModelAdmin):
    list_display  = ['user', 'test', 'score', 'passed']
    list_filter   = ['user']


admin.site.register(Course, CourseAdmin)
admin.site.register(Lecture, LectureAdmin)
admin.site.register(Test, TestAdmin)
admin.site.register(TestResult, TestResultAdmin)

admin.site.unregister(Group)
