DEBUG = True
PORT = 9000
HOST = '0.0.0.0'

SQLALCHEMY_DATABASE_URI = 'mysql+cymysql://root:123456@localhost:3306/fisher'

SECRET_KEY = '123456'

# Email setting
MAIL_SERVER = 'smtp.gmail.com'
MAIL_PORT = 587
MAIL_USE_SSL = False
MAIL_USE_TSL = True
MAIL_USERNAME = 'kyrietest66@gmail.com'
MAIL_PASSWORD = 'fuyletwtskivxvjt'
MAIL_SUBJECT_PREFIX = '[BookFlow]'
MAIL_SENDER = ('BookFlow Admin', 'kyrietest66@gmail.com')
