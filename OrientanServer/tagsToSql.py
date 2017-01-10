# import mysql.connector
import pymysql.cursors

cnx = pymysql.connect(user='yzujava201601', password='javae94xjp4',
                              host='140.138.77.98',
                              db='yzujava201601',
                              charset='utf8',
                              cursorclass=pymysql.cursors.DictCursor)

with cnx.cursor() as cursor:
    insert_tags = """INSERT INTO tags(tagname) VALUES(%(tags)s);"""

    string = ""
    with open("D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\zhwiki-latest-all-titles",encoding="utf8") as file:
        file.readline()
        i = 0
        tmp = 500
        for line in range(0,4837914):
            # t = file.readline().split("\t")[1][:-1]
            text = {"tags":file.readline().strip()[2:]}
            # string+=((insert_tags%text))
            try:
                cursor.execute(insert_tags,text)
            except:
                pass
            i+=1

            if tmp <=i:
                print(i)
                cnx.commit()
                tmp = i + 500


    cnx.commit()



cnx.close()