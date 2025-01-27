from flask_sqlalchemy import SQLAlchemy as _SQLAlchemy
from sqlalchemy.orm import Query as BaseQuery
from sqlalchemy import Column, Integer
from contextlib import contextmanager
from datetime import datetime
from flask import abort

class SQLAlchemy(_SQLAlchemy):
    @contextmanager
    def auto_commit(self):
        try:
            yield
            self.session.commit()
        except Exception as e:
            self.session.rollback()
            raise e

class Query(BaseQuery):
    def filter_by(self, **kwargs):
        if 'status' not in kwargs.keys():
            kwargs['status'] = 1
        return super(Query, self).filter_by(**kwargs)

    def first_or_404(self):
        result = self.first()
        if not result:
            abort(404)
        return result

    def get_or_404(self, ident):
        result = self.get(ident)
        if not result:
            abort(404)
        return result

db = SQLAlchemy(query_class=Query)

class Base(db.Model):
    __abstract__ = True
    create_time = Column('create_time', Integer)
    update_time = Column('update_time', Integer)
    delete_time = Column('delete_time', Integer)
    status = Column('status', Integer, default=1)

    def __init__(self):
        self.create_time = int(datetime.now().timestamp())
        self.update_time = int(datetime.now().timestamp())

    def set_attrs(self, attrs_dict):
        for key, value in attrs_dict.items():
            if hasattr(self, key) and key != 'id':
                setattr(self, key, value)

    @property
    def create_datetime(self):
        if self.create_time:
            return datetime.fromtimestamp(self.create_time)
        else:
            return None

    def delete(self):
        self.status = 0
