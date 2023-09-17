from django.shortcuts import render
from django.urls import reverse
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required

from .models import *


def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse('study:index'))


@login_required(login_url="login/")
def index(request):
    course_list = Course.objects.all()
    
    context = {
        'user': request.user,
        'course_list': course_list
    }
    return render(request, 'study/index.html', context)

