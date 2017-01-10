# import mysql.connector
import pymysql.cursors
class dbConnect():
    """
    連線資料庫用
    connect - 開始連線
    close - 關閉連線
    execute - 執行 sql 指令
    query - 查詢
    commit - 要讓資料庫保存結果
    quickexecute - 執行簡單指令
    quickquery - 執行簡單查詢
    """
    # class 的變數
    cnx = None
    cursor = None
    # todo add customer connect data
    def __init__(self):
        pass
    def connect(self):
        self.cnx = pymysql.connect(user='yzujava201601', password='javae94xjp4',
                                      host='140.138.77.98',
                                      db='yzujava201601',
                                      charset='utf8',
                                      cursorclass=pymysql.cursors.DictCursor)


    def close(self):
        assert self.cnx != None, "cnx can't be None, check if connect first"
        self.cnx.close()

    def execute(self,SQLString,parameter):
        assert self.cnx != None,"cnx can't be None, check if connect first"
        with self.cnx.cursor() as cursor:
            cursor.execute(SQLString,parameter)
            result = cursor.fetchall ()

    def query(self,SQLString,parameter):
        assert self.cnx != None,"cnx can't be None, check if connect first"
        with self.cnx.cursor() as cursor:
            cursor.execute(SQLString,parameter)
            result = cursor.fetchall ()
        return result

    def queryOne(self,SQLString,parameter):
        assert self.cnx != None,"cnx can't be None, check if connect first"
        with self.cnx.cursor() as cursor:
            cursor.execute(SQLString,parameter)
            result = cursor.fetchone()
        return result

    def commit(self):
        assert self.cnx != None, "cnx can't be None, check if connect first"
        self.cnx.commit()

    def quickexecute(self,SQLString,parameter):
        self.connect()
        self.execute(SQLString,parameter)
        self.commit()
        self.close()

    def quickquery(self,SQLString,parameter):
        self.connect()
        resule = self.query(SQLString,parameter)
        self.commit()
        self.close()
        return resule
