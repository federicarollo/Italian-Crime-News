# DICE: a Dataset of Italian Crime Event news

DICE is a collection of Italian news articles describing 13 types of crime events that happened in the province of Modena, Italy, between the end of 2011 and 2021.

| Number of documents  |  10,395 |
| ------------- |  ------------- |
| Theft |        7,627 (73.37\%) |
| Drug dealing |    934 (8.99\%) |
| Aggression |      426 (4.10\%) |
| Illegal sale |    339 (3.26\%) |
| Mistreatment |    201 (1.93\%) |
| Robbery |         182 (1.75\%) |
| Scam |            171 (1.65\%) |
| Evasion |         135 (1.30\%) |
| Sexual violence | 124 (1.19\%) |
| Money laundering | 98 (0.94\%) |
| Kidnapping |       66 (0.63\%) |
| Murder |           54 (0.52\%) |
| Fraud |            38 (0.37\%) |

21 news articles have empty text.

The news articles are published online by the newspaper named <a href="https://gazzettadimodena.gelocal.it/modena">Gazzetta di Modena</a>.
Thanks to an agreement between the University of Modena and Reggio Emilia and the Gazzetta di Modena, signed on May 2022, the corpus is free to redistribute and transform without encountering legal copyright issues under an Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0).

Moreover, the dataset includes data derived from the abovementioned components thanks to the application of Natural Language Processing techniques. Some examples are the place of the crime event occurrence (municipality, area, address and GPS coordinates), the date of the occurrence, and the type of the crime events described in the news article obtained by an automatic categorization of the text.

At the moment, the data are organized in the following files:

_ "italian_crime_news.csv" is the actual dataset containing the main information about the news articles: id, url, title, sub title, text, place of the event occurrence (municipality, area, address), latitude and longitude (i.e., the GPS coordinates of the place), publication_date, event_date (i.e., when the crime occurred), newspaper_tag (i.e., crime category provided by the newspaper), word2vec_tag (i.e., crime category obtained by applying different categorization algorithms to the embeddings of news articles'body), newspaper/publisher.

_ "duplicate.csv" contains the records associating duplicate news articles: id_news1, id_news2, algorithm, similarity_score.

_ "algorithm.csv" contains information about the algorithms used to find duplicates contained in the file "duplicate.csv": id, name, numofdays, configurations.

_ "automatic_annotation.jsonl" contains the named entities found by Tint, the time expressions identified by Heideltime, and DBpedia URIs obtained by DBpedia Spotlight from the text of the news for all the news articles in the dataset.

_ "annotation.jsonl" contains the automatic annotation mentioned above and the manual annotation of What was stolen in the theft, Where the theft occurred, Which store or company was robbed, Who is the thief or criminal, Who was mugged. This annotation is available for 131 news articles, since 42 of them are news articles not related to thefts or contain information abount more events, they have been not annotated.

_ "Linee guida per l'annotazione V2.1.pdf" is the file with the guidelines followed for the manual annotation.


We plan to increase the number of the current manual annotations to 1,000 documents by the end of 2023.

Other researchers can employ the dataset to apply other algorithms of text categorization and duplicate detection and compare their results with the benchmark. The dataset can be useful for several scopes, e.g., geo-localization of the events, text summarization, crime analysis, crime prediction, community detection, topic modeling, news recommendation.


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
