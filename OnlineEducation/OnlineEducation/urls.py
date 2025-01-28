"""
URL configuration for OnlineEducation project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.conf.urls import include
from django.views.generic import TemplateView
from django.views.static import serve

from OnlineEducation.settings import MEDIA_ROOT, STATIC_ROOT
from apps.users.views import LogoutView, LoginView, RegisterView, AciveUserView, ForgetPwdView, ResetView, ModifyPwdView, IndexView

urlpatterns = [
       path('grappelli/', include('grappelli.urls')),  # grappelli URLS
       path('admin/', admin.site.urls),

       path('', IndexView.as_view(), name="index"),
       path('login/', LoginView.as_view(), name="login"),
       path('logout/', LogoutView.as_view(), name="logout"),
       path('register/', RegisterView.as_view(), name="register"),
       path('captcha/', include('captcha.urls')),
       path('active/<str:active_code>/', AciveUserView.as_view(), name="user_active"),
       path('forget/', ForgetPwdView.as_view(), name="forget_pwd"),
       path('reset/<str:active_code>/', ResetView.as_view(), name="reset_pwd"),
       path('modify_pwd/', ModifyPwdView.as_view(), name="modify_pwd"),

       path('org/', include(('apps.orgnization.urls', 'org'), namespace="org")),
       path('course/', include(('apps.courses.urls', 'course'), namespace="course")),

       path('users/', include(('apps.users.urls', 'users'), namespace="users")),

       path('media/<path:path>', serve, {"document_root": MEDIA_ROOT}),
       path('static/<path:path>', serve, {"document_root": STATIC_ROOT}),
]

handler404 = 'apps.users.views.page_not_found'
handler500 = 'apps.users.views.page_error'