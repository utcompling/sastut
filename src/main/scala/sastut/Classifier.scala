package sastut

import nak.NakContext._
import nak.core.FeaturizedClassifier
import nak.data.{FeatureObservation,Featurizer}

/**
 * A trait for text classification functions. The return value is a pair consisting of
 * the best label for the input and the confidence assigned to that label by the
 * classifier.
 */
trait TextClassifier[I] extends (I => (String, Double))

/**
 * A text classifier that assigns a single label to every text that is given to it.
 */
class MajorityClassBaseline(majorityClass: String, prob: Double)
  extends TextClassifier[Tweet] {
  def apply(content: Tweet) = (majorityClass, prob)
}

/**
 * An object that implements a function for calculating the majority
 * class label given a sequence of labels, and its probability in that
 * sequence. E.g. for the labels "yes, yes, no, yes" it should
 * return (yes, .75).
 */
object MajorityClassBaseline {
  import nak.util.CollectionUtil._

  def apply(labels: Seq[String]) = {
    val (majorityLabel, majorityProb) =
      labels
        .counts
        .mapValues(_.toDouble / labels.length)
        .maxBy(_._2)

    new MajorityClassBaseline(majorityLabel, majorityProb)
  }
}

/**
 * An object that implements a function for splitting a string into a
 * sequence of tokens.
 */
object Tokenizer {
  def apply(text: String) = chalk.lang.eng.Twokenize(text)
  //def apply(text: String) = text.split("\\s+")
}

class SimplestEverClassifier extends TextClassifier[Tweet] {
  def apply(tweet: Tweet) = {
    val tokens = Tokenizer(tweet.content)
    val prediction = 
      if (tokens.contains("good"))
	"positive"
      else if (tokens.contains("bad"))
	"negative"
      else
	"neutral"
    (prediction,1.0)
  }
}

object ImdbPolarity100 {

  val positive = Set("good","awesome","great","fantastic","wonderful","perfection","captures","wonderfully","powell","refreshing","flynn","delightful","gripping","beautifully","underrated","superb","delight","welles","unforgettable","touching","favorites","extraordinary","stewart","brilliantly","friendship","wonderful","magnificent","finest","marie","jackie","freedom","pleasantly","fantastic","terrific","outstanding","noir","ruth","marvelous","exceptional","excellent","poignant","chilling","gem","amazing","ralph","chan","bette","kelly","errol","overlooked","gritty","shines","portrait","innocence","powerful","ensemble","burns","remarkable","pitt","carol","barbara","favorite","norma","stunning","favourite","von","journey","perfect","crafted","appreciated","subtle","brilliant","recommended","daniel","troubled","consequences","wilder","bond","sensitive","prince","perfectly","haunting","emotions",":)","courage","loved","highly","rare","nominated","tony","everyday","irene","contrast","fabulous","rival","notch","helps","unique","intense","jimmy","today","awesome","balance","spectacular","provides")

  val negative = Set("bad","terrible","worst","sucks","awful","dumb","dude","cheap","trite","dracula","joke","random","fake","unbelievable","poor","screaming","utter","disappointing","cliché","irritating","supposed","disappointment","zombies","clichéd","asleep","mediocre","fails","unless","bad","annoying","mildly","bland","ludicrous","amateur","obnoxious","horribly","bother","unintentionally","dumb","costs","shark","embarrassed","plastic","ripped","turkey","junk","boring","rip-off","rubbish","useless","whatsoever","unbelievably","ridiculous","excuse","crappy","vampires","forgettable","avoid","dull","wooden","inept","ashamed","stupid","mess","garbage","embarrassing","badly","suck","terrible","worse","pile","dreadful","tedious","crap","cardboard","wasted","insulting","stupidity","idiotic","pathetic","amateurish","horrible","unconvincing","uninteresting","insult","uninspired","sucks","miserably","boredom","cannibal","godzilla","lame","wasting","remotely","awful","poorly","laughable","worst","lousy","redeeming","atrocious","pointless","blah","waste","unfunny","seagal")

}


