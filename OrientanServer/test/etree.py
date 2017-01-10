from __future__ import print_function
from xml.etree.ElementTree import Element, SubElement, tostring
import jieba
from sklearn import feature_extraction
from sklearn.feature_extraction.text import TfidfVectorizer


try:

    from lxml import etree

except ImportError:

    import xml.etree.ElementTree as etree

import re

RE = re.compile(r"""\[\[(File|Category):[\s\S]+\]\]|
        \[\[[^|^\]]+\||
        \[\[|
        \]\]|
        \'{2,5}|
        (<s>|<!--)[\s\S]+(</s>|-->)|
        {{[\s\S\n]+?}}|
        <ref>[\s\S]+</ref>|
        ={1,6}""", re.VERBOSE)
def _get_namespace(tag):
    namespace = re.match("^{(.*?)}", tag).group(1)

    if not namespace.startswith("http://www.mediawiki.org/xml/export-"):
        raise ValueError("%s not recognized as MediaWiki database dump"

                         % namespace)

    return namespace


def extract_pages(f):
    """Extract pages from Wikimedia database dump.



    Parameters

    ----------

    f : file-like or str

        Handle on Wikimedia article dump. May be any type supported by

        etree.iterparse.



    Returns

    -------

    pages : iterable over (int, string, string)

        Generates (page_id, title, content) triples.

        In Python 2.x, may produce either str or unicode strings.

    """

    elems = (elem for _, elem in etree.iterparse(f, events=["end"]))

    # We can't rely on the namespace for database dumps, since it's changed

    # it every time a small modification to the format is made. So, determine

    # those from the first element we find, which will be part of the metadata,

    # and construct element paths.

    elem = next(elems)

    namespace = _get_namespace(elem.tag)

    ns_mapping = {"ns": namespace}

    page_tag = "{%(ns)s}page" % ns_mapping

    text_path = "./{%(ns)s}revision/{%(ns)s}text" % ns_mapping

    id_path = "./{%(ns)s}id" % ns_mapping

    title_path = "./{%(ns)s}title" % ns_mapping

    for elem in elems:

        if elem.tag == page_tag:

            text = elem.find(text_path).text

            if text is None:
                continue

            yield (int(elem.find(id_path).text),

                   elem.find(title_path).text,

                   text)

            # Prune the element tree, as per

            # http://www.ibm.com/developerworks/xml/library/x-hiperfparse/

            # We do this only for <page>s, since we need to inspect the

            # ./revision/text element. That shouldn't matter since the pages

            # comprise the bulk of the file.

            elem.clear()

            if hasattr(elem, "getprevious"):

                # LXML only: unlink elem from its parent

                while elem.getprevious() is not None:
                    del elem.getparent()[0]


#######################################################################################################################
########################################上方未經過授權 或 GNU############################################################
#######################################################################################################################
# 刪除並提取需要的資訊到xml裡面
def delete_useless_info(file):
    # Test; will write article info + prefix of content to stdout



    root = Element('root')

    f = open("zhwiki-latest-pages-articles-zh.xml", mode="a",encoding="UTF-8")
    f.write("<root>")
    for pageid, title, text in extract_pages(file):
        if (re.match(("Wikipedia:|Help:|Category:|Portal:|Template:|MediaWiki:|File:|模塊:|Topic:"), title) or re.match("#REDIRECT|#redirect",text)):
            continue
        title = title

        text = text


        # page = SubElement(root, 'page')
        # __ = SubElement(page, 'title')
        # __.text = title
        # __ = SubElement(page, 'text')
        # __.text = text
        f.write("<page>")
        f.write("<title>" + title + "</title>")
        f.write("<text>" + text + "</text>")
        f.write("</page>")


        print("%d '%s' " % (pageid, title))

    f.write("</root>")
    # f.write(tostring(root,encoding="UTF-8").decode("utf-8"))

