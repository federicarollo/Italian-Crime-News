# Italian Crime News

The dataset contains the main components of the news articles published online by the newspaper named <a href="https://gazzettadimodena.gelocal.it/modena">Gazzetta di Modena</a>: url of the web page, title, sub-title, text, date of publication, crime category assigned to each news article by the author.

The news articles are written in Italian and describe 11 types of crime events occurred in the province of Modena between the end of 2011 and 2021.

Moreover, the dataset includes data derived from the abovementioned components thanks to the application of Natural Language Processing techniques. Some examples are the place of the crime event occurrence (municipality, area, address and GPS coordinates), the date of the occurrence, and the type of the crime events described in the news article obtained by an automatic categorization of the text.

In the end, news articles describing the same crime events (duplicates) are detected by calculating the document similarity.

Now, we are working on the application of question answering to extract the 5W+1H and we plan to extend the current dataset with the obtained data.

Other researchers can employ the dataset to apply other algorithms of text categorization and duplicate detection and compare their results with the benchmark. The dataset can be useful for several scopes, e.g., geo-localization of the events, text summarization, crime analysis, crime prediction, community detection, topic modeling.

At the moment, the information is organized in the following files:

_ "italian_crime_news.csv", the actual dataset containing the main information about the news articles: id, url, title, sub title, text, municipality, area, address, publication_date, event_date, latitude, longitude, newspaper_tag, word2vec_tag, newspaper.

_ "duplicate.csv", containing the records associating duplicate news articles: id_news1, id_news2, algorithm, similarity_score.

_ "algorithm.csv", containing the informations about the algorithms used to find duplicates contained in the file "duplicate.csv": id, name, numofdays, configurations. 






**If the dataset is useful, please consider citing papers using the BibTex entry below.**

```
@inproceedings{rollo2020,
  author    = {Federica Rollo and
               Laura Po},
  editor    = {Jeff Z. Pan and
               Valentina A. M. Tamma and
               Claudia d'Amato and
               Krzysztof Janowicz and
               Bo Fu and
               Axel Polleres and
               Oshani Seneviratne and
               Lalana Kagal},
  title     = {Crime Event Localization and Deduplication},
  booktitle = {The Semantic Web - {ISWC} 2020 - 19th International Semantic Web Conference,
               Athens, Greece, November 2-6, 2020, Proceedings, Part {II}},
  series    = {Lecture Notes in Computer Science},
  volume    = {12507},
  pages     = {361--377},
  publisher = {Springer},
  year      = {2020},
  doi       = {10.1007/978-3-030-62466-8\_23}
}

@inproceedings{bonisoli2021,
  author    = {Giovanni Bonisoli and
               Federica Rollo and
               Laura Po},
  editor    = {Maria Ganzha and
               Leszek A. Maciaszek and
               Marcin Paprzycki and
               Dominik Slezak},
  title     = {Using Word Embeddings for Italian Crime News Categorization},
  booktitle = {Proceedings of the 16th Conference on Computer Science and Intelligence Systems, Online, September 2-5, 2021},
  pages     = {461--470},
  year      = {2021},
  url       = {https://doi.org/10.15439/2021F118},
  doi       = {10.15439/2021F118}
}

@inproceedings{rollo2021,
  author    = {Federica Rollo and
               Giovanni Bonisoli and
               Laura Po},
  editor    = {Ewa Ziemba and
               Witold Chmielarz},
  title     = {Supervised and Unsupervised Categorization of an Imbalanced Italian Crime News Dataset},
  booktitle = {Information Technology for Management: Business and Social Issues
               - 16th Conference, {ISM} 2021, and FedCSIS-AIST 2021 Track, Held as
               Part of FedCSIS 2021, Virtual Event, September 2-5, 2021, Extended
               and Revised Selected Papers},
  series    = {Lecture Notes in Business Information Processing},
  volume    = {442},
  pages     = {117--139},
  publisher = {Springer},
  year      = {2021},
  url       = {https://doi.org/10.1007/978-3-030-98997-2\_6},
  doi       = {10.1007/978-3-030-98997-2\_6}
}

@inproceedings{rollo2022,
  author    = {Federica Rollo and Laura Po and Giovanni Bonisoli},
  title     = {Online News Event Extraction for Crime Analysis},
  booktitle = {Proceedings of the 30th Italian Symposium on Advanced Database Systems,
               {SEBD} 2022, Tirrenia (PI), Italy, June 19-22, 2022},
  series    = {{CEUR} Workshop Proceedings}
  publisher = {CEUR-WS.org},
  year      = {2022}
}
```
