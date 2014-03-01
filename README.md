### emokids

## What is it?

This is a shell repository for code to create classifiers for polarity classification for tweets. The instructions may be found in [the description of homework five](https://github.com/utcompling/applied-nlp/wiki/Homework5) for the [@appliednlp](http://twitter.com/appliednlp) course.

This was @jasonbaldridge's private repository while getting the homework ready. The shell repository for the students to start with is the [gpp](https://github.com/utcompling/gpp) (Genuine People Personalities) repository. If you want to give the homework a try, you can start with that, and then compare your results to those given below.

## Why is it?

Some people [have it in for computational linguists](http://xkcd.com/114/), so we gotta fight back and start categorizing all those sentimental, emotion-laden wimps out there, with all their [#firstworldproblems](https://twitter.com/search?q=%23firstworldproblems&src=tyah). Let the machine learning commence!

## How well does it work?

Here are some highlights of the performance of the model so that students can compare with how they did.

### Dev sets

My best dev result for debate08 is with c=0.3.

```bash
$ bin/gpp exp --train data/debate08/train.xml --eval data/debate08/dev.xml -x -c .3
################################################################################
Evaluating data/debate08/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
406	30	18	|	454	negative
76	61	4	|	141	neutral
81	23	96	|	200	positive
----------------------------
563	114	118
negative neutral positive

--------------------------------------------------------------------------------
		70.82	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
72.11	89.43	79.84	negative
53.51	43.26	47.84	neutral
81.36	48.00	60.38	positive
...................................
68.99	60.23	62.69	Average
```

For HCR it is:

```bash
$ bin/gpp exp --train data/hcr/train.xml --eval data/hcr/dev.xml -x -c 1.2
################################################################################
Evaluating data/hcr/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
380	32	52	|	464	negative
93	50	18	|	161	neutral
108	10	54	|	172	positive
----------------------------
581	92	124
negative neutral positive

--------------------------------------------------------------------------------
		60.73	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
65.40	81.90	72.73	negative
54.35	31.06	39.53	neutral
43.55	31.40	36.49	positive
...................................
54.43	48.12	49.58	Average
```

### Test sets

Now on the test sets, using the above cost parameter values.

Debate08:

```bash
$ bin/gpp exp --train data/debate08/train.xml --eval data/debate08/test.xml -x -c .3
################################################################################
Evaluating data/debate08/test.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
308	44	20	|	372	negative
100	83	14	|	197	neutral
109	41	76	|	226	positive
----------------------------
517	168	110
negative neutral positive

--------------------------------------------------------------------------------
		58.74	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
59.57	82.80	69.29	negative
49.40	42.13	45.48	neutral
69.09	33.63	45.24	positive
...................................
59.36	52.85	53.34	Average
```


HCR:

```bash
$ bin/gpp exp --train data/hcr/train.xml --eval data/hcr/test.xml -x -c 1.2
################################################################################
Evaluating data/hcr/test.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
439	21	51	|	511	negative
62	49	22	|	133	neutral
80	15	59	|	154	positive
----------------------------
581	85	132
negative neutral positive

--------------------------------------------------------------------------------
		68.55	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
75.56	85.91	80.40	negative
57.65	36.84	44.95	neutral
44.70	38.31	41.26	positive
...................................
59.30	53.69	55.54	Average
```

### Stanford data

For Stanford, w/o optimizing the cost, and using both HCR and Debate08 as training sets.

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml -e data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
58	12	5	|	75	negative
7	24	2	|	33	neutral
34	27	47	|	108	positive
--------------------------
99	63	54
negative neutral positive

--------------------------------------------------------------------------------
		59.72	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
58.59	77.33	66.67	negative
38.10	72.73	50.00	neutral
87.04	43.52	58.02	positive
...................................
61.24	64.53	58.23	Average
```

So, what happens if we add more data? Let's use the dev files as well.

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml data/debate08/dev.xml data/hcr/dev.xml -e data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
61	9	5	|	75	negative
5	27	1	|	33	neutral
25	27	56	|	108	positive
-------------------------
91	63	62
negative neutral positive

--------------------------------------------------------------------------------
		66.67	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
67.03	81.33	73.49	negative
42.86	81.82	56.25	neutral
90.32	51.85	65.88	positive
...................................
66.74	71.67	65.21	Average
```

Turns out that a few more labels can go a long way in improving performance.

Heck, let's throw in everything we've got from the other datasets, so adding the test files.

```bash
bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml data/debate08/dev.xml data/hcr/dev.xml data/debate08/test.xml data/hcr/test.xml -e data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
59	11	5	|	75	negative
5	27	1	|	33	neutral
22	25	61	|	108	positive
--------------------------
86	63	67
negative neutral positive

--------------------------------------------------------------------------------
		68.06	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
68.60	78.67	73.29	negative
42.86	81.82	56.25	neutral
91.04	56.48	69.71	positive
...................................
67.50	72.32	66.42	Average
```

So, still going up, but starting to get diminishing returns from data in this domain.

Of course, we wouldn't do this in a careful research study, but it was worth doing here just to show what happens.

### Emoticons

Emoticons help if you only consider positive/negative. The neutrals they add are quite bad.

Here's without emoticon data and just the HCR and debate training sets.

```bash
$ bin/gpp exp --train data/hcr/train.xml data/debate08/train.xml --eval data/stanford/dev.xml -x################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
58	12	5	|	75	negative
7	24	2	|	33	neutral
34	27	47	|	108	positive
--------------------------
99	63	54
negative neutral positive

--------------------------------------------------------------------------------
		59.72	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
58.59	77.33	66.67	negative
38.10	72.73	50.00	neutral
87.04	43.52	58.02	positive
...................................
61.24	64.53	58.23	Average
```

Things basically die with the emoticon data (as you likely found if you tried it yourself):

```bash
$ bin/gpp exp --train data/emoticon/train.xml data/hcr/train.xml data/debate08/train.xml --eval data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
14	57	4	|	75	negative
0	33	0	|	33	neutral
4	63	41	|	108	positive
--------------------------
18	153	45
negative neutral positive

--------------------------------------------------------------------------------
		40.74	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
77.78	18.67	30.11	negative
21.57	100.00	35.48	neutral
91.11	37.96	53.59	positive
...................................
63.49	52.21	39.73	Average
```

What if we just keep the positive and negative examples from the emoticons, but use all training instances from HCR and debate?

```bash
$ bin/gpp exp --train data/emoticon/train.xml data/hcr/train.xml data/debate08/train.xml --eval data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
53	0	22	|	75	negative
8	4	21	|	33	neutral
23	0	85	|	108	positive
--------------------------
84	4	128
negative neutral positive

--------------------------------------------------------------------------------
		65.74	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
63.10	70.67	66.67	negative
100.00	12.12	21.62	neutral
66.41	78.70	72.03	positive
...................................
76.50	53.83	53.44	Average
```

So, better overall accuracy, though F-score average dives because of the poor recall of neutrals.

But, let's try evaluating without the neutrals and see if emoticon stuff can help for that subset. Here's without the emoticon data (scoring on only the positive/negative examples).

```bash
$ bin/gpp exp --train data/hcr/train.xml data/debate08/train.xml --eval data/stanford/dev.xml -x
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
69	6	|	75	negative
52	56	|	108	positive
-----------------
121	62
negative positive

--------------------------------------------------------------------------------
		68.31	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
57.02	92.00	70.41	negative
90.32	51.85	65.88	positive
...................................
73.67	71.93	68.15	Average
```

Here's with the emoticon data:

```bash
$ bin/gpp exp --train data/emoticon/train.xml data/hcr/train.xml data/debate08/train.xml --eval data/stanford/dev.xml -x 
################################################################################
Evaluating data/stanford/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
51	24	|	75	negative
23	85	|	108	positive
------------------
74	109
negative positive

--------------------------------------------------------------------------------
		74.32	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
68.92	68.00	68.46	negative
77.98	78.70	78.34	positive
...................................
73.45	73.35	73.40	Average
```

This suggests that improvements can be obtained from emoticon labels, but more sophistication is required, e.g. using a two stage classification setup (first subjective vs objective and then positive vs negative, for the subjective tweets).



### Training with multiple datasets 

Optimal for Debate08 dev:

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml --eval data/debate08/dev.xml -x -c .2
################################################################################
Evaluating data/debate08/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
417	23	14	|	454	negative
77	61	3	|	141	neutral
90	18	92	|	200	positive
----------------------------
584	102	109
negative neutral positive

--------------------------------------------------------------------------------
		71.70	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
71.40	91.85	80.35	negative
59.80	43.26	50.21	neutral
84.40	46.00	59.55	positive
...................................
71.87	60.37	63.37	Average
```

Optimal for HCR:

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml --eval data/hcr/dev.xml -x -c .7
################################################################################
Evaluating data/hcr/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
393	34	37	|	464	negative
94	47	20	|	161	neutral
107	14	51	|	172	positive
----------------------------
594	95	108
negative neutral positive

--------------------------------------------------------------------------------
		61.61	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
66.16	84.70	74.29	negative
49.47	29.19	36.72	neutral
47.22	29.65	36.43	positive
...................................
54.29	47.85	49.15	Average

```

Now for the test sets.

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml --eval data/debate08/test.xml -x -c .2
################################################################################
Evaluating data/debate08/test.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
322	37	13	|	372	negative
102	76	19	|	197	neutral
117	43	66	|	226	positive
----------------------------
541	156	98
negative neutral positive

--------------------------------------------------------------------------------
		58.36	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
59.52	86.56	70.54	negative
48.72	38.58	43.06	neutral
67.35	29.20	40.74	positive
...................................
58.53	51.45	51.45	Average

```

Which is worse than the 58.74/53.34 obtained with just the debate08 training data.

For HCR:

```bash
$ bin/gpp exp --train data/debate08/train.xml data/hcr/train.xml --eval data/hcr/test.xml -x -c .7
################################################################################
Evaluating data/hcr/test.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
438	25	48	|	511	negative
63	48	22	|	133	neutral
79	22	53	|	154	positive
----------------------------
580	95	123
negative neutral positive

--------------------------------------------------------------------------------
		67.54	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
75.52	85.71	80.29	negative
50.53	36.09	42.11	neutral
43.09	34.42	38.27	positive
...................................
56.38	52.07	53.56	Average
```

Which is worse than using just the HCR training data, which obtained 68.55/55.54. So, more data doesn't always help: it has to be the right data, or it has to be exploited in the right way. E.g. we could use any number of domain adaptation techniques that would try to get some benefit from the out of domain data without letting it dampen the effectiveness of the in domain data.

