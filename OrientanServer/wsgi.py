#!/usr/bin/python
import os
# # virtenv = os.environ['OPENSHIFT_PYTHON_DIR'] + '/virtenv/'
# virtualenv = "D:\DATA\learn\python\\recommand system\Orientan\server\\activate.bat"
#
# #py3版
# with open(virtualenv) as f:
#     #code = compile(f.read(), virtualenv, 'exec')
#     exec(virtualenv, dict(__file__=virtualenv))


# py2版
# try:
#     execfile(virtualenv, dict(__file__=virtualenv))
# except IOError:
#     pass
#
# IMPORTANT: Put any additional includes below this line.  If placed above this
# line, it's possible required libraries won't be in your searchable path
#


from MainServer import app as application
#
# Below for testing only
#
if __name__ == '__main__':
    from wsgiref.simple_server import make_server
    httpd = make_server('0.0.0.0', 5000, application)
    # Wait for a single request, serve it and quit.
    httpd.serve_forever()
