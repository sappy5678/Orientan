from nltk.corpus import sinica_treebank
import nltk
from xml.etree.ElementTree import Element, SubElement, tostring
import xml.etree.ElementTree
import jieba
import re
from datetime import datetime
import xml.etree.cElementTree as ET
root = Element('root')

context = xml.etree.ElementTree.iterparse( source='D:\DATA\learn\python\\recommand system\Orientan\wiki\zn\zhwiki-latest-pages-articles1.xml', events=("start", "end"))
# turn it into an iterator
context = iter(context)

# get the root element
event, xmlroot = context.__next__()
flag = False
for event, elem in context:


    if elem.tag == "{http://www.mediawiki.org/xml/export-0.10/}page" and event == "start":
        page = SubElement(root, 'page')
        flag = True
        continue

    if(elem.tag =="{http://www.mediawiki.org/xml/export-0.10/}text" and event == "start" and flag == True) :
        if(page not in root):
            continue
        if(elem.text==None ):
            try:
                print(__.text)
                root.remove(page)
                flag = False
            except:
                pass
            continue
        elif (re.match("'''这个页面现在已经作废，相关信息请参看", elem.text) or re.match("'''這個頁面現在已經作廢，相關信息請參看", elem.text)):
            root.remove(page)
            flag = False
            continue
        else:
            __ = SubElement(page, 'text')
            __.text = elem.text
            continue


    if elem.tag == "{http://www.mediawiki.org/xml/export-0.10/}title" and event == "start" and flag == True:
        if(elem.text == None):
            continue
        if(re.match("Wikipedia:",elem.text) or  re.match("Help:",elem.text)):
            try:
                root.remove(page)
                flag = False
            except:
                pass
            continue
        __ = SubElement(page, 'title')
        __.text = elem.text
        continue

    if elem.tag == "{http://www.mediawiki.org/xml/export-0.10/}redirect" and event == "start" and flag == True:
        if(page not in root):
            continue
        root.remove(page)
        flag = False
        continue


    if event == "end" and elem.tag == "{http://www.mediawiki.org/xml/export-0.10/}page":
        xmlroot.clear()

print(tostring(root,encoding="UTF-8").decode("utf-8") )
f=open("tmp.xml",mode='w', encoding = 'utf8')
f.write(tostring(root,encoding="UTF-8").decode("utf-8") )
