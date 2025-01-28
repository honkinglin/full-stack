from django import forms
import re

from apps.operation.models import UserAsk
from captcha.fields import CaptchaField

class UserAskForm(forms.Form):
    class Meta:
        model = UserAsk
        fields = ['name', 'mobile', 'course_name']

    def clean_mobile(self):
        """
        Check if the mobile number is valid
        """
        mobile = self.cleaned_data['mobile']
        REGEX_MOBILE = "^1[358]\d{9}$|^147\d{8}$|^176\d{8}$"
        p = re.compile(REGEX_MOBILE)
        if p.match(mobile):
            return mobile
        else:
            raise forms.ValidationError('Mobile Number Invalid', code='mobile_invalid')
