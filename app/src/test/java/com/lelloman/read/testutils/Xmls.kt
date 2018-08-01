package com.lelloman.read.testutils

import com.lelloman.read.feed.ParsedFeed
import io.reactivex.Single
import java.io.File

object Xmls {
    private const val FANPAGE = "rss_fanpage.xml"
    private const val REPUBBLICA = "rss_repubblica.xml"
    private const val REPUBBLICA_ACTUAL = "rss_repubblica_actual.xml"
    const val SAMPLE = "rss_sample2.0.xml"
    const val VICE = "rss_vice.xml"
    private const val WIRED = "rss_wired.xml"

    val ALL_RSS = listOf(FANPAGE, REPUBBLICA, REPUBBLICA_ACTUAL, SAMPLE, VICE, WIRED)

    const val MALFORMED_XML1 = "malformed_xml1.xml"
    const val INVALID_ROOT_TAG_XML = "invalid_root_tag.xml"

    val SAMPLE_FEEDS = listOf(
        ParsedFeed(
            title = "Star City",
            subtitle = "How do Americans get ready to work with Russians aboard the International\n" +
                "                Space Station? They take a crash course in culture, language and protocol at\n" +
                "                Russia's <a href=\"http://howe.iki.rssi.ru/GCTC/gctc_e.htm\">Star City</a>.\n            ",
            link = "http://liftoff.msfc.nasa.gov/news/2003/news-starcity.asp",
            timestamp = 1054633161000
        ),
        ParsedFeed(
            title = "The Engine That Does More",
            subtitle = "Before man travels to Mars, NASA hopes to design new engines that will let\n" +
                "                us fly through the Solar System more quickly. The proposed VASIMR engine would do\n" +
                "                that.\n" +
                "            ",
            link = "http://liftoff.msfc.nasa.gov/news/2003/news-VASIMR.asp",
            timestamp = 1054024652000
        ),
        ParsedFeed(
            title = "Astronauts' Dirty Laundry",
            subtitle = "Compared to earlier spacecraft, the International Space Station has many\n" +
                "                luxuries, but laundry facilities are not one of them. Instead, astronauts have other\n" +
                "                options.\n" +
                "            ",
            link = "http://liftoff.msfc.nasa.gov/news/2003/news-laundry.asp",
            timestamp = 1053420962000
        )
    )

    val VICE_FEED_0 = ParsedFeed(
        title = "La verità sul video 'La verità su Aquarius e Saviano'",
        subtitle = "\n" +
            "                Abbiamo analizzato punto per punto il video 'La verità su Aquarius e Saviano' di Luca Donadel.",
        link = "\n" +
            "                https://www.vice.com/it/article/evk74m/la-verita-sul-video-la-verita-su-aquarius-e-saviano-donadel\n" +
            "            ",
        timestamp = 1529149073000
    )

    val VICE_FEED_1 = ParsedFeed(
        title = "\n" +
            "                Come Salvini è diventato il premier fantasma di questo governo",
        subtitle = "\n" +
            "                In questi giorni Salvini si è occupato di tutto. Ha fatto il ministro dell'Interno, delle Infrastrutture degli Esteri, e dell'Economia.",
        link = "https://www.vice.com/it/article/8xebbv/salvini-premier-governo",
        timestamp = 1529065934000
    )

    fun readFile(fileName: String): Single<String> = Single.fromCallable {
        val uri = javaClass.classLoader.getResource(fileName)
        File(uri.file).readText()
    }
}