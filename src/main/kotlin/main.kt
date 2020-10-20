import java.awt.geom.Point2D
import java.io.File
import kotlin.math.abs

fun main() {
    /*val gpx = GpxHandler("tracks/Oil Spill Hill.gpx")
    val name = gpx.getTrackName()
    val segment = gpx.track
    println(name)
    var activity = GpxHandler("tracks/leaderboard1.gpx")
    var extracted = activity.extractSegment(segment[0], segment.last())
    var result = average(segment, extracted)
    for (i in 2..5) {
        activity = GpxHandler("tracks/leaderboard$i.gpx")
        extracted = activity.extractSegment(segment[0], segment.last(), 0.0002)
        result = average(result, i, extracted, 1)
    }
    File("tracks/result.txt").printWriter().use { out ->
        result.forEach { point -> out.println("${point.x}, ${point.y}") }
    }
    // */

    val path = arrayListOf(
            Point2D.Double(49.081058967858553, -124.02711996808648),
            Point2D.Double(49.081045975908637, -124.02711996808648),
            Point2D.Double(49.080972969532013, -124.02720001526177),
            Point2D.Double(49.080885965377092, -124.02734803967178),
            Point2D.Double(49.080840032547712, -124.02750100940466),
            Point2D.Double(49.080785969272256, -124.02767200022936),
            Point2D.Double(49.080721009522676, -124.02785296551883),
            Point2D.Double(49.080659989267588, -124.02797802351415),
            Point2D.Double(49.080581031739712, -124.02812495827675),
            Point2D.Double(49.080480029806495, -124.02823400683701),
            Point2D.Double(49.080441975966096, -124.02822000905871),
            Point2D.Double(49.080329993739724, -124.02825999073684),
            Point2D.Double(49.080180963501334, -124.02823601849377),
            Point2D.Double(49.080051966011524, -124.02821497991681),
            Point2D.Double(49.079936966300011, -124.02820902876556),
            Point2D.Double(49.079830013215542, -124.02818203903735),
            Point2D.Double(49.079728005453944, -124.02809302322567),
            Point2D.Double(49.079631026834249, -124.0279839746654),
            Point2D.Double(49.079535976052284, -124.02786302380264),
            Point2D.Double(49.079461041837931, -124.02773503214121),
            Point2D.Double(49.079450983554125, -124.0277249738574),
            Point2D.Double(49.07931100577116, -124.02768800966442),
            Point2D.Double(49.079216038808227, -124.02768599800766),
            Point2D.Double(49.079162981361151, -124.02766403742135),
            Point2D.Double(49.079072959721088, -124.02768398635089),
            Point2D.Double(49.079072959721088, -124.02762296609581),
            Point2D.Double(49.079098021611571, -124.0275920368731),
            Point2D.Double(49.079147977754474, -124.02757200412452),
            Point2D.Double(49.079195000231266, -124.02752003632486),
            Point2D.Double(49.079196006059647, -124.02752095833421),
            Point2D.Double(49.079207992181182, -124.02746102772653),
            Point2D.Double(49.079205980524421, -124.02738701552153),
            Point2D.Double(49.079189971089363, -124.02737000025809),
            Point2D.Double(49.079131968319416, -124.02727402746677),
            Point2D.Double(49.079128028824925, -124.02725701220334),
            Point2D.Double(49.079111013561487, -124.02720001526177),
            Point2D.Double(49.079045969992876, -124.02718702331185),
            Point2D.Double(49.079029960557818, -124.02715299278498),
            Point2D.Double(49.078995008021593, -124.02705903165042),
            Point2D.Double(49.078991990536451, -124.02702902443707),
            Point2D.Double(49.078963994979858, -124.02698300778866),
            Point2D.Double(49.078944968059659, -124.02685903944075),
            Point2D.Double(49.07894697971642, -124.02678796090186),
            Point2D.Double(49.078951003029943, -124.02677597478032),
            Point2D.Double(49.078930970281363, -124.02681201696396),
            Point2D.Double(49.078917978331447, -124.02683003805578),
            Point2D.Double(49.078925019130111, -124.02685602195561),
            Point2D.Double(49.078915966674685, -124.02685602195561),
            Point2D.Double(49.078919989988208, -124.02687597088516),
            Point2D.Double(49.078925019130111, -124.02688896283507),
            Point2D.Double(49.078931976109743, -124.02696599252522),
            Point2D.Double(49.078897023573518, -124.02699499391019),
            Point2D.Double(49.078832985833287, -124.02705903165042),
            Point2D.Double(49.078729972243309, -124.0270550083369),
            Point2D.Double(49.07870901748538, -124.02704402804375),
            Point2D.Double(49.078650008887053, -124.02709599584341),
            Point2D.Double(49.078600974753499, -124.02710395865142),
            Point2D.Double(49.078588988631964, -124.02709499001503),
            Point2D.Double(49.078574990853667, -124.02708501555026),
            Point2D.Double(49.07855998724699, -124.02701896615326),
            Point2D.Double(49.078566022217274, -124.02700203470886),
            Point2D.Double(49.07855898141861, -124.02703195810318),
            Point2D.Double(49.078561998903751, -124.02703195810318),
            Point2D.Double(49.078565016388893, -124.02704302221537),
            Point2D.Double(49.07851698808372, -124.02709197252989),
            Point2D.Double(49.078471977263689, -124.02710999362171),
            Point2D.Double(49.078426966443658, -124.02708199806511),
            Point2D.Double(49.07838698476553, -124.02707302942872),
            Point2D.Double(49.078374998643994, -124.02708099223673),
            Point2D.Double(49.078358989208937, -124.02710697613657),
            Point2D.Double(49.078341973945498, -124.02711099945009),
            Point2D.Double(49.078345997259021, -124.02709297835827),
            Point2D.Double(49.07832101918757, -124.02711703442037),
            Point2D.Double(49.078304003924131, -124.02706498280168),
            Point2D.Double(49.0782780200243, -124.02704402804375),
            Point2D.Double(49.078228985890746, -124.0270359814167),
            Point2D.Double(49.078158996999264, -124.02706095948815),
            Point2D.Double(49.078094959259033, -124.02707101777196),
            Point2D.Double(49.078051960095763, -124.02706498280168),
            Point2D.Double(49.078027987852693, -124.02710898779333),
            Point2D.Double(49.077912988141179, -124.02714704163373),
            Point2D.Double(49.077867977321148, -124.02714997529984),
            Point2D.Double(49.077810980379581, -124.0271379891783),
            Point2D.Double(49.077792959287763, -124.02711996808648),
            Point2D.Double(49.077812992036343, -124.02710597030818),
            Point2D.Double(49.07778499647975, -124.02710697613657),
            Point2D.Double(49.077813997864723, -124.02713203802705),
            Point2D.Double(49.077801005914807, -124.02715902775526),
            Point2D.Double(49.077798994258046, -124.02718299999833),
            Point2D.Double(49.077809974551201, -124.02722499333322),
            Point2D.Double(49.07780796289444, -124.02728701941669),
            Point2D.Double(49.077770998701453, -124.02732096612453),
            Point2D.Double(49.077749038115144, -124.02736295945942),
            Point2D.Double(49.077729005366564, -124.02742004022002),
            Point2D.Double(49.077705033123493, -124.02748298831284),
            Point2D.Double(49.077646024525166, -124.027493968606),
            Point2D.Double(49.077617023140192, -124.02747502550483),
            Point2D.Double(49.077599002048373, -124.02744602411985),
            Point2D.Double(49.077583998441696, -124.02743303216994),
            Point2D.Double(49.077578969299793, -124.02739799581468),
            Point2D.Double(49.077585004270077, -124.02732901275158),
            Point2D.Double(49.077579975128174, -124.02732398360968),
            Point2D.Double(49.077579975128174, -124.02731702663004),
            Point2D.Double(49.077578969299793, -124.0273420047015),
            Point2D.Double(49.077583998441696, -124.02734602801502),
            Point2D.Double(49.077560026198626, -124.0273779630661),
            Point2D.Double(49.077562037855387, -124.02734099887311),
            Point2D.Double(49.077524989843369, -124.0273470338434),
            Point2D.Double(49.077543010935187, -124.02734301052988),
            Point2D.Double(49.077540999278426, -124.02733999304473),
            Point2D.Double(49.077566983178258, -124.02735600247979),
            Point2D.Double(49.077568994835019, -124.0273779630661),
            Point2D.Double(49.07754804007709, -124.02736203745008),
            Point2D.Double(49.07754996791482, -124.02735499665141),
            Point2D.Double(49.077559020370245, -124.02740000747144),
            Point2D.Double(49.077559020370245, -124.02744702994823),
            Point2D.Double(49.077562959864736, -124.02745499275625),
            Point2D.Double(49.077574023976922, -124.02744200080633),
            Point2D.Double(49.077587015926838, -124.02739296667278),
            Point2D.Double(49.077606964856386, -124.02739296667278),
            Point2D.Double(49.07761400565505, -124.02740998193622),
            Point2D.Double(49.077629009261727, -124.02740797027946),
            Point2D.Double(49.077665973454714, -124.02742498554289),
            Point2D.Double(49.07770000398159, -124.02740595862269),
            Point2D.Double(49.077736968174577, -124.02736203745008),
            Point2D.Double(49.077758006751537, -124.02730001136661),
            Point2D.Double(49.077787008136511, -124.02728299610317),
            Point2D.Double(49.077825983986259, -124.02715902775526),
            Point2D.Double(49.077844005078077, -124.02713103219867),
            Point2D.Double(49.077885998412967, -124.02708602137864),
            Point2D.Double(49.077925980091095, -124.02702902443707),
            Point2D.Double(49.07804399728775, -124.02702600695193),
            Point2D.Double(49.078093031421304, -124.02702097781003),
            Point2D.Double(49.078115997835994, -124.02704402804375),
            Point2D.Double(49.078180035576224, -124.02698996476829),
            Point2D.Double(49.078209036961198, -124.02693003416061),
            Point2D.Double(49.078267039731145, -124.02690002694726),
            Point2D.Double(49.078342979773879, -124.02691897004843),
            Point2D.Double(49.07838799059391, -124.02690698392689),
            Point2D.Double(49.078412968665361, -124.0269120130688),
            Point2D.Double(49.078440964221954, -124.02691402472556),
            Point2D.Double(49.078489998355508, -124.02687496505678),
            Point2D.Double(49.078541966155171, -124.02688602916896),
            Point2D.Double(49.078624024987221, -124.02691897004843),
            Point2D.Double(49.078707005828619, -124.02688100002706),
            Point2D.Double(49.078755034133792, -124.02688896283507),
            Point2D.Double(49.078826028853655, -124.02691301889718),
            Point2D.Double(49.078884031623602, -124.0268739592284),
            Point2D.Double(49.078953014686704, -124.02683297172189),
            Point2D.Double(49.079019986093044, -124.02698895893991),
            Point2D.Double(49.079099027439952, -124.02707302942872),
            Point2D.Double(49.07911797054112, -124.02708903886378),
            Point2D.Double(49.079208998009562, -124.02720998972654),
            Point2D.Double(49.079278986901045, -124.02730696834624),
            Point2D.Double(49.079323997721076, -124.02737997472286),
            Point2D.Double(49.079363979399204, -124.02748600579798),
            Point2D.Double(49.079370014369488, -124.02750000357628),
            Point2D.Double(49.079293990507722, -124.02755800634623),
            Point2D.Double(49.0792450401932, -124.02757996693254),
            Point2D.Double(49.079187037423253, -124.02763897553086),
            Point2D.Double(49.079045969992876, -124.02773603796959),
            Point2D.Double(49.079030966386199, -124.02774902991951),
            Point2D.Double(49.078962989151478, -124.02781398966908),
            Point2D.Double(49.078862993046641, -124.02797198854387),
            Point2D.Double(49.078810019418597, -124.02804298326373),
            Point2D.Double(49.078755034133792, -124.02815597131848),
            Point2D.Double(49.078711029142141, -124.02825001627207),
            Point2D.Double(49.078694013878703, -124.02832796797156),
            Point2D.Double(49.07868797890842, -124.02842997573316),
            Point2D.Double(49.078683033585548, -124.02862896211445),
            Point2D.Double(49.078630981966853, -124.02878796681762),
            Point2D.Double(49.078565016388893, -124.02892601676285),
            Point2D.Double(49.078497961163521, -124.02908803895116),
            Point2D.Double(49.078428978100419, -124.02919298037887),
            Point2D.Double(49.078378016129136, -124.02931896038353),
            Point2D.Double(49.078329987823963, -124.02948701754212),
            Point2D.Double(49.078324036672711, -124.02961802668869),
            Point2D.Double(49.078289000317454, -124.02970100753009),
            Point2D.Double(49.078249018639326, -124.02980502694845),
            Point2D.Double(49.078242983669043, -124.02980896644294),
            Point2D.Double(49.078201996162534, -124.02990896254778),
            Point2D.Double(49.078163020312786, -124.02997300028801),
            Point2D.Double(49.078115997835994, -124.03002496808767),
            Point2D.Double(49.078026982024312, -124.03018296696246),
            Point2D.Double(49.077963028103113, -124.03027500025928),
            Point2D.Double(49.077889015898108, -124.03037801384926),
            Point2D.Double(49.077801005914807, -124.03047096915543),
            Point2D.Double(49.077691035345197, -124.0305520221591),
            Point2D.Double(49.077596990391612, -124.03063600882888),
            Point2D.Double(49.077466987073421, -124.03075402602553),
            Point2D.Double(49.07735601067543, -124.03090699575841),
            Point2D.Double(49.077279986813664, -124.03102803044021),
            Point2D.Double(49.077235981822014, -124.03108703903854),
            Point2D.Double(49.077154006808996, -124.03122098185122),
            Point2D.Double(49.077111007645726, -124.03127102181315),
            Point2D.Double(49.077048981562257, -124.03139096684754),
            Point2D.Double(49.076967006549239, -124.03152097016573),
            Point2D.Double(49.076885031536222, -124.03167997486889),
            Point2D.Double(49.076816970482469, -124.03182900510728),
            Point2D.Double(49.076789980754256, -124.03197099454701),
            Point2D.Double(49.076784029603004, -124.03213100507855),
            Point2D.Double(49.076763996854424, -124.03225899673998),
            Point2D.Double(49.076742036268115, -124.03243702836335),
            Point2D.Double(49.076757039874792, -124.03258203528821),
            Point2D.Double(49.076744969934225, -124.03273601084948),
            Point2D.Double(49.076737007126212, -124.03298998251557),
            Point2D.Double(49.076714040711522, -124.03312803246081),
            Point2D.Double(49.076683027669787, -124.03327798470855),
            Point2D.Double(49.076656959950924, -124.03334998525679),
            Point2D.Double(49.076643968001008, -124.0334649849683),
            Point2D.Double(49.076568027958274, -124.03346003964543),
            Point2D.Double(49.076562998816371, -124.0334649849683)
    )
    val path2 = arrayListOf(
            Point2D.Double(49.081065002828836, -124.02713698334992),
            Point2D.Double(49.081031978130341, -124.02711301110685),
            Point2D.Double(49.080966012552381, -124.02719900943339),
            Point2D.Double(49.080883031710982, -124.02731996029615),
            Point2D.Double(49.080852018669248, -124.0273969899863),
            Point2D.Double(49.080790998414159, -124.02759497053921),
            Point2D.Double(49.080724027007818, -124.02775900438428),
            Point2D.Double(49.080711035057902, -124.02778498828411),
            Point2D.Double(49.080611960962415, -124.02795002795756),
            Point2D.Double(49.080551024526358, -124.0280360262841),
            Point2D.Double(49.080479023978114, -124.02806603349745),
            Point2D.Double(49.080457985401154, -124.02808497659862),
            Point2D.Double(49.080324964597821, -124.02813300490379),
            Point2D.Double(49.08023402094841, -124.02813501656055),
            Point2D.Double(49.080160008743405, -124.02814499102533),
            Point2D.Double(49.08008499071002, -124.02812898159027),
            Point2D.Double(49.080005027353764, -124.02810601517558),
            Point2D.Double(49.079931015148759, -124.02808103710413),
            Point2D.Double(49.079845016822219, -124.02804097160697),
            Point2D.Double(49.079795982688665, -124.02801901102066),
            Point2D.Double(49.07974099740386, -124.02796603739262),
            Point2D.Double(49.079652987420559, -124.02788498438895),
            Point2D.Double(49.079539999365807, -124.02776503935456),
            Point2D.Double(49.079457018524408, -124.02770301327109),
            Point2D.Double(49.079391974955797, -124.02767300605774),
            Point2D.Double(49.079382000491023, -124.02766495943069),
            Point2D.Double(49.079283010214567, -124.02765397913754),
            Point2D.Double(49.079147977754474, -124.0276369638741),
            Point2D.Double(49.079101039096713, -124.02762698940933),
            Point2D.Double(49.078984027728438, -124.02770997025073),
            Point2D.Double(49.078957038000226, -124.02773302048445),
            Point2D.Double(49.078937005251646, -124.02772597968578),
            Point2D.Double(49.078927030786872, -124.02772204019129),
            Point2D.Double(49.078812031075358, -124.02788096107543),
            Point2D.Double(49.078746987506747, -124.02802404016256),
            Point2D.Double(49.0786889847368, -124.0282019879669),
            Point2D.Double(49.078652020543814, -124.02834003791213),
            Point2D.Double(49.078616984188557, -124.02850499376655),
            Point2D.Double(49.078555963933468, -124.02873298153281),
            Point2D.Double(49.078507013618946, -124.02890296652913),
            Point2D.Double(49.078463008627295, -124.02907798066735),
            Point2D.Double(49.078406011685729, -124.02926104143262),
            Point2D.Double(49.078383967280388, -124.02933698147535),
            Point2D.Double(49.078349014744163, -124.02945499867201),
            Point2D.Double(49.078337028622627, -124.02955298312008),
            Point2D.Double(49.078310038894415, -124.029639987275),
            Point2D.Double(49.078262010589242, -124.02979396283627),
            Point2D.Double(49.078252958133817, -124.02981701306999),
            Point2D.Double(49.078167965635657, -124.02996797114611),
            Point2D.Double(49.078102000057697, -124.03009101748466),
            Point2D.Double(49.078014995902777, -124.03022596612573),
            Point2D.Double(49.077932015061378, -124.03034398332238),
            Point2D.Double(49.07785096205771, -124.03044096194208),
            Point2D.Double(49.077776027843356, -124.03052000328898),
            Point2D.Double(49.077673014253378, -124.0306070074439),
            Point2D.Double(49.077574023976922, -124.03067003935575),
            Point2D.Double(49.077489031478763, -124.03076198883355),
            Point2D.Double(49.077392974868417, -124.03085402213037),
            Point2D.Double(49.077367996796966, -124.03086902573705),
            Point2D.Double(49.077289961278439, -124.03095996938646),
            Point2D.Double(49.077247967943549, -124.03106197714806),
            Point2D.Double(49.077178984880447, -124.03118804097176),
            Point2D.Double(49.077112013474107, -124.03127001598477),
            Point2D.Double(49.077059039846063, -124.03135702013969),
            Point2D.Double(49.076987039297819, -124.03144201263785),
            Point2D.Double(49.076944962143898, -124.03152599930763),
            Point2D.Double(49.076886959373951, -124.03159003704786),
            Point2D.Double(49.07686298713088, -124.03172297403216),
            Point2D.Double(49.076872961595654, -124.03178801760077),
            Point2D.Double(49.076843038201332, -124.03206302784383),
            Point2D.Double(49.076815964654088, -124.03222597204149),
            Point2D.Double(49.076826022937894, -124.03233803808689),
            Point2D.Double(49.076802972704172, -124.0324979647994),
            Point2D.Double(49.076738012954593, -124.03275302611291),
            Point2D.Double(49.076685039326549, -124.0329609811306),
            Point2D.Double(49.076637011021376, -124.03310598805547),
            Point2D.Double(49.076579008251429, -124.03301596641541)
    )
    var newPath = average(path, path2)
    for (i in 1..9)
        newPath = average(newPath, path2)
    File("tracks/result.txt").printWriter().use { out ->
        newPath.forEach { point -> out.println("${point.x}, ${point.y}") }
    }// */
}