# 把檔案切細一點存
import dewiki###################################################### GUN
def store_multifile(file,path = "."):


    for pageid, title, text in extract_pages(file):
        if (re.match(("Wikipedia:|Help:|Category:|Portal:|Template:|MediaWiki:|File:|模塊:|Topic:"), title) or re.match("#REDIRECT|#redirect|# 重定向",text)):
            continue
        title = title

        # 去除雜訊
        text = dewiki.from_string(text)
        text = " ".join(jieba.lcut(text))
        page = {}
        page["id"] = pageid
        page["title"] = title
        page["text"] = text

        # page = SubElement(root, 'page')
        # __ = SubElement(page, 'title')
        # __.text = title
        # __ = SubElement(page, 'text')
        # __.text = text

        f = open(path+str(pageid)+".txt", mode="w", encoding="UTF-8")
        f.write(json.dumps(page))

        f.close()
        print(pageid)




from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
import os
import xml.etree.ElementTree as ElementTree
import numpy ,scipy.sparse.csr as csr_matrix


def save_sparse_csr(filename,array):
    numpy.savez(filename,data = array.data ,indices=array.indices,
             indptr =array.indptr, shape=array.shape )


def load_sparse_csr(filename):
    loader = numpy.load(filename)
    return csr_matrix((  loader['data'], loader['indices'], loader['indptr']),
                         shape = loader['shape'])

import pickle
def save_tfidf(filename,array):
    f = open(filename,mode="wb")
    pickle.dump(array,f)
    f.close()

def load_tfidf(filename):
    f = open(filename,mode="rb")
    __ = pickle.load(f)
    f.close()
    return __


import json
def train_idf_module(file_root,module = None):

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


    save_tfidf(mode_path+"module_matrix.pk",x)

    save_tfidf(mode_path+"module_vectorizer.pk",vectorizer)

    save_tfidf(mode_path + "page_list.pk", page)

    # minify page
    for __ in page:
        __.pop("text")

    save_tfidf(mode_path + "page_list_title.pk", page)

    # f = open("D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\module\idf.json", mode="w", encoding="UTF-8")
    # f.write(json.dumps(vectorizer.idf_))
    # f.close()

from sklearn.metrics.pairwise import cosine_similarity
def tags(target,mode_path,matrix_path,page_list):
    # 仔入模型
    module = load_tfidf(mode_path)
    page_list = load_tfidf(page_list)

    # 文字前處理
    # 切字
    target = [" ".join(jieba.cut(target))]

    # 統計
    target = module.transform(target)

    # 開始計算
    result = cosine_similarity(target,load_tfidf(matrix_path))
    #numpy.apply_along_axis(print_title,axis=0, arr=result[0][0:])
    __ = result[0][0:]
    result = KNN(20,__)
    for i in result:
        print(page_list[i[1]]["title"])
    print([page_list[i[1]]["title"] for i in result])
    # for value in range(0,len(__)):
    #     if __[value]>0.1:
    #         print(page_list[value]["title"])

def print_title(x):
    module_path = "D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\module\\"
    page_list = module_path + "page_list_title.pk"
    page_list = load_tfidf(page_list)
    for i in x:
        if i > 0.5:
            print(page_list[x]["title"])

import heapq
def KNN(Knumber,list):
    result = []
    for i in range(0, len(list)):
        if len(result) <= Knumber:
            heapq.heappush(result,(list[i],i))
            continue
        else:
            heapq.heappushpop(result, (list[i], i))
    return result
    return heapq.nsmallest(Knumber, result, key=None)
    # for i in range(0,len(list)):
    #     if len(result) ==0:
    #         result.append([0, list[0]])
    #         continue
    #     if min(result,key=lambda result:result[1])<list[i]:
    #         if len(result) <Knumber:
    #             result.append([i,list[i]])
    #             continue
    #         else :
    #             result.remove(min(result,key=lambda result:result[1]))
    #             result.append([i, list[i]])
    #             continue

    return result
if __name__ == "__main__":
    file = 'D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\zhwiki-latest-pages-articles-zh.xml'
    path = """D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\store_split\\"""
    # store_multifile(file, path)

    print("完成解析\n 開始訓練")

    # train_idf_module("D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\store_split", module=None)

    module_path = "D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\process\module\\"
    text = """古明地覺"""
    tags(target = text, mode_path = module_path+"module_vectorizer.pk", matrix_path = module_path+"module_matrix.pk", page_list = module_path+"page_list_title.pk")
    pass
