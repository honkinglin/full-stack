import math
from flask import current_app

class BookViewModel:
    def __init__(self, book):
      self.title = book.get('volumeInfo', {}).get('title', '')

      self.isbn = book.get('volumeInfo', {}).get('industryIdentifiers', [])
      self.isbn = self.isbn[0].get('identifier', '') if self.isbn else ''

      self.author = ', '.join(book.get('volumeInfo', {}).get('authors', []))
      self.publisher = book.get('volumeInfo', {}).get('publisher', '')
      self.pages = math.ceil(book.get('volumeInfo', {}).get('pageCount', 1) / current_app.config['PER_PAGE'])
      retail_price = book.get('saleInfo', {}).get('retailPrice', '')
      self.price = str(retail_price.get('amount', 0)) + retail_price.get('currencyCode', '') if retail_price else ''
      self.summary = book.get('volumeInfo', {}).get('description', '')
      self.image = book.get('volumeInfo', {}).get('imageLinks', {}).get('thumbnail', '')

    @property
    def intro(self):
      intros = filter(lambda x: True if x else False, [self.author, self.publisher, self.price])
      return ' / '.join(intros)

class BookCollection:
    def __init__(self):
      self.total = 0
      self.books = []
      self.keyword = ''

    def fill(self, book_flow, keyword):
        self.total = book_flow.total
        self.keyword = keyword
        self.books = [BookViewModel(book) for book in book_flow.books]
