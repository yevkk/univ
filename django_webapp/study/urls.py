from django.urls import path
from django.contrib.auth import views as auth_views

from . import views

app_name    = 'study'
urlpatterns = [
    path('login/',  auth_views.LoginView.as_view(), name='login'),
    path('logout/', views.logout_view,              name='logout'),

    path('',                          views.index,   name='index'),
    path('course/<int:course_id>/',   views.course,  name='course'),
    path('lecture/<int:lecture_id>/', views.lecture, name='lecture')
]
