import os
#
# MAIL_SERVER = 'smtp.worksmobile.com'
# MAIL_PORT = 465
# MAIL_USE_SSL = True
# # MAIL_USERNAME = os.environ.get('CAMP_USERNAME')
# # MAIL_PASSWORD = os.environ.get('CAMP_PASSWORD')
# MAIL_USERNAME = 'yzu2016camp@itaclub.asia'
# MAIL_PASSWORD = "uy5983yzu2016camp"
# SECURITY_EMAIL_SENDER = 'yzu2016camp@itaclub.asia'
#
# # database
#
# basedir = os.path.abspath(os.path.dirname(__file__))
#
# # SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(basedir+"/db/", 'app.db')
# # SQLALCHEMY_DATABASE_URI = 'sqlite:///' + "db/app.db"
# SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.environ.get('OPENSHIFT_DATA_DIR') + "db/app.db"
# SQLALCHEMY_MIGRATE_REPO = os.path.join(basedir, 'db_repository')
#
# HOST_NAME = os.environ.get('OPENSHIFT_APP_DNS', 'localhost')
# APP_NAME = os.environ.get('OPENSHIFT_APP_NAME', 'flask')
# IP = os.environ.get('OPENSHIFT_PYTHON_IP', '127.0.0.1')
# PORT = int(os.environ.get('OPENSHIFT_PYTHON_PORT', 8051))

# debug
# TRAP_BAD_REQUEST_ERRORS = True
PROPAGATE_EXCEPTIONS = True
MAIL_DEBUG = True