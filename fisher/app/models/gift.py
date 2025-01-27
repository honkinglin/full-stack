from sqlalchemy import Column, Integer, Boolean, ForeignKey, String, desc, func
from sqlalchemy.orm import relationship
from flask import current_app

from app.models.base import Base, db
from app.spider.book_flow import BookFlow

class Gift(Base):
    user = relationship('User')
    uid = Column(Integer, ForeignKey('user.id'), primary_key=True)
    id = Column(Integer, primary_key=True, autoincrement=True)
    isbn = Column(String(15), nullable=False)
    launched = Column(Boolean, default=False)

    @property
    def book(self):
        book_flow = BookFlow()
        book_flow.search_by_isbn(self.isbn)
        return book_flow.first

    @classmethod
    def recent(cls):
        recent_gift = Gift.query.filter_by(launched=False).order_by(
            desc(Gift.create_time)).limit(
            current_app.config['RECENT_BOOK_COUNT']).distinct().all()
        return recent_gift

        # recent_gift = (
        #     Gift.query
        #     .with_entities(
        #         Gift.isbn,
        #         func.max(Gift.create_time).label("create_time")  # 获取每个 ISBN 分组中最新的时间
        #     )
        #     .filter_by(launched=False)
        #     .group_by(Gift.isbn)
        #     .order_by(func.max(Gift.create_time).desc())  # 按最新时间排序
        #     .limit(current_app.config['RECENT_BOOK_COUNT'])
        #     .all()
        # )
        return recent_gift

    @classmethod
    def get_wish_counts(cls, isbn_list):
        from app.models.wish import Wish
        count_list = db.session.query(func.count(Wish.id), Wish.isbn).filter(
            Wish.launched == False, Wish.isbn.in_(isbn_list), Wish.status == 1
        ).group_by(Wish.isbn).all()
        count_list = [{'count': w[0], 'isbn': w[1]} for w in count_list]
        return count_list

    @classmethod
    def get_user_gifts(cls, uid):
        gifts = Gift.query.filter_by(uid=uid, launched=False).order_by(
            desc(Gift.create_time)).all()
        return gifts