object ImdbPolarity1000 {

  val positive = Set("good","awesome","great","fantastic","wonderful","perfection","captures","wonderfully","powell","refreshing","flynn","delightful","gripping","beautifully","underrated","superb","delight","welles","unforgettable","touching","favorites","extraordinary","stewart","brilliantly","friendship","wonderful","magnificent","finest","marie","jackie","freedom","pleasantly","fantastic","terrific","outstanding","noir","ruth","marvelous","exceptional","excellent","poignant","chilling","gem","amazing","ralph","chan","bette","kelly","errol","overlooked","gritty","shines","portrait","innocence","powerful","ensemble","burns","remarkable","pitt","carol","barbara","favorite","norma","stunning","favourite","von","journey","perfect","crafted","appreciated","subtle","brilliant","recommended","daniel","troubled","consequences","wilder","bond","sensitive","prince","perfectly","haunting","emotions",":)","courage","loved","highly","rare","nominated","tony","everyday","irene","contrast","fabulous","rival","notch","helps","unique","intense","jimmy","today","awesome","balance","spectacular","provides","dramas","unexpected","shy","tragic","happiness","davis","hong","seasons","walter","creates","beauty","carries","robinson","fascinating","touched","guilt","sons","lucas","aunt","satisfying","deeply","oscar","greatest","ann","portrayal","eerie","loving","incredible","mature","brazil","essence","hooked","winning","unusual","vhs","vengeance","complex","kung","ages","harry","debut","thrillers","rose","learns","sweet","lonely","spy","holmes","willis","warm","grim","urban","beautiful","solid","tears","feelings","mgm","paris","themes","thief","masterpiece","danny","lovable","joy","raw","quiet","subsequent","colorful","allows","treat","italy","charming","essential","focuses","heart","louis","anime","reveals","impact","importance","surprising","natural","castle","secrets","moving","funniest","kennedy","christie","subtitles","todd","season","witty","health","compelling","sharp","deeper","arthur","tragedy","fonda","eastwood","great","nicely","magical","smith","gorgeous","lovely","winner","rob","lucy","baseball","enjoyed","craig","jean","quaid","woo","tight","disagree","relate","princess","memorable","adventure","childhood","episodes","strong","realizes","traditional","strength","apartment","adds","legendary","empire","studios","holds","tense","driven","miike","cry","develops","realistic","edge","touches","animated","deals","gene","sides","portraying","waters","surreal","albert","impressed","environment","steals","colors","paced","packed","sunday","frank","sentimental","captured","sir","stays","nowadays","trek","leslie","maria","rules","cagney","midnight","rain","court","germany","anger","tale","won","simple","cowboy","sophisticated","nevertheless","fresh","golden","enjoyable","bugs","memories","magic","depiction","lovers","performances","supporting","emotional","identity","bears","relationship","brad","sky","dan","portrays","friendly","henry","jim","brings","handsome","donald","streets","greatly","available","ken","delivers","effectively","charlie","andrew","society","relationships","best","atmosphere","china","genius","owner","workers","simon","influence","britain","romantic","interpretation","surprisingly","tricks","match","bitter","refuses","stylish","brooks","rough","india","inner","patrick","emotionally","era","andy","ride","love","paul","unlike","greater","seeking","march","jonathan","tcm","meets","performance","city","france","dreams","hall","complicated","succeeds","glimpse","striking","perspective","carefully","le","effective","easy","works","rings","england","trilogy","concert","aired","future","nation","ward","individual","criticism","appreciate","master","inspired","sees","cook","definitely","both","victor","douglas","sports","marriage","brave","meeting","pleasant","joseph","dealing","page","william","awards","fantasy","keeping","jack","joan","personal","artist","determined","albeit","emily","accomplished","animation","royal","impressive","morgan","ultimate","jane","gives","discovers","innocent","segment","award","drew","ted","classic","h","father","moved","versions","growing","images","touch","river","families","quirky","sexuality","washington","nature","brian","timing","century","expressions","offers","pleasure","russell","melodrama","games","academy","eddie","brown","fine","job","claire","visually","tiger","mexico","countries","stood","remembered","ice","figures","believable","surprises","struggles","always","experiences","helen","anna","suspenseful","oliver","adventures","lives","overcome","australia","knowing","bound","sisters","authentic","broadway","realism","survival","personalities","players","color","young","kirk","gangster","french","reminiscent","trial","tradition","greek","dick","wwii","psychological","jr","generation","variety","twists","understanding","peace","howard","grandfather","grand","charm","tend","union","sent","grow","david","revolution","redemption","thanks","age","deep","europe","mainstream","pulls","tales","gift","life","jeff","brothers","war","family","true","shorts","civil","tribute","superior","york","epic","fox","world","share","documentaries","stands","appearances","fellow","enjoy","rogers","each","proud","hitchcock","julie","fits","human","jackson","approach","different","series","madness","loves","dennis","passion","moves","received","famous","genuine","uncle","south","reminds","arrives","musical","eric","campbell","tells","faithful","japan","desire","youth","adult","shows","nazi","provided","brutal","investigation","sympathetic","smile","dance","success","attitude","initially","individuals","visit","carter","alan","accurate","viewing","league","finding","performed","present","hilarious","horrors","discovered","stunts","atmospheric","nine","karloff","dirty","daily","position","frightening","begins","fun","crime","humanity","com","answers","battles","radio","continues","necessary","among","recently","depicted","san","prisoners","finds","combination","ring","james","detail","treasure","plays","inevitable","born","thoroughly","settings","brought","appropriate","spielberg","traveling","modern","roles","drama","decades","fu","nonetheless","romance","singing","vision","strongly","contemporary","losing","helping","vicious","shakespeare","julia","ordinary","especially","social","honor","sacrifice","piano","searching","happy","pictures","detective","wise","role","reveal","lose","max","planned","lewis","challenge","familiar","chinese","ways","michelle","genuinely","dated","sinister","episode","language","spirit","lynch","chief","presents","history","fourth","louise","higher","ryan","closely","warner","critics","justice","international","thrilling","chicago","remains","fully","visual","popular","techniques","hidden","african","cynical","dream","w","pride","struggle","surprised","reality","sometimes","u","information","irish","mixed","intelligent","john","keeps","welcome","comedies","late","tour","elizabeth","dancing","follows","forces","beings","bogart","silent","marry","puts","birth","caught","sandler","years","learned","wayne","minor","flaws","slowly","comedic","seat","travels","michael","murder","criminal","physical","stronger","johnny","grew","miss","queen","visuals","liked","de","africa","married","closer","identify","important","catch","experience","shop","vincent","played","restaurant","events","boss","bridge","flawed","also","often","likable","enemies","fame","believes","advantage","breath","wild","increasingly","format","documentary","levels","culture","disney","lawyer","regular","fortunately","deserved","lights","score","pace","silence","support","movement","younger","later","wealthy","own","rarely","glad","witness","equally","clean","evening","discover","met","political","artists","quest","twisted","team","battle","dark","light","particular","viewed","australian","imagery","protect","live","heaven","parker","festival","robert","jerry","recommend","cultural","miller","enjoying","established","shape","older","step","united","herself","studio","harsh","price","small","business","humorous","shadow","cold","details","animals","grows","poverty","early","cooper","song","relatively","opposite","cinema","proves","adam","including","decade","between","mary","projects","storytelling","seven","system","photography","well","successful","recognize","collection","exciting","humour","skill","murphy","still","release","engaging","ford","gordon","wind","instantly","thoughts","territory","genre","country","accept","media","broke","view","understood","parents","protagonists","become","bill","entertaining","focused","starring","mine","soundtrack","released","wide","allen","becoming","includes","although","uses","lucky","turner","neat","el","return","summer","form","thankfully","past","german","son","guest","cary","stories","lord","chemistry","very","red","california","ago","ambitious","win","normal","peter","may","creating","theatrical","notable","dramatic","travel","personally","breaking","matt","became","boys","choices","threat","escape","western","cinematography","foreign","actions","recent","halloween","suits","element","fate","british","plenty","theme","helped","his","throughout","whose","kong","roger","mother","harris","key","rights","certain","richard","riding","ray","screening","bruce","lived","japanese","rachel","stage","mrs","personality","study","print","songs","torn","various","classics","fiction","meet","universe","created","truly","rich","bringing","frame","cousin","indeed","building","led","usa","drawn","persona","others","addition","brother","day","central","wit","change","memory","wars","ironic","chance","combined","crowd","mitchell","vs")

