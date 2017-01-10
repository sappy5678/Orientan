
# coding: utf-8

# # 測試tfidf
# 
# ### 測試資料
# 

# In[1]:

documents = (
"The sky is blue",
"The sun is bright",
"The sun in the sky is bright",
"We can see the shining sun, the bright sun",
"The flowers is red"
)


# ### 特徵提取

# In[2]:

from sklearn.feature_extraction.text import TfidfVectorizer
tfidf_vectorizer = TfidfVectorizer()
tfidf_matrix = tfidf_vectorizer.fit_transform(documents)
# print (tfidf_matrix.shape)
# print (tfidf_matrix)


# In[3]:

# # 看單字ID
# print (tfidf_matrix.todense())


# In[4]:

# 開始判斷
# 加入測資
str = ["The is Yellow"]
response = tfidf_vectorizer.transform(str)

# print( response)


# ## 使用

# In[5]:

# 引入 餘弦相似度
from sklearn.metrics.pairwise import cosine_similarity

# 算出相似度
tmp = cosine_similarity(response, tfidf_matrix)

print(tmp)
# In[6]:

# print(tfidf_vectorizer.get_feature_names())


# ## 保存並讀取模型

# In[7]:

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


# In[10]:

# 保存
save_tfidf("test_module.pk",tfidf_vectorizer)


# In[11]:

# 讀取
tmp = load_tfidf("test_module.pk")


# In[12]:

str = ["The is a red flowers"]
response = tmp.transform(str)

print( response)


# In[14]:

str = ["The is Yellow"]
response = tmp.transform(str)

print( response)


# In[ ]:



