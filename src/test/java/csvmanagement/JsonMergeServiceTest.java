package csvmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import csvmanagement.models.SelectedCsv;
import org.junit.Test;

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
        boolean result = jsonMergeService.hasKey(selectedCsv.getKeyField());

        // then
        assertTrue(result);
    }

    @Test
    public void addElement_AddsGivenElementToJsonNode() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        // when
        jsonMergeService.addElement(selectedCsv);

        // then
        assertTrue(jsonMergeService.hasKey(selectedCsv.getKeyField()));
    }

    @Test
    public void getPrettyPrintJsonString_ReturnsPrettyJson() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        jsonMergeService.addElement(selectedCsv);

        // when
        String result = jsonMergeService.getPrettyPrintJsonString();

        // then
        assertEquals("{\r\n  \"key\" : \"value\"\r\n}", result);
    }

    @Test
    public void removeElement_RemovesElementIfJsonNodeContainsIt() {
        // given
        JsonMergeService jsonMergeService = JsonMergeService.getInstance();
        SelectedCsv selectedCsv = new SelectedCsv("key", "value");
        jsonMergeService.addElement(selectedCsv);

        // when
        jsonMergeService.removeElement(selectedCsv);

        // then
        assertFalse(jsonMergeService.hasKey(selectedCsv.getKeyField()));
    }


}