  val negative = Set("bad","terrible","worst","sucks","awful","dumb","hide","actors","got","laughing","strike","am","deliver","hang","evil","had","basic","depressing","horrific","risk","finish","involves","twin","went","snow","shot","reporter","b","line","far","interest","was","add","talent","card","comparison","fast","felt","have","standing","ground","girls","ever","korean","technically","drinking","prove","nightmare","clearly","stolen","cruel","folks","religious","offer","left","girlfriend","cause","villain","weapons","fly","expectations","etc","religion","don","been","nude","drive","picked","because","happen","thousands","scorsese","bits","built","already","bright","run","mom","happened","skin","fair","product","purely","costume","group","impression","jumps","served","forward","fire","tall","machine","maker","tied","talk","reviewer","flashbacks","actual","title","list","huge","penn","mansion","christmas","comical","say","so","starts","read","happening","military","bollywood","plan","possible","answer","pacing","appeal","kind","gang","weird","executed","meant","st","ended","sell","like","writer","surely","holiday","drag","finished","production","students","cannot","twist","were","laughs","did","taxi","parody","expected","uncomfortable","forgive","gags","calls","crying","reviewers","bizarre","subplot","alex","tell","bed","filmmaker","corny","carry","walks","videos","sean","rated","i'd","seems","wait","sound","student","ii","sense","speaking","clips","merely","accidentally","woods","said","chase","beaten","treated","begin","comedian","oddly","funnier","result","store","speak","proper","getting","talks","bambi","plague","absolute","bodies","jail","silver","repeatedly","awkward","imagine","hate","turned","names","average","would","bank","start","they","guts","clothes","holocaust","false","producing","insane","mistakes","floor","falls","five","evidence","hadn't","movie","pink","rest","started","if","resembles","beer","looking","might","commit","absolutely","hunter","mike","yourself","beyond","hired","going","rescue","intended","weapon","sort","keaton","properly","only","wear","dean","required","quick","teach","your","legs","kid","belief","angry","chasing","or","contain","escapes","massive","edited","film-making","anyway","robbery","handful","iii","par","name","stuck","do","believe","crude","sleeping","straight","suit","free","stuff","theory","there","kinda","attacks","cares","dropped","theater","doesn't","making","desert","watching","boyfriend","forced","kept","cops","point","spends","laugh","try","million","material","simply","ideas","whole","unable","crystal","jump","dies","count","needed","creatures","corrupt","sympathy","turning","doing","piece","designed","should","confused","want","hair","pretty","actually","female","upset","enough","die","cutting","myself","exception","storyline","head","act","directing","decide","smoking","normally","filming","fault","sounds","chose","ron","reviews","expensive","slow","something","agents","attempted","rating","ruin","footage","running","terrorist","isn't","plane","ship","box","we're","somehow","freaks","removed","farce","driving","wrote","biggest","hill","buried","size","nasty","stop","frankenstein","apparent","describe","better","steven","shoots","hours","context","nor","humans","body","suddenly","intelligence","guys","boat","theatre","incredibly","cars","admittedly","fighter","animal","fell","non","original","murdered","then","blockbuster","suffers","drunk","ask","m","totally","thinks","make","kill","produce","off","you're","anywhere","cop","scream","mention","excited","wanted","could","shots","device","blows","dragon","van","anybody","exact","lighting","succeed","dead","happens","ghosts","drop","let","saves","can't","cuts","manage","shame","rolling","goofy","lines","bloody","camera","make-up","curiosity","single","gotta","they've","concept","decided","loud","word","entire","destroyed","door","attack","gun","horror","nurse","video","elsewhere","hundred","just","hood","car","stereotype","stay","ass","writing","soap","pay","maybe","stopped","rape","value","talking","else","trying","i'll","nobody","killed","desperately","attempting","hint","credits","attempts","lake","practically","exploitation","harrison","claims","blonde","sick","phone","qualities","spend","i'm","hanging","heads","dollars","chicken","dozen","scares","possibly","childish","ain't","spoilers","acceptable","college","any","even","otherwise","hopes","remaining","pass","exist","care","aren't","don't","spoof","conspiracy","miserable","editor","development","guns","lenny","bag","sign","thinking","cut","purpose","destroy","curse","dialogue","attacked","didn't","hardly","forgot","thrown","term","filmmakers","effects","flicks","sex","khan","gory","k","project","they're","explain","wondering","island","scientists","dubbed","scripts","kills","throws","virtually","call","obvious","scenario","t","complete","completely","damn","remote","spoiler","minute","props","campy","sitting","calling","predator","tries","competent","superman","disappointed","thirty","heck","friday","base","eat","wearing","god","blood","he'd","gay","lower","summary","teen","scary","hey","wood","acting","possessed","robots","writers","sleep","tired","accent","teeth","asking","walking","obviously","franchise","potential","whatever","wow","killing","review","imdb","wonder","d","promise","killer","jokes","seemed","cash","giant","victims","experiment","twins","tree","basically","they'd","weekend","deaths","sadly","plot","computer","thing","pregnant","looks","warning","dry","wrong","either","satan","positive","cant","research","anything","shut","paying","scare","bush","editing","category","frankly","somewhere","someone","millions","sequel","saving","reasonably","sit","walk","lou","budget","drink","virus","thin","cheesy","stereotypes","killings","no","propaganda","idea","wasn't","flick","naked","freddy","low-budget","tarantino","jerk","least","flash","holes","thats","favor","gross","rip","problem","dragged","half","guy","nuclear","hell","except","asian","dialog","franco","you'd","creature","racist","convince","why","remake","whoever","sequels","monsters","fail","mouth","forest","lying","hills","doll","cover","neither","mistake","sadistic","blair","please","originality","deserve","utterly","saying","makeup","flesh","wouldn't","sum","saved","lack","lacking","reason","demon","watchable","advice","mean","screenwriter","intentions","truck","disbelief","wolf","fill","meat","gonna","rent","looked","suppose","spent","empty","hour","channel","chainsaw","reasonable","failure","weren't","makers","tried","stock","assume","could've","proof","freak","gratuitous","effort","stick","stiff","none","hated","alien","grade","rental","burn","accents","write","silly","instead","skip","execution","clown","would've","promising","dear","warned","knife","guess","scientist","shoot","slasher","stereotypical","premise","low","bottom","substance","seriously","f","confusing","script","alas","bomb","terrorists","okay","producers","zero","throw","gag","monster","consists","load","unfortunately","zombie","logic","total","sake","explained","clichés","priest","supposedly","credibility","so-called","stomach","couldn't","barely","money","throwing","christian","nothing","apparently","hopkins","jesus","attempt","unpleasant","disaster","figured","torture","ok","rented","weak","chaney","decent","?","nudity","grave","downright","terribly","continuity","fat","endless","hoping","somebody","ruined","lesbian","unnecessary","breasts","yeah","mindless","errors","shallow","paper","b-movie","button","below","minutes","sounded","security","honestly","chick","ninja","bothered","santa","dollar","wanna","bunch","lacks","halfway","contrived","spending","aliens","brain","porn","gore","harold","plain","motivation","paint","flat","unrealistic","save","outer","sorry","bore","vampire","absurd","jaws","nowhere","predictable","sat","repetitive","cgi","painful","clue","bored","paid","failed","explanation","oh","retarded","ugly","generic","walked","cheese","lazy","ha","seconds","blame","disgusting","trash","offensive","painfully","werewolf","monkey","renting","cabin","pretentious","sucked","idiot","alright","nonsense","dude","cheap","trite","dracula","joke","random","fake","unbelievable","poor","screaming","utter","disappointing","cliché","irritating","supposed","disappointment","zombies","clichéd","asleep","mediocre","fails","unless","bad","annoying","mildly","bland","ludicrous","amateur","obnoxious","horribly","bother","unintentionally","dumb","costs","shark","embarrassed","plastic","ripped","turkey","junk","boring","rip-off","rubbish","useless","whatsoever","unbelievably","ridiculous","excuse","crappy","vampires","forgettable","avoid","dull","wooden","inept","ashamed","stupid","mess","garbage","embarrassing","badly","suck","terrible","worse","pile","dreadful","tedious","crap","cardboard","wasted","insulting","stupidity","idiotic","pathetic","amateurish","horrible","unconvincing","uninteresting","insult","uninspired","sucks","miserably","boredom","cannibal","godzilla","lame","wasting","remotely","awful","poorly","laughable","worst","lousy","redeeming","atrocious","pointless","blah","waste","unfunny","seagal")

}

