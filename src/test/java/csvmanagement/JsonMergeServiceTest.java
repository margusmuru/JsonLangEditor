package csvmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import csvmanagement.models.SelectedCsv;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class JsonMergeServiceTest {

    @Test
    public void setJsonNode_AddsJsonAndReturnsTrue_IfValidJson() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        String json = "{\"key\":\"value\"}";
        // when

        boolean result = jsonMergeService.setJsonNode(json);

        // then
        assertTrue(result);
    }

    @Test
    public void setJsonNode_ReturnsFalse_IfInvalidJson() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        String json = "{\"key\":\"value\"";
        // when

        boolean result = jsonMergeService.setJsonNode(json);

        // then
        assertFalse(result);
        assertEquals(new ObjectMapper().createObjectNode(), jsonMergeService.getJsonNode());
    }

    @Test
    public void hasElement_ReturnsTrue_IfJsonNodeContainsGivenElement() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        String json = "{\"key\":\"value\"}";
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        // when

        jsonMergeService.setJsonNode(json);
        boolean result = jsonMergeService.hasElement(selectedCsv);

        // then
        assertTrue(result);
    }

    @Test
    public void add_AddsGivenElementsToJsonNode(){
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        // when
        jsonMergeService.add(singletonList(selectedCsv));

        // then
        assertTrue(jsonMergeService.hasElement(selectedCsv));
    }

    @Test
    public void getPrettyPrintJsonString_ReturnsPrettyJson(){
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        jsonMergeService.add(singletonList(selectedCsv));

        // when
        String result = jsonMergeService.getPrettyPrintJsonString();

        // then
        assertEquals("{\r\n  \"key\" : \"value\"\r\n}", result);
    }

}