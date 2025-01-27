from app.libs.httper import HTTP
from flask import current_app

class BookFlow:
    # https://developers.google.com/books/docs/v1/reference/volumes?hl=zh-cn
    isbn_url = 'https://www.googleapis.com/books/v1/volumes?q=isbn:{}'
    keyword_url = 'https://www.googleapis.com/books/v1/volumes?q={}&startIndex={}&maxResults={}'

    headers = {
        'X-goog-api-key': 'AIzaSyAjWffV71a--M-losCRv1zuYJjzTHAS-IY'
    }

    def __init__(self):
        self.total = 0
        self.books = []

    def __fill_single(self, data):
        if data:
            self.total = 1
            self.books.append(data)

    def __fill_collection(self, data):
        if data:
            self.total = data['totalItems']
            self.books = data['items']

    def search_by_isbn(self, isbn):
        url = self.isbn_url.format(isbn)
        result = HTTP.get(url, headers=self.headers)
        self.__fill_collection(result)

    def search_by_keyword(self, keyword, page=1):
        url = self.keyword_url.format(keyword, self.calculate_start(page), current_app.config['PER_PAGE'])
        result = HTTP.get(url, headers=self.headers)
        self.__fill_collection(result)
        return result

    def calculate_start(self, page):
        return (page - 1) * current_app.config['PER_PAGE']

    @property
    def first(self):
        return self.books[0] if self.total >= 1 else None