class SmallLexiconClassifier extends TextClassifier[Tweet] {
  //val positive = Set("good")
  //val negative = Set("bad")

  //val positive = Set("good","awesome","great","fantastic","wonderful")
  //val negative = Set("bad","terrible","worst","sucks","awful","dumb")

  //import ImdbPolarity100._

  //import ImdbPolarity1000._
				   
  // Use Bing Liu's lexicon
  //import Polarity._

  val positive = ImdbPolarity1000.positive | Polarity.positive
  val negative = ImdbPolarity1000.negative | Polarity.negative

  def apply(tweet: Tweet) = {
    val content = tweet.content
    val tokens = Tokenizer(content)
    val positiveCount = tokens.count(positive).toDouble
    val negativeCount = tokens.count(negative).toDouble
    List(
      ("positive",positiveCount),
      ("negative",negativeCount),
      ("neutral",.9)
    ).sortBy(_._2).last
  }
}


/**
 * A classifier that counts positive and negative terms in a text and picks a
 * label based on these counts. The label "neutral" is chosen when there are 
 * equal numbers of positive and negative tokens (or zero of both).
 */
class LexiconRatioClassifier extends TextClassifier[Tweet] {
  import Polarity._

  // Return the number of positive tokens in the token sequence.
  def numPositiveTokens(tokens: Seq[String]): Int = tokens.count(positive)

