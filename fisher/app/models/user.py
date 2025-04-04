from werkzeug.security import generate_password_hash, check_password_hash
from sqlalchemy import Column, Integer, String
from itsdangerous import URLSafeTimedSerializer as Serializer
from flask import current_app

from app import login_manager
from app.libs.enums import PendingStatus
from app.models.base import Base, db
from flask_login import UserMixin

from app.libs.helper import is_isbn_or_key
from app.spider.book_flow import BookFlow
from app.models.gift import Gift
from app.models.wish import Wish
from app.models.drift import Drift
from math import floor

class User(UserMixin, Base):
    id = Column(Integer, primary_key=True)
    nickname = Column(String(24), nullable=False)
    phone_number = Column(String(18), unique=True)
    _password = Column('password', String(255), nullable=False)
    email = Column(String(50), unique=True, nullable=False)
    beans = Column(Integer, default=0)
    send_counter = Column(Integer, default=0)
    receive_counter = Column(Integer, default=0)
    wx_open_id = Column(String(50))
    wx_name = Column(String(32))

    @property
    def password(self):
        return self._password

    @password.setter
    def password(self, raw):
        self._password = generate_password_hash(raw)

    def can_send_drift(self):
        if self.beans < 1:
            return False
        success_gifts_count = Gift.query.filter_by(uid=self.id, launched=True).count()
        success_receive_count = Drift.query.filter_by(requester_id=self.id, pending=PendingStatus.success).count()
        return True if floor(success_receive_count / 2) <= floor(success_gifts_count) else False

    def check_password(self, raw):
        return check_password_hash(self._password, raw)

    def can_save_to_list(self, isbn):
        if not is_isbn_or_key(isbn):
            return False
        book_flow = BookFlow()
        book_flow.search_by_isbn(isbn)
        if not book_flow.first:
            return False
        # 不允许一个用户同时赠送多本相同的图书
        # 一个用户不可能同时成为赠送者和索要者
        gifting = Gift.query.filter_by(uid=self.id, isbn=isbn, launched=False).first()
        wishing = Wish.query.filter_by(uid=self.id, isbn=isbn, launched=False).first()
        if not gifting and not wishing:
            return True
        else:
            return False

    def generate_token(self, expiration=600):
        s = Serializer(current_app.config['SECRET_KEY'], salt='login')
        return s.dumps({'id': self.id}).decode('utf-8')

    @staticmethod
    def reset_password(token, new_password):
        s = Serializer(current_app.config['SECRET_KEY'], salt='reset-password')
        try:
            data = s.loads(token.encode('utf-8'))
        except:
            return False

        uid = data.get('id')
        with db.auto_commit():
            user = User.query.get(uid)
            user.password = new_password
        return True

    @property
    def summary(self):
        return dict(
            nickname=self.nickname,
            beans=self.beans,
            email=self.email,
            send_receive=str(self.send_counter) + '/' + str(self.receive_counter)
        )


@login_manager.user_loader
def get_user(uid):
    return User.query.get(int(uid))
