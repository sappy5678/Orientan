from flask import Flask,request
import json
app = Flask(__name__)
from flask_cors import CORS, cross_origin
CORS(app)



# 加入api route
from flask_restful import Resource, Api
from module import *
api = Api(app)
api.add_resource(ApiDistribute.UserHistory,"/api/v1.0/UserHistory/<string:userID>")
api.add_resource(ApiDistribute.RecommendPages,"/api/v1.0/RecommendPages/<string:userID>/<int:size>")
@app.route('/')
def hello_world():
    return 'Hello World!'

@app.route('/postTest', methods=['GET', 'POST'])
def postTest():
    print(json.loads((request.data).decode("utf-8") ))
    return "SUCESS"

@app.route('/test/<string:a>')
def a():
    print("SUCCESSSSSS")    
    return a


if __name__ == '__main__':

    
    import logging
    file_handler = logging.FileHandler('flask.log')
    file_handler.setLevel(logging.WARNING)
    app.logger.addHandler(file_handler)
    app.run(host='0.0.0.0')
