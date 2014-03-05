### SAS Tutorial

## What is it?

This is a repository for demo code for [Jason Baldridge](http://www.jasonbaldridge.com)'s [Practical Sentiment Analysis Tutorial](http://sentimentsymposium.com/workshops.html) for the [Sentiment Analysis Symposium](http://sentimentsymposium.com) on March 5, 2014.

NOTE: this is currently under construction! The notes below will be updated over the next two days.

## Context

This demo code was pulled out multiple sources, but primarily from the solutions key to a homework for creating classifiers for polarity classification for tweets. The original instructions may be found in [the description of homework five](https://github.com/utcompling/applied-nlp/wiki/Homework5) for the [@appliednlp](http://twitter.com/appliednlp) course.

### Compiling

Do the following to build the code. Note: you'll need a Linux or Mac machine.

Clone the repository first (I'll make a direct download soon). This will create a directory 'sastut' with all the code and data. I'll assume it is in your home directory.

```bash
$ cd ~/sastut
$ ./build compile
```

If this succeeds, you should be ready to go!

### Data

The data is in the `sastut/data` directory. Each dataset has its own subdirectory. 

The IMDB data is not included, so you need to [download it](http://ai.stanford.edu/~amaas/data/sentiment/aclImdb_v1.tar.gz) and unpack it in the data directory. Here's an easy way to do it if you have `wget`.

```bash
$ cd ~/sastut/data
$ wget http://ai.stanford.edu/~amaas/data/sentiment/aclImdb_v1.tar.gz
$ tar xvf aclImdb_v1.tar.gz
```

### Running experiments

Here's how to run the super simple "good/bad" classifier and test it on the debate08 data:

```bash
$ bin/sastut exp --eval data/debate08/dev.xml -m simple
################################################################################
Evaluating data/debate08/dev.xml
--------------------------------------------------------------------------------
Confusion matrix.
Columns give predicted counts. Rows give gold counts.
--------------------------------------------------------------------------------
5	442	7	|	454	negative
1	140	0	|	141	neutral
0	182	18	|	200	positive
--------------------------
6	764	25
negative	neutral	positive

--------------------------------------------------------------------------------
		20.50	Overall accuracy
--------------------------------------------------------------------------------
P	R	F
83.33	1.10	2.17	negative
18.32	99.29	30.94	neutral
72.00	9.00	16.00	positive
...................................
57.89	36.46	16.37	Average
```

You can give multiple evaluation sets by including the other files.

```bash
$ bin/sastut exp --eval data/debate08/dev.xml data/hcr/dev.xml  data/stanford/dev.xml -m simple
```

Here's how to train a logistic regression classifier on the Debate08 data and evaluate on the other sets:

```bash
$ bin/sastut exp --train data/debate08/train.xml --eval data/debate08/dev.xml data/hcr/dev.xml  data/stanford/dev.xml -m L2R_LR
```

Use the -x option to use the extended features.

```bash
$ bin/sastut exp --train data/debate08/train.xml --eval data/debate08/dev.xml data/hcr/dev.xml  data/stanford/dev.xml -m L2R_LR -x
```

Tip: use the "-d" (or "--detailed") option to output the detailed breakdown of which items where correctly labeled, which were incorrectly labeled, separated out by kind of error.

Use "small_lexicon" to use the small lexicon example. To try different lexicons, you need to modify the class SmallLexiconClassifier in the file `src/main/scala/sastut/Classifier.scala` and recompile. (I'll make this easier later today.) 

For example, to use Bing Liu's lexicon, you'll need to comment out these lines:

```scala
  val positive = Set("good","awesome","great","fantastic","wonderful")
  val negative = Set("bad","terrible","worst","sucks","awful","dumb")
```

And uncomment the second of these two lines:

```scala
  // Use Bing Liu's lexicon
  //import Polarity._
```

To run the simple lexicon on IMDB data (this will take longer because there are many more documents):

```bash
$ bin/sastut exp --eval data/aclImdb/test/labeledBow.feat -m small_lexicon -f imdb
```

Train a logistic regression model using bag-of-words features:

```bash
$ bin/sastut exp --train data/aclImdb/train/labeledBow.feat --eval data/aclImdb/test/labeledBow.feat -m L2R_LR -f imdb
```

Note: this uses the pre-extracted features provided by Chris Potts and colleagues. We can also run on the raw text and use our own feature extraction code:

```bash
$ bin/sastut exp --train data/aclImdb/train/ --eval data/aclImdb/test/ -m L2R_LR -f imdb_raw
```

Use "-x" to use extended features.

Finally, you can give multiple training set files to the `--train` option:

```bash
$ bin/sastut exp --train data/hcr/train.xml data/debate08/train.xml --eval data/debate08/dev.xml data/hcr/dev.xml data/stanford/dev.xml -m L2R_LR
```

### Sentiment lexicon expansion

To do the IMDB sentiment lexicon using log-likelihood ratios:

```bash
$ bin/sastut run sastut.LLR data/aclImdb/
```

I'll provide more detail in this README later, but this is enough to start playing around.

### NLP pipeline example

There is a very simple [OpenNLP](http://apache.opennlp.org) pipeline that you can use to analyze raw texts. Run it as follows:

```bash
$ bin/sastut run sastut.EnglishPipeline data/vinken.txt
```

You can provide any text as an argument for it to be analyzed.