  // Return the number of negative tokens in the token sequence.
  def numNegativeTokens(tokens: Seq[String]): Int = tokens.count(negative)

  def apply(tweet: Tweet) = {
    val content = tweet.content

    val tokens = Tokenizer(content)
    val numPositive = numPositiveTokens(tokens)
    val numNegative = numNegativeTokens(tokens)

    // Add a small count to each so we don't get divide-by-zero error
    val positiveScore = numPositive + .1
    val negativeScore = numNegative + .1

    // Let neutral be preferred if nothing is found, and go with neutral
    // if pos and neg are the same.
    val neutralScore =
      if (numPositive == numNegative) tokens.length
      else .2

    // Calculate a denominator so we can pretend we have probabilities.
    val denominator = positiveScore + negativeScore + neutralScore

    // Create pseudo-probabilities based on the counts
    val predictions =
      List(("positive", positiveScore / denominator),
        ("negative", negativeScore / denominator),
        ("neutral", neutralScore / denominator))

    // Sort and return the top label and its confidence
    predictions.sortBy(_._2).last
  }
}

/**
 * An adaptor class that allows a maxent model trained via OpenNLP Maxent to be
 * used in a way that conforms with the TextClassifier trait defined above.
 */
class NakClassifier[I](classifier: FeaturizedClassifier[String,I])
  extends TextClassifier[I] {

  def apply(content: I) = {
    val prediction = classifier.evalRaw(content).toIndexedSeq
    val (prob, index) = prediction.zipWithIndex.maxBy(_._1)
    (classifier.labelOfIndex(index), prob)
  }
}

object AttrVal {
  def apply(a: String, v: String) = FeatureObservation(a+":"+v)
}

object BasicFeaturizer extends Featurizer[Tweet,String] {
  def apply(tweet: Tweet) =
    Tokenizer(tweet.content).map(token => FeatureObservation(token))
}


/**
 * An implementation of a FeatureExtractor that extracts more information out
 * of a tweet than the DefaultFeatureExtractor defined in ClassifierUtil.scala.
 * This is the main part of the assignment.
 */
object ExtendedFeaturizer extends Featurizer[Tweet,String] {

  // Import any classes and objects you need here. AttrVal is included already.
  import scala.util.matching.Regex
  import Polarity._
  import English.stopwords

  // Define any fields, including regular expressions and helper objects, here.
  // For example, you may want to include the lexicon ration classifier here (hint),
  // and a Porter stemmer, and whatever else you think might help.

  // End of sentence marker
  private val EOS = "[-*-EOS-*-]"

  val lexClassifier = new LexiconRatioClassifier
  val stemmer = new chalk.lang.eng.PorterStemmer
  val Threepeat = """\w+(.)\1\1+\w+""".r
  val AllCaps = """[^\w]*[A-Z][A-Z]+[^\w]*""".r

  // A class to allow an implicit conversion for easy regex handling.
  class Matcher(regex: Regex) {
    def fullMatch(input: String) = regex.pattern.matcher(input).matches
    def hasMatch(input: String) = regex.findAllIn(input).length > 1
  }

