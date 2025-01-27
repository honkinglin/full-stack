from sqlalchemy import Column, Integer, String, Boolean, ForeignKey, func, desc
from sqlalchemy.orm import relationship
from app.models.base import Base, db

class Wish(Base):
    id = Column(Integer, primary_key=True)
    user = relationship('User')
    uid = Column(Integer, ForeignKey('user.id'))
    isbn = Column(String(13))
    launched = Column(Boolean, default=False)

    @classmethod
    def get_user_wishes(cls, uid):
        wishes = Wish.query.filter_by(uid=uid, launched=False).order_by(
            desc(Wish.create_time)).all()
        return wishes

    @classmethod
    def get_gifts_count(cls, isbn_list):
        from app.models.gift import Gift
        count_list = db.session.query(func.count(Gift.id), Gift.isbn).filter(
            Gift.launched == False, Gift.isbn.in_(isbn_list), Gift.status == 1
        ).group_by(Gift.isbn).all()
        count_list = [{'count': w[0], 'isbn': w[1]} for w in count_list]
        return count_list

    @property
    def book(self):
        from app.spider.book_flow import BookFlow
        book_flow = BookFlow()
        book_flow.search_by_isbn(self.isbn)
        return book_flow.first

