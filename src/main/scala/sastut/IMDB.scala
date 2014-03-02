package sastut

import io.Source.fromFile 
import collection.mutable

object LLR {

  def main(args: Array[String]) {
    val topdir = args.head
    val vocab = fromFile(topdir+"/imdb.vocab").getLines.toIndexedSeq

    val vocabSize = vocab.length
    val positiveCounts = Array.fill(vocabSize)(1.0/vocabSize)
    val negativeCounts = Array.fill(vocabSize)(1.0/vocabSize)
    
    for (line <- fromFile(topdir+"/train/labeledBow.feat").getLines) {
      val ratingRaw :: countsRaw = line.split(" ").toList
      val rating = ratingRaw.toInt
      val counts = countsRaw.map { wordCountString => 
	val Array(wordId, wordCount) = wordCountString.split(":").map(_.toInt)
	(wordId, wordCount)
      }
      
      if (rating > 5)
	for ((wordId, count) <- counts) positiveCounts(wordId) += count
      else
	for ((wordId, count) <- counts) negativeCounts(wordId) += count
    }

    val unsupCounts = Array.fill(vocabSize)(1.0/vocabSize)

    for (line <- fromFile(topdir+"/train/unsupBow.feat").getLines) {
      val ratingRaw :: countsRaw = line.split(" ").toList
      val rating = ratingRaw.toInt
      val counts = countsRaw.map { wordCountString => 
	val Array(wordId, wordCount) = wordCountString.split(":").map(_.toInt)
	(wordId, wordCount)
      }
      for ((wordId, count) <- counts) unsupCounts(wordId) += count
    }

    val posSum = positiveCounts.sum.toDouble
    val positiveProbs = positiveCounts.map(_/posSum)

    val negSum = negativeCounts.sum.toDouble
    val negativeProbs = negativeCounts.map(_/negSum)

    val unsupSum = unsupCounts.sum.toDouble
    val unsupProbs = unsupCounts.map(_/unsupSum)

    val llratios = (for {
      (pos, neg, unsup) <- (positiveProbs,negativeProbs,unsupProbs).zipped
      ss = if (unsup > 2.0/vocabSize) 1.0 else 0.0
    } yield ss*math.log(pos/neg)).toIndexedSeq
      
    val llratiosSorted = vocab.zip(llratios).sortBy(-_._2)

    println("\n---------------------")
    println("Highly positive terms.")
    //llratiosSorted.take(100).foreach(println) 
    println("\""+llratiosSorted.take(100).map(_._1).mkString("\",\"")+"\"")

    println("\n---------------------")
    println("Highly negative terms.")
    //llratiosSorted.takeRight(100).foreach(println) 
    println("\""+llratiosSorted.takeRight(100).map(_._1).mkString("\",\"")+"\"")


    //val er = fromFile(topdir+"/imdbEr.txt").getLines.map(_.toDouble).toIndexedSeq
    //val erSorted = vocab.zip(er).sortBy(-_._2)
    //println("\n---------------------")
    //println("er highly positive terms.")
    //erSorted.take(20).foreach(println) 
    //
    //println("\n---------------------")
    //println("er highly negative terms.")
    //erSorted.takeRight(20).foreach(println) 


  }

}
