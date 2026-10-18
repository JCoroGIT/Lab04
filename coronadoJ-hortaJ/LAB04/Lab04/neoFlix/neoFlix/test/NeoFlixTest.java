package test;
import domain.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NeoFlixTest {

    private NeoFlix neoFlix;

    @Before
    public void setUp() {
        neoFlix = new NeoFlix();
    }

        @Test
    public void shouldHaveInitialContents() {
        assertEquals(6, neoFlix.numberContents());
    }

    @Test
    public void shouldAddEpisode() throws NeoFlixException{
        int before = neoFlix.numberContents();
        neoFlix.addEpisode("The Last Dance", "", "100", "50", "10", "80");
        assertEquals(before + 1, neoFlix.numberContents());
        assertTrue(neoFlix.consult("The Last Dance") instanceof Episode);
    }

    @Test
    public void shouldAddSeriesWithExistingEpisodes() throws NeoFlixException {
        int before = neoFlix.numberContents();
        neoFlix.addSeries("Neo Samurai", "2025", "The Rival\nThe Tournament");
        assertEquals(before + 1, neoFlix.numberContents());
        Content series = neoFlix.consult("Neo Samurai");
        assertTrue(series instanceof Series);
        String data = series.data(false);
        assertTrue(data.contains("The Rival"));
        assertTrue(data.contains("The Tournament"));
    }

    @Test
    public void shouldConsultIgnoringCase() {
        assertNotNull(neoFlix.consult("the rival"));
    }

    @Test
    public void shouldNotFindUnknownContent() {
        assertNull(neoFlix.consult("No Existe"));
    }

    @Test
    public void shouldAllowDuplicatedTitle() throws NeoFlixException{
        int before = neoFlix.numberContents();
        neoFlix.addEpisode("The Hidden Village", "", "199", "120", "50", "430");
        assertEquals(before + 1, neoFlix.numberContents());
    }

    @Test(expected = NumberFormatException.class)
    public void shouldFailWithNonNumericValue()throws NeoFlixException {
        neoFlix.addEpisode("Bad Episode", "", "abc", "1", "1", "1");
    }

    @Test(expected = NumberFormatException.class)
    public void shouldFailWithEmptyNumericValue() throws NeoFlixException{
        neoFlix.addEpisode("Bad Episode", "", "", "", "", "");
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailOnDataWhenSeriesHasUnknownEpisode() throws NeoFlixException {
        int before = neoFlix.numberContents();
        neoFlix.addSeries("Ghost Series", "2025", "No Existe");
        assertEquals(before + 1, neoFlix.numberContents());
        neoFlix.consult("Ghost Series").data(false);
    }

    @Test
    public void shouldListHeaderWithNumberOfContents() {
        assertTrue(neoFlix.toString().startsWith("6 elementos\n"));
    }

    @Test
    public void shouldListInitialEpisodesAndSeries() {
        String list = neoFlix.toString();
        assertTrue(list.contains(">The Hidden Village"));
        assertTrue(list.contains(">The First Mission"));
        assertTrue(list.contains(">The Rival"));
        assertTrue(list.contains(">The Tournament"));
        assertTrue(list.contains(">The Final Battle"));
        assertTrue(list.contains(">The Neo Ninja: 2020"));
    }

    @Test
    public void shouldListSeriesWithItsEpisodes() {
        assertTrue(neoFlix.toString().contains("The Neo Ninja: 2020\n\tThe Hidden Village"));
    }

    @Test
    public void shouldListWithoutIndicators() {
        String list = neoFlix.toString();
        assertFalse(list.contains("("));
        assertFalse(list.contains("Indicadores incompletos"));
    }

    @Test
    public void shouldListEmptySelection() {
        assertEquals("0 elementos\n", neoFlix.data(new ArrayList<Content>(), true));
    }

    @Test
    public void shouldListNewEpisodeAfterAdding() throws NeoFlixException{
        neoFlix.addEpisode("The Last Dance", "", "100", "50", "10", "80");
        String list = neoFlix.toString();
        assertTrue(list.startsWith("7 elementos\n"));
        assertTrue(list.contains(">The Last Dance"));
    }
    
    
    // una ejecución normal y una por excepción
    
    @Test
    public void shouldAddEpisodeNormally() throws NeoFlixException {
        int before = neoFlix.numberContents();
        neoFlix.addEpisode("The Last Dance", "", "100", "50", "10", "80");
        assertEquals(before + 1, neoFlix.numberContents());
    }

    @Test
    public void shouldNotAddExistingTitle() {
        int before = neoFlix.numberContents();
        try {
            neoFlix.addEpisode("The Rival", "", "100", "50", "10", "80");
            fail();
        } catch (NeoFlixException e) {
            assertEquals(NeoFlixException.TITLE_EXISTS, e.getMessage());
    }
        assertEquals(before, neoFlix.numberContents());
}

    @Test
    public void shouldNotAddNonNumericValues() {
        int before = neoFlix.numberContents();
        try {
            neoFlix.addEpisode("Bad Episode", "", "abc", "1", "1", "1");
            fail();
        } catch (NeoFlixException e) {
            assertEquals(NeoFlixException.NOT_NUMERIC, e.getMessage());
        }
        assertEquals(before, neoFlix.numberContents());
}

    @Test
    public void shouldNotAddCompletedGreaterThanAttempts() {
        try {
            neoFlix.addEpisode("Bad Episode", "", "10", "20", "5", "20");
            fail();
        } catch (NeoFlixException e) {
            assertEquals(NeoFlixException.DATA_ERROR, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddSumVotesGreaterThanAllowed() {
        try {
            neoFlix.addEpisode("Bad Episode", "", "10", "5", "5", "51");
            fail();
        } catch (NeoFlixException e) {
            assertEquals(NeoFlixException.DATA_ERROR, e.getMessage());
        }
    }

@Test
public void shouldNotAddSeriesWithUnknownEpisode() {
    int before = neoFlix.numberContents();
    try {
        neoFlix.addSeries("Ghost Series", "2025", "No Existe");
        fail();
    } catch (NeoFlixException e) {
        assertEquals(NeoFlixException.EPISODE_NOT_FOUND, e.getMessage());
    }
    assertEquals(before, neoFlix.numberContents());
}

//capa de dominio

    @Test
    public void shouldConsultExistingContentByTitle() {
        assertEquals("The Rival", neoFlix.consult("The Rival").getTitle());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldFailSelectingByPrefix() {
        neoFlix.select("T");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldFailSearchingByPrefix() {
        neoFlix.search("T");
    }
}