fun average(list1: List<Point2D>, list2: List<Point2D>)
        : List<Point2D> {
    return average(list1, 1, list2, 1)
}

fun average(list1: List<Point2D>, weight1: Int, list2: List<Point2D>, weight2: Int)
        : List<Point2D> {
    val DISTANCE_THRESHOLD = 0.00004496 // Equivalent to about 5 meters when using lat/lon
    // Assume each track is complete (endpoints pair with each other),
    // and 'travelling' in the same direction
    // Then each point from each line should find the closest point from the other track,
    // and average with that point
    val updated = ArrayList<Point2D>()
    // Average the starting point
    updated += Point2D.Double(
        (list1[0].x * weight1 + list2[0].x * weight2) / (weight1 + weight2),
        (list1[0].y * weight1 + list2[0].y * weight2) / (weight1 + weight2)
    )
    // Run through both lists and add average points to the new list
    val iter1 = list1.iterator()
    val iter2 = list2.iterator()
    // For each point, find if it's earlier than the one from the other list
    // I feel this is a bit of a naive solution. This method simply finds both
    // alternatives, then just adds whichever is closest to the previous point
    var p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
    var p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
    // Will this leave at least one item from each list remaining?
    while (iter1.hasNext() && iter2.hasNext()) {
        when {
            abs(p1.distance(updated.last()) - p2.distance(updated.last())) < DISTANCE_THRESHOLD -> {
                // If the two points are equidistant, average them
                updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
            p1.distance(updated.last()) < p2.distance(updated.last()) -> {
                updated += p1
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
            }
            else -> {
                updated += p2
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
        }
    }
    // At this point there will be only one point left on one list or the other
    // Add the required remaining points that aren't already accounted for
    // So long as the points in the list with remaining items are closer
    // than the last one from the other list, add those points to the updates
    when {
        iter1.hasNext() -> {
            while (iter1.hasNext() &&
                    p1.distance(updated.last()) < (p2.distance(updated.last()) - DISTANCE_THRESHOLD)) {
                // Add the point to the list
                updated += p1
                p1 = averageNearestPoint(iter1.next(), list2, weight1, weight2)
            }
            // There are now no more points on the second list, and the only
            // remaining points on the first are farther than the list2 point.
            // Add the average of the next two points
            updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            // Every remaining point should be averaged with the endpoint of list2
            iter1.forEachRemaining { point ->
                updated += Point2D.Double(
                    (point.x * weight1 + list2.last().x * weight2) / (weight1 + weight2),
                    (point.y * weight1 + list2.last().y * weight2) / (weight1 + weight2)
                )
            }
        }
        iter2.hasNext() -> {
            while (iter2.hasNext() &&
                    p2.distance(updated.last()) < (p1.distance(updated.last()) - DISTANCE_THRESHOLD)) {
                updated += p2
                p2 = averageNearestPoint(iter2.next(), list1, weight2, weight1)
            }
            updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            iter2.forEachRemaining { point ->
                updated += Point2D.Double(
                    (point.x * weight2 + list1.last().x * weight1) / (weight1 + weight2),
                    (point.y * weight2 + list1.last().y * weight1) / (weight1 + weight2)
                )
            }
        }
        else -> {
            // I'm not sure if this is guaranteed for every case yet, but in the
            // cases I've seen when the second-last point for both lists are
            // averaged with each other there will be the last points on each path
            // that won't otherwise get accounted for.
            // I believe that's the only case where this can happen, as in other
            // cases one of the two lists will still have points left

            // Determine if the points should be averaged or added one at a time
            when {
                p1.distance(updated.last()) < p2.distance(updated.last()) - DISTANCE_THRESHOLD -> {
                    updated += p1
                    updated += p2
                }
                p2.distance(updated.last()) < p1.distance(updated.last()) - DISTANCE_THRESHOLD -> {
                    updated += p2
                    updated += p1
                }
                else ->
                    updated += Point2D.Double((p1.x + p2.x) / 2, (p1.y + p2.y) / 2)
            }
        }
    }
    // In my process for adding points I'll always actually add the first point
    // twice. Once in the initial add and once from whichever line's endpoint
    // extends past the other line. I don't want to rewrite the loop until I've
    // determined if it actually works as expected in the current state. So I'm
    // just removing the first redundant point here after the fact
    updated.removeAt(0)
    return updated
}

/**
 * Finds the average between a given point and the closest spot along a path
 * given by a list of points
 */
fun averageNearestPoint(point: Point2D, path: List<Point2D>)
        : Point2D {
    return averageNearestPoint(point, path, 1, 1)
}
fun averageNearestPoint(point: Point2D, path: List<Point2D>, pointWeight: Int, lineWeight: Int)
        : Point2D {
    // Find the nearest point on each line segment, then take the average with the closest one
    var nearestPoint = nearestPointOnLine(point, Pair(path[0], path[1]))
    var nearestDist = nearestPoint.distance(point)
    for (i in 2 until path.size) {
        val candidate = nearestPointOnLine(point, Pair(path[i - 1], path[i]))
        val candidateDist = point.distance(candidate)
        if (candidateDist < nearestDist) {
            nearestDist = candidateDist
            nearestPoint = candidate
        }
    }
    // Average with the nearest point
    return Point2D.Double(
        (point.x * pointWeight + nearestPoint.x * lineWeight) / (pointWeight + lineWeight),
        (point.y * pointWeight + nearestPoint.y * lineWeight) / (pointWeight + lineWeight)
    )
}

/**
 * Finds the closest point to point P on line segment
 */
fun nearestPointOnLine(p: Point2D, line: Pair<Point2D, Point2D>)
        : Point2D {
    // Find vectors for A->P and A->B
    val aToP = Point2D.Double(p.x - line.first.x, p.y - line.first.y)
    val aToB = Point2D.Double(line.second.x - line.first.x, line.second.y - line.first.y)

    val atb2 = aToB.x * aToB.x + aToB.y * aToB.y
    val dotProd = aToP.x * aToB.x + aToP.y * aToB.y
    val dist = dotProd / atb2

    // Find the point
    val nearest = Point2D.Double(line.first.x + aToB.x * dist, line.first.y + aToB.y * dist)

    // Make sure the point actually lies on the line
    // There are four cases:
    //    1. The point is left of the line
    //    2. The point is right of the line
    //    3. The point is below the line
    //    4. The point is above the line
    // Normally checking only left/right would work fine, but if the line is
    // vertical those won't work, so checking above/below is also needed as
    // a special case
    // Only check one value at a time, as the actual slope of the line
    // can't be guaranteed, and we can't guarantee the order of A/B
    return if (nearest.x < line.first.x && nearest.x < line.second.x)
        if (line.first.x < line.second.x)
            Point2D.Double(line.first.x,  line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.y < line.first.y && nearest.y < line.second.y)
        if (line.first.y < line.second.y)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.x > line.first.x && nearest.x > line.second.x)
        if (line.first.x > line.second.x)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else if (nearest.y > line.first.y && nearest.y > line.second.y)
        if (line.first.y > line.second.y)
            Point2D.Double(line.first.x, line.first.y)
        else
            Point2D.Double(line.second.x, line.second.y)
    else
        nearest
}
