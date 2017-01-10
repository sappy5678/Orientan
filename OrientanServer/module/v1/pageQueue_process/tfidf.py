
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import jieba
import os
import xml.etree.ElementTree as ElementTree
import numpy ,scipy.sparse.csr as csr_matrix
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfVectorizer
import heapq
import pickle
import json
from sklearn.metrics.pairwise import cosine_similarity
class tfidf():
    """
    計算tfidf用的
    """
    module = None
    page_list = None
    matrix = None
    def __init__(self,mode_path,page_list,matrix_path):
        # 讀入模型
        self.module = self.load_tfidf(mode_path)
        self.page_list = self.load_tfidf(page_list)
        self.matrix = self.load_tfidf(matrix_path)

    def save_tfidf(self,filename,array):
        f = open(filename,mode="wb")
        pickle.dump(array,f)
        f.close()

    def load_tfidf(self,filename):
        f = open(filename,mode="rb")
        __ = pickle.load(f)
        f.close()
        return __



    # 訓練模型
    def train_idf_module(self,file_root,module = None):

        page = []
        i =0
        # 計算詞頻
        vectorizer = TfidfVectorizer() if module == None else numpy.load(module)
        for file in os.listdir(file_root):
            f = open("D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\store_split\\" + file , encoding="UTF-8")
            __=json.load(f)
            #
            # if(i>30):
            #     break
            i+=1


            # 計算tfidf



            t = __["title"]
            te = __["text"]
            print(__["id"])
            page.append(__)
            f.close()

        # # 計算tf-idf
        x = vectorizer.fit_transform([v["text"] for v in page])
        # transformer = TfidfTransformer()
        # tfidf = transformer.transform(vectorizer)

        mode_path = "D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\module\\"
        # 寫入結果到文字本



            # f = open(path+str(page[i]["id"]), mode="w", encoding="UTF-8")
            # f.write(json.dumps(tfidfin))
            # f.close()


        self.save_tfidf(mode_path+"module_matrix.pk",x)

        self.save_tfidf(mode_path+"module_vectorizer.pk",vectorizer)

        self.save_tfidf(mode_path + "page_list.pk", page)

        # minify page
        for __ in page:
            __.pop("text")

            self.save_tfidf(mode_path + "page_list_title.pk", page)

    # 位文章上標籤
    def tags(self,target,number = 10):


        # 文字前處理
        # 切字
        target = [" ".join(jieba.cut(target))]

        # 統計
        target = self.module.transform(target)

        # 開始計算
        result = cosine_similarity(target,self.matrix)

        __ = result[0][0:]
        result = self.KNN(number,__)
        # for i in result:
        #     print(self.page_list[i[1]]["title"])
        # print(result)
        return [self.page_list[i[1]]["title"] for i in result]


    # KNN 推薦法
    def KNN(self,Knumber,list):
        result = []
        for i in range(0, len(list)):
            if len(result) <= Knumber:
                heapq.heappush(result,(list[i],i))
                continue
            else:
                heapq.heappushpop(result, (list[i], i))
        return result