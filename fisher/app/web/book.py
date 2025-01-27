from flask import jsonify, request, render_template
import json
from app.models.gift import Gift
from app.models.wish import Wish
from flask_login import current_user

from . import web
from app.libs.helper import is_isbn_or_key
from app.spider.book_flow import BookFlow
from app.forms.book import SearchForm
from app.view_models.book import BookCollection, BookViewModel
from app.view_models.trade import TradeInfo

@web.route('/book/search')
def search():
    form = SearchForm(request.args)
    books = BookCollection()
    if form.validate():
        q = form.q.data.strip()
        page = form.page.data
        isbn_or_key = is_isbn_or_key(q)
        book_flow = BookFlow()
        if isbn_or_key == 'isbn':
            book_flow.search_by_isbn(q)
        else:
            book_flow.search_by_keyword(q, page)
        books.fill(book_flow, q)

        # return json.dumps(books, default=lambda o: o.__dict__)
        return render_template('search_result.html', books=books)
    else:
        return jsonify({'msg': 'The keyword is invalid'}), 400


@web.route('/book/<isbn>/detail')
def book_detail(isbn):
    has_in_gifts = False
    has_in_wishes = False

    book_flow = BookFlow()
    book_flow.search_by_isbn(isbn)
    book = BookViewModel(book_flow.first)

    if current_user.is_authenticated:
        if Gift.query.filter_by(uid=current_user.id, isbn=isbn, launched=False).first():
            has_in_gifts = True
        if Wish.query.filter_by(uid=current_user.id, isbn=isbn, launched=False).first():
            has_in_wishes = True

    trade_gifts = Gift.query.filter_by(isbn=isbn, launched=False).all()
    trade_wishes = Wish.query.filter_by(isbn=isbn, launched=False).all()

    trade_gifts_model = [TradeInfo(trade_gifts)]
    trade_wishes_model = [TradeInfo(trade_wishes)]

    return render_template('book_detail.html', book=book, wishes=trade_wishes_model, gifts=trade_gifts_model,
                           has_in_gifts=has_in_gifts, has_in_wishes=has_in_wishes)
