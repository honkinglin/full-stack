from django.urls import path, include

from .views import UserinfoView, UploadImageView, UpdatePwdView, SendEmailCodeView, UpdateEmailView, MyCourseView, MyFavOrgView, MyFavTeacherView, MyFavCourseView, MyMessageView

urlpatterns = [
    path('info/', UserinfoView.as_view(), name="user_info"),

    path('image/upload/', UploadImageView.as_view(), name="image_upload"),

    path('update/pwd/', UpdatePwdView.as_view(), name="update_pwd"),

    path('sendemail_code/', SendEmailCodeView.as_view(), name="sendemail_code"),

    path('update_email/', UpdateEmailView.as_view(), name="update_email"),

    path('mycourse/', MyCourseView.as_view(), name="mycourse"),

    path('myfav/org/', MyFavOrgView.as_view(), name="myfav_org"),

    path('myfav/teacher/', MyFavTeacherView.as_view(), name="myfav_teacher"),

    path('myfav/course/', MyFavCourseView.as_view(), name="myfav_course"),

    path('mymessage/', MyMessageView.as_view(), name="mymessage"),
]