"""
@ author sappy5678
@ email  sappy5678@gmail.com

這是用來集中放全域變數的東東
"""
from queue import PriorityQueue
# 全域變數 global variable

# 等待爬下來的queue
# 0 - 馬上爬 , 1 - 新使用者 , 2 - 檢查網站是否更新
__WaitToCrawler__ = PriorityQueue()