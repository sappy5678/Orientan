from module.v1.pageQueue_process.tfidf import tfidf
from GlobalVariable import __WaitToCrawler__
from configparser import ConfigParser,ExtendedInterpolation
parser = ConfigParser(interpolation=ExtendedInterpolation())
parser.read('./config.ini')
import logging

logging.basicConfig(filename=parser["Log"]["Log"])

from threading import *
from module.v1.dbConnect import dbConnect
from queue import Queue
import time
import datetime
class MainProcess(Thread):
    TF= None
    queueSize = None
    target = None
    db = None
    ThreadQueue = None
    def __init__(self,queueSize):
        Thread.__init__(self) #
        self.TF = tfidf(mode_path=parser["ModulePath"]["mode_path"],page_list=parser["ModulePath"]["page_list"],
                        matrix_path=parser["ModulePath"]["matrix_path"])
        self.queueSize = queueSize


    def run(self):
        self.target = __WaitToCrawler__.get()
        print(datetime.datetime.now())
        print("Size of queue ", __WaitToCrawler__.qsize())
        print("Size of target ", len(self.target[1]["pages"]))
        self.MapThread()
        time.sleep(600)

        # 停止條件
        while (not __WaitToCrawler__.queue == []) or not(self.target[1]["pages"] == []):
            print(datetime.datetime.now())
            print("Size of queue ",__WaitToCrawler__.qsize())
            print("Size of target ", len(self.target[1]["pages"]))
            if self.target[1]["pages"] ==[]:
                self.target = __WaitToCrawler__.get()
            time.sleep(60)
            self.refresh()

        print("finish")
    def MapThread(self):
        # self.db = dbConnect()
        # self.db.connect()
        self.ThreadQueue = Queue()

        # 分配工作
        for __ in range(0,self.queueSize):
            tmp = ChildProcess(target=self.target[1]["pages"],TF=self.TF,user=self.target[1]["username"],db=self.db)
            self.ThreadQueue.put(tmp)
            tmp.start()

    def refresh(self):

        # 檢查 Thread 的健康狀態
        for __ in range(0,self.ThreadQueue.qsize()):

            # 如果死掉就拿出來
            tmp = self.ThreadQueue.get()
            if (tmp._is_stopped == True):
                continue
            else :
                self.ThreadQueue.put(tmp)

            if tmp.gettarget ==[]:
                tmp.settarget = self.target

        # 讓數量湊到滿
        for __ in range(self.ThreadQueue.qsize(),self.queueSize):
            tmp = ChildProcess(target=self.target[1]["pages"],TF=self.TF,user=self.target[1]["username"],db=self.db)
            self.ThreadQueue.put(tmp)
            tmp.start()




import requests
# todo impore and remove this readability
from module.v1.pageQueue_process.readability import Readability

class ChildProcess(Thread):
    # target 是 list
    target = None
    TF = None
    db = None
    user = None
    def __init__(self,target,TF,user,db):
        Thread.__init__(self)
        self.target = target
        self.TF = TF
        self.user = user
        self.db = dbConnect()
        self.db.connect()
    def crawler(self,target):
        response = requests.get(target)
        content = Readability(response.text, target).content

        return content
    def run(self):

        while( self.target != []):

            tar = self.target.pop(0)
            if tar == None or tar == []:
                continue
            # 暴力更新 user 喜好QQ
            tar["username"] = self.user
            sql = "delete from users_tags_relation where userid = %(username)s"
            self.db.execute(sql, tar)
            self.db.commit()
            # 加速
            # 先看看有沒有在資料庫裡面
            # 有的話就不用爬，沒有才爬
            # 這是判斷沒有的情況
            sql = "select * from pages where url = %s"
            pageid = self.db.query(sql, tar["url"])
            if (pageid == ()):
                indb = True
                try:
                    content = self.crawler(tar["url"])
                    result = self.TF.tags(target=content)
                    tar["descr"] = content
                    sql = "insert into pages(title,url,descr) values(%(title)s,%(url)s,%(descr)s);"
                    self.db.execute(sql,tar)
                    self.db.commit()
                except:
                    continue

            else :
                try:
                    sql = "select tagid from pages_tags_relation where pageid = %s"
                    tags = self.db.query(sql,pageid[0]["id"])
                    sql = "insert into users_pages_relation(pageid,userid) values(" +\
                                    "(select id from pages where url = %(url)s limit 1)," +\
                                    "(select id from users where username = %(username)s limit 1));"
                    self.db.execute(sql,tar)
                    self.db.commit()
                except:
                    continue
                for __ in tags:
                    try:
                        tar["tag"] = __
                        sql = "insert into users_tags_relation(tagid,userid) values(" +\
                                        "(select id from tags where tagname = %(tag)s limit 1)," +\
                                        "(select id from users where username = %(username)s limit 1));"
                        self.db.execute(sql,tar)
                        self.db.commit()
                    except:
                        continue
                continue

            try :
                sql = "insert into users_pages_relation(pageid,userid) values(" +\
                                "(select id from pages where url = %(url)s limit 1)," +\
                                "(select id from users where username = %(username)s limit 1));"
                self.db.execute(sql,tar)
                self.db.commit()
            except:
                continue

            for __ in result:
                tar["tag"] = __
                # todo finish insert query
                # 用來判斷 tags 不在 wikitag上的東東
                try:
                    try:
                        sql = "insert into pages_tags_relation(pageid,tagid) values("+\
                                   "(select id from pages where url = %(url)s limit 1),"+\
                                   "(select id from tags where tagname = %(tag)s limit 1));"
                        self.db.execute(sql,tar)
                        self.db.commit()
                    except:
                        sql = """INSERT INTO tags(tagname) VALUES(%(tag)s);"""
                        self.db.execute(sql, tar)
                        self.db.commit()
                        sql = "insert into pages_tags_relation(pageid,tagid) values("+\
                                   "(select id from pages where url = %(url)s limit 1),"+\
                                   "(select id from tags where tagname = %(tag)s limit 1));"
                        self.db.execute(sql,tar)
                    sql = "insert into users_tags_relation(tagid,userid) values(" +\
                                    "(select id from tags where tagname = %(tag)s limit 1)," +\
                                    "(select id from users where username = %(username)s limit 1) );"
                    self.db.execute(sql,tar)
                    self.db.commit()



                except:
                    logging.exception(datetime.datetime.now())
                    logging.log(sql%tar)

                    pass


        self.db.commit()

        self.db.close()
    def settarget(self,target):
        self.target = target
    def gettarget(self):
        return self.target

