from . import web
from flask_login import current_user, login_required
from app.models.wish import Wish
from flask import render_template

from ..spider.book_flow import BookFlow
from ..view_models.trade import MyTrades
from app.models.base import db
from flask import redirect, url_for, flash
from app.libs.email import send_email
from app.models.gift import Gift


@web.route('/my/wish')
def my_wish():
    uid = current_user.id
    wishes_of_mine = Wish.get_user_wishes(uid)
    isbn_list = [wish.isbn for wish in wishes_of_mine]
    gift_count_list = Wish.get_gifts_count(isbn_list)
    view_model = MyTrades(wishes_of_mine, gift_count_list)
    return render_template('my_wish.html', wishes=view_model.trades)

@web.route('/wish/book/<isbn>')
@login_required
def save_to_wish(isbn):
    book_flow = BookFlow()
    book_flow.search_by_isbn(isbn)
    if current_user.can_save_to_list(isbn):
        with db.auto_commit():
            wish = Wish()
            wish.uid = current_user.id
            wish.isbn = isbn
            db.session.add(wish)
    else:
        flash('这本书已添加至你的赠送清单或已存在于你的心愿清单，请不要重复添加')
    return redirect(url_for('web.book_detail', isbn=isbn))


@web.route('/satisfy/wish/<int:wid>')
@login_required
def satisfy_wish(wid):
    wish = Wish.query.get_or_404(wid)
    gift = Gift.query.filter_by(uid=current_user.id, isbn=wish.isbn).first()
    if not gift:
        flash('你还没有上传此书，请点击“加入赠送清单”添加此书。添加前请确保自己可以赠送此书')
    else:
        send_email(wish.user.email, '有人想送你一本书', 'email/satisify_wish.html', wisher=current_user, gift=gift)
        flash('已向他/她发送了一封邮件，如果对方愿意接受你的赠送，你将收到一个鱼漂')
    return redirect(url_for('web.book_detail', isbn=wish.isbn))


@web.route('/wish/book/<isbn>/redraw')
def redraw_from_wish(isbn):
    wish = Wish.query.filter_by(uid=current_user.id, isbn=isbn).first_or_404()
    with db.auto_commit():
        wish.delete()
    return redirect(url_for('web.my_wish'))
