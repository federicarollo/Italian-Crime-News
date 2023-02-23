# IAA Second Round

This folder contains the results of the second round of IAA. At the moment, it contains the following files:

_ "gold_standard.jsonl" contains the 30 news of the gold standard

_ "expert.jsonl" contains the annotations from the expert on the 30 news.

_ "annotator.jsonl" contains the annotations from the annotator on the 30 news.

The table shows the performances of the two annotators w.r.t. the gold standard.

<table>
  <tr>
    <th>Encoder</th>
	<th colspan="3">Exact Match</th>
	<th colspan="3">Partial Match</th>
  </tr>
  <tr>
    <th></th>
    <th>Precision</th>
    <th>Recall</th>
    <th>F1</th>
    <th>Precision</th>
    <th>Recall</th>
    <th>F1</th>
  </tr>
  <tr>
    <th>Expert</th>
    <td>89%</td>
    <td>83%</td>
    <td>86%</td>
    <td>92%</td>
    <td>87%</td>
    <td>90%</td>
  </tr>
  <tr>
    <th>Annotator</th>
    <td>86%</td>
    <td>85%</td>
    <td>85%</td>
    <td>91%</td>
    <td>90%</td>
    <td>90%</td>
  </tr>
</table>
