package sastut

/**
 * An object that sets up the configuration for command-line options using
 * Scallop and returns the options, ready for use.
 */
object PolarityExperimentOpts {

  import org.rogach.scallop._
  
  def apply(args: Array[String]) = new ScallopConf(args) {
    banner("""
Classification application.

For usage see below:
	     """)
    val method = opt[String]("method", default=Some("L2R_LR"), descr="The type of solver to use. Possible values: majority , lexicon, or any liblinear solver type.")

    val cost = opt[Double]("cost", default=Some(1.0), validate = (0<), descr="The cost parameter C. Bigger values means less regularization (more fidelity to the training set).")

    val trainfiles = opt[List[String]]("train", descr="The files containing training events.")
    val evalfiles = opt[List[String]]("eval", descr="The files containing evalualation events.")
    val extended = opt[Boolean]("extended", short = 'x', descr="Use extended features.")
    val help = opt[Boolean]("help", noshort=true, descr="Show this message")
    val detailed = opt[Boolean]("detailed")
    val verbose = opt[Boolean]("verbose")
  }
}


/**
 * A standalone application that is the entry point for running sentiment analysis
 * experiments on the Twitter datasets in data/classify.
 */
object PolarityExperiment {

  import java.io.File
  import nak.NakContext._
  import nak.util.ConfusionMatrix

  def main(args: Array[String]) {

    val opts = PolarityExperimentOpts(args)

    val examples = 
      for (file <- opts.trainfiles(); ex <- DatasetReader(new File(file))) 
        yield ex

    lazy val featurizer = if (opts.extended()) ExtendedFeaturizer else BasicFeaturizer

    val classifier = opts.method() match {
      case "majority" => MajorityClassBaseline(examples.map(_.label))

      case "lexicon" => new LexiconRatioClassifier

      case solverDescription =>
        val solver = nak.liblinear.Solver(solverDescription)
        val config = new nak.liblinear.LiblinearConfig(solverType=solver,cost=opts.cost())
        new NakClassifier(trainClassifier(config, featurizer, examples))
    }

    val results = for (file <- opts.evalfiles()) yield {
      val evalSource = new File(file)
      val evalExamples = DatasetReader(evalSource)
      val evalTweets = evalExamples.map(_.features)
      val (predictions, confidence) = evalTweets.map(classifier).unzip
      val tweetTexts = evalTweets.map(_.content)
      val cmatrix = ConfusionMatrix(evalExamples.map(_.label), predictions, tweetTexts)
      (file, cmatrix)
    }
    
    for ((file,cmatrix) <- results) {
      println("################################################################################")
      println("Evaluating " + file)
      if (opts.detailed()) 
        println(cmatrix.detailedOutput) 
      else 
        println(cmatrix)
      println
    }

  }

}


