from django.shortcuts import render, get_object_or_404
from django.urls import reverse
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.core.exceptions import PermissionDenied
from django.utils import timezone
from django.db.models import F

from .models import *


def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse('study:index'))


@login_required(login_url="login/")
def index(request):
    all_courses       = Course.objects.all()
    completed_courses = CompletedCourses.objects.filter(user=request.user.id)

    course_list = [{
        'e'         : course,
        'completed' : (course in completed_courses),
        'available' : all([prec in completed_courses for prec in course.preconditions.all()])
        } for course in all_courses]
    
    context = {
        'user'        : request.user,
        'course_list' : course_list
    }
    return render(request, 'study/index.html', context)


@login_required(login_url="login/")
def course(request, course_id):
    course       = get_object_or_404(Course, pk=course_id)
    course_tests = Test.objects.filter(course=course_id)

    test_list = [{
        'e'      : test,
        'passed' : any([t_res.passed() for t_res in TestResult.objects.filter(test=test.id, user=request.user.id)]),
        'failed' : any([not t_res.passed() for t_res in TestResult.objects.filter(test=test.id, user=request.user.id)])
        } for test in course_tests]
    
    context = {
        'user'      : request.user,
        'course'    : course,
        'test_list' : test_list
    }
    return render(request, 'study/course.html', context)


@login_required(login_url="login/")
def lecture(request, lecture_id):
    lecture = get_object_or_404(Lecture, pk=lecture_id)
    
    context = {
        'user'    : request.user,
        'lecture' : lecture,
    }
    return render(request, 'study/lecture.html', context)


@login_required(login_url="login/")
def test(request, test_id):
    test    = get_object_or_404(Test, pk=test_id)
    results = TestResult.objects.filter(test=test.id, user=request.user.id)
    
    context = {
        'user'    : request.user,
        'test'    : test,
        'results' : results.order_by('-dt')
    }
    return render(request, 'study/test.html', context)


@login_required(login_url="login/")
def submit_test(request, test_id):
    test      = get_object_or_404(Test, pk=test_id)
    tasks     = test.task_set.all()
    max_score = len(tasks)
    score     = 0

    for task in tasks:
        options = task.option_set.all()
        
        false_opt_ids, true_opt_ids = [], []
        for opt in options:
            (false_opt_ids, true_opt_ids)[opt.correct].append(opt.id)
        
        if not any([f'opt-{id}' in request.POST and request.POST[f'opt-{id}'] == 'on' for id in false_opt_ids]) \
           and all([f'opt-{id}' in request.POST and request.POST[f'opt-{id}'] == 'on' for id in true_opt_ids]):
            score += 1
    
    score_percent = score / max_score * 100
    
    # Save result
    res = TestResult(user=request.user, test=test, score=score_percent, dt=timezone.now())
    res.save()

    # Update stats
    if score_percent >= test.pass_score:
        test.stats_passed = F('stats_passed') + 1
    else:
        test.stats_failed = F('stats_failed') + 1

    # Complete course
    course_tests = Test.objects.filter(course=test.course.id)
    if all([TestResult.objects.filter(user=request.user, test=t, score__gte=t.pass_score).exists() for t in course_tests]):
        c = CompletedCourses(user=request.user, course=test.course)
        c.save()

    return HttpResponseRedirect(reverse('study:course', args=(test.course.id, )))


@login_required(login_url="login/")
def account_info(request):
    completed_courses = CompletedCourses.objects.filter(user=request.user.id)
    test_results      = TestResult.objects.filter(user=request.user.id)
    
    context = {
        'user'              : request.user,
        'completed_courses' : completed_courses,
        'test_results'      : test_results.order_by('-dt')
    }
    return render(request, 'study/account_info.html', context)