  // The implicit conversion of Regex to Matcher.
  implicit def regexToMatcher(regex: Regex) = new Matcher(regex)

  def apply(tweet: Tweet) = {
    val content = tweet.content
    val tokens = Tokenizer(content).toSeq
    val contentTokens = tokens.filter(stopwords)
    val stems = tokens.map(stemmer(_))

    val unigrams =
      tokens
        .filterNot(stopwords)
        .map(token => AttrVal("unigram", token))
        .toSeq

    val polarityFeatures =
      tokens
        .map(_.replaceAll("#", ""))
        .flatMap { token =>
          if (positive(token)) Some(AttrVal("polarity", "POSITIVE"))
          else if (negative(token)) Some(AttrVal("polarity", "NEGATIVE"))
          else None
        }

    val bigrams = (Seq(EOS) ++ stems ++ Seq(EOS)).sliding(2).flatMap {
      case List(first, second) =>
        Some(AttrVal("bigram", first + "::" + second))
      case _ => None
    }

    val emphasis = tokens.flatMap { token =>
      if (Threepeat.hasMatch(token)) Some("[-THREEPEAT-]")
      else if (AllCaps.fullMatch(token)) Some("[-ALLCAPS-]")
      else if (token.endsWith("!")) Some("[-EXCLAMATION-]")
      else None
    }
    
    val emphasisFeatures = emphasis.map(AttrVal("emphasis",_))

    //val trigrams = (Seq(EOS, EOS) ++ stems ++ Seq(EOS, EOS)).sliding(3).flatMap {
    //  case List(first, second, third) =>
    //    Some(AttrVal("trigram", first + "::" + second + "::" + third))
    //  case _ => None
    //}

    val subjectiveTokens =
      contentTokens.flatMap { token =>
        if (positive(token)) Some("[-POSITIVE-]")
        else if (negative(token)) Some("[-NEGATIVE-]")
        else Some("[-NEUTRAL-]")
      }

    val bigramPolarity = (Seq(EOS) ++ subjectiveTokens ++ Seq(EOS)).sliding(2).flatMap {
      case List(first, second) =>
        Some(AttrVal("bigramPolarity", first + "::" + second))
      case _ => None
    }.toSeq

    (unigrams
      ++ Seq(AttrVal("lexratio", lexClassifier(tweet)._1))
      ++ bigrams
      ++ bigramPolarity
      ++ emphasisFeatures
     //++ trigrams
      ++ polarityFeatures)
  }
}
