package eu.kanade.tachiyomi.extension.en.pbfcomics

import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import okhttp3.Request
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import rx.Observable

class PerryBibleFellowship : ParsedHttpSource() {
    override val name = "Perry Bible Fellowship"
    override val baseUrl = "https://pbfcomics.com/"
    override val lang = "en"
    override val supportsLatest = false

    override fun fetchPopularManga(page: Int): Observable<MangasPage> {
        val manga = SManga.create().apply {
            title = name
            artist = "Nicholas Gurewitch"
            author = artist
            status = SManga.ONGOING
            url = ""
            description = "Nicholas Gurewitch draws the The Perry Bible Fellowship. In addition to comics and comic books, he is the co-writer of several un-made TV shows, movie scripts, and numerous D&D sessions with his niece and nephews. You can contact him at nicholas@pbfcomics.com. \n\n" +
                "The Perry Bible Fellowship (or PBF, for short) was founded with Albert Birney and Evan Keogh in 2001. It's been published all over the world, from The Guardian to Playboy. It is the winner of Ignatz, Harvey, and Eisner awards. It owes much to the editorial and creative input of Evan Keogh, and also Jordan E. Morris, who joined the fellowship in 2002. To this day, most PBFs are designed with insights from colleagues, friends, and family. In 2021, to celebrate 20 years of comics, Nicholas invited other artists to help him draw some PBF comics."
            thumbnail_url = "https://pbfcomics.com/wp-content/themes/PBF/images/header_tall.jpg"
        }

        return Observable.just(MangasPage(arrayListOf(manga).reversed(), false))
    }

    override fun fetchSearchManga(page: Int, query: String, filters: FilterList): Observable<MangasPage> = Observable.just(MangasPage(emptyList(), false))

    override fun fetchMangaDetails(manga: SManga): Observable<SManga> {
        return Observable.just(manga)
    }

    override fun chapterListSelector() =
        """#all_thumbnails .thumbnail_gallery_item a"""

    override fun chapterListParse(response: Response): List<SChapter> {
        return super.chapterListParse(response).reversed()
    }

    override fun chapterFromElement(element: Element): SChapter {
        return SChapter.create().apply {
            chapter_number = element.attr("id").toFloat()
            setUrlWithoutDomain(element.attr("href").substring(22))
            name = element.select(".thumbnail_post_title").text()
            // date_upload // Get from URL
        }
    }

    override fun pageListParse(document: Document): List<Page> =
        document.select("#comic img").mapIndexed { i, element -> Page(i, "", element.attr("src")) }

    // <editor-fold desc="Not Used">
    override fun imageUrlParse(document: Document): String = throw UnsupportedOperationException()

    override fun popularMangaSelector(): String = throw UnsupportedOperationException()

    override fun searchMangaFromElement(element: Element): SManga = throw UnsupportedOperationException()

    override fun searchMangaNextPageSelector(): String = throw UnsupportedOperationException()

    override fun searchMangaSelector(): String = throw UnsupportedOperationException()

    override fun popularMangaRequest(page: Int): Request = throw UnsupportedOperationException()

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request = throw UnsupportedOperationException()

    override fun popularMangaNextPageSelector(): String = throw UnsupportedOperationException()

    override fun popularMangaFromElement(element: Element): SManga = throw UnsupportedOperationException()

    override fun mangaDetailsParse(document: Document): SManga = throw UnsupportedOperationException()

    override fun latestUpdatesNextPageSelector(): String = throw UnsupportedOperationException()

    override fun latestUpdatesFromElement(element: Element): SManga = throw UnsupportedOperationException()

    override fun latestUpdatesRequest(page: Int): Request = throw UnsupportedOperationException()

    override fun latestUpdatesSelector(): String = throw UnsupportedOperationException()
    // </editor-fold>
}
