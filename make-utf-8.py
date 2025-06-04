import pandas as pd

df = pd.read_csv("italian_crime_news.csv", encoding='cp1252')
df.to_csv("italian_crime_news_utf8.csv", index=False, encoding='utf-8')
