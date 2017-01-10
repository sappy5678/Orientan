from threading import *
class t(Thread):
    def run(self):
        print(self.getName())
        self.isAlive
        return

q=t()
q.start()
print(q.isAlive)
t().start()
t().start()
t().start()
t().start()
t().start()
print(q._is_stopped)
from dbConnect import dbConnect
db = dbConnect()
db.connect()
u = db.query("select * from pages where id = %s;",392)
print(u)
db.close()
