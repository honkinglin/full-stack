from wtforms import Form, StringField, IntegerField
from wtforms.validators import Length, NumberRange, DataRequired


class SearchForm(Form):
    q = StringField(validators=[DataRequired(), Length(min=1, max=30)])
    page = IntegerField(validators=[NumberRange(min=1, max=99)], default=1)

class DriftForm(Form):
    recipient_name = StringField(validators=[DataRequired(), Length(min=2, max=20, message='收件人姓名长度必须在2到20个字符之间')])
    mobile = StringField(validators=[DataRequired(), Length(11, 11, message='手机号码长度必须为11个字符')])
    message = StringField()
    address = StringField(validators=[DataRequired(), Length(min=10, max=70, message='地址还不到10个字符吗？尽量写详细一些吧')])
