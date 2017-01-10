"""
@ author sappy5678
@ email  sappy5678@gmail.com

這是一個 api 的介面
主要是用來分發 api 到正確 function 的轉換器
api 形式為 restful
"""

from flask_restful import Resource
import json
from flask import request, make_response
from module.v1.dbConnect import dbConnect
from GlobalVariable import __WaitToCrawler__
from module.v1.pageQueue_process.MainProcess import MainProcess
class UserHistory(Resource):
    """
    用來放置或者是取得使用者歷史紀錄偏好
    """

    # todo UserHistory get wait to complement
    def get(self,userID):
        """
        用來讀取使用者歷史紀錄的
        :param jsonForm:
        :return: reVal(json) -
        """
        jsonForm = (request.data).decode("utf-8")
        reVal = {}

        # 嘗試讀取 json 資料
        try:
            data = json.loads(jsonForm)
        except:

            reVal["statusCode"] = 500
            reVal["descript"] = "UNknown Error"
            return(json.dumps(reVal))


    def post(self,userID):
        """
         送出使用者偏好
        :param jsonForm:
        :return:
        """
        reVal = {}
        jsonForm = (request.data).decode("utf-8")
        # 嘗試讀取 json 資料
        try:
            data = json.loads(jsonForm)
        except:

            reVal["statusCode"] = 500
            reVal["descript"] = "UNknown Error"
            return(json.dumps(reVal))

        # 檢查使用者是否在資料庫中
        Priority = 1
        # 資料庫連線
        db = dbConnect()
        db.connect()

        # 如果查詢不到的話
        # 先幫他註冊

        if db.query("select * from users where username = %s",userID) == ():
            db.execute("insert into users(username) values(%s)",userID)
            db.commit()
            Priority = 0

        # 放入等待爬得queue中
        WaitToPutInQueue = {"username":userID,
                            "pages":data["jsonForm"]
                            }
        __WaitToCrawler__.put((Priority,WaitToPutInQueue))


        reVal["statusCode"] = 200
        reVal["descript"] = "SUCESS"
        db.close()
        thr = MainProcess(1)
        thr.start()
        return (json.dumps(reVal))

import heapq
import datetime
class RecommendPages(Resource):
    """
    跟推薦系統要結果
    """

    # todo ***** finish recommand get
    def get(self,userID,size):
        print(datetime.datetime.now())
        packet={}
        db = dbConnect()
        db.connect()
        sql = "select id from users where username = %(username)s"
        packet["username"] = userID
        tags = db.query(sql,packet)
        # 資訊包裹

        packet["userID"] = tags[0]["id"]

        # 回傳暫存
        returnValue =  []
        reVal = {}


        # sql = "select tagid from users_tags_relation where userid = %(userID)s"
        # tags = db.query(sql,packet)
        # packet["tag"] = tag["tagid"]
        sql = "select * from pages where id in (select pageid from pages_tags_relation where tagid in (select tagid from users_tags_relation where userid = %(userID)s));"
        print(sql% packet)
        pages = db.query(sql, packet)


        # 統計出現次數
        for page in pages:
            page["descr"]=""
            page["title"] = page["title"].replace('"',"").replace("'","")
            if returnValue == []:
                heapq.heappush(returnValue,[-1,page["id"],page])
            else:
                for __ in returnValue:
                    if page["id"] == __[2]["id"]:
                        __[0] -= 1
                        break
                    if __ == returnValue[-1] :
                        heapq.heappush(returnValue, [-1,page["id"], page])
                        break
        reVal["statusCode"] = 200
        reVal["descript"] = "SUCESS"
        returnValue = [i[2] for i in returnValue]
        print(datetime.datetime.now())
        if len(returnValue) <= size or size == 0:
            reVal["pages"] = returnValue
        else:
            reVal["pages"] = returnValue[0:size]

        db.close()
        # print((json.dumps(reVal,ensure_ascii=False)))
        return make_response((json.dumps(reVal)))


