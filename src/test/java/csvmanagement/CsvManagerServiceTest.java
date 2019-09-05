package csvmanagement;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class CsvManagerServiceTest {

    CsvManagerService managerService = CsvManagerService.getInstance();

    @Before
    public void setup() {
        CsvManagerService.getInstance().clearRecords();
        CsvManagerService.getInstance().clearHistory();
    }

    @Test
    public void setRecords_setsRecords1() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"});
        // when
        managerService.setRecords(data);

        // then
        assertEquals(managerService.getObservableRecords(), data);
        assertEquals(managerService.getRecordsHistory(), ImmutableList.of(data));
    }

    @Test
    public void setRecords_setsRecords2() {
        // given
        List<String[]> data = ImmutableList.of(
                new String[]{"1", "2", "3"},
                new String[]{"4", "5", "6"});
        // when
        managerService.setRecords(data);

        // then
        assertEquals(managerService.getObservableRecords(), data);
        assertEquals(managerService.getRecordsHistory(), ImmutableList.of(data));
    }

    @Test
    public void setRecords_setsRecords3() {
        // given
        List<String[]> data = ImmutableList.of(
                new String[]{"1", "2", "3"},
                new String[]{"4", "5", "6"},
                new String[]{"1", "2", "3"});
        // when
        managerService.setRecords(data);

        // then
        assertEquals(managerService.getObservableRecords(), data);
        assertEquals(managerService.getRecordsHistory(), ImmutableList.of(data));
    }

    @Test
    public void clearRecords_setsEmptyArrayListAsRecords_WhenDataPresent() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"});
        // when
        managerService.setRecords(data);
        managerService.clearRecords();

        // then
        assertEquals(new ArrayList<>(), managerService.getObservableRecords());
        assertEquals(ImmutableList.of(data, new ArrayList<>()), managerService.getRecordsHistory());
    }

    @Test
    public void clearRecords_setsEmptyArrayListAsRecords_WhenNoDataPresent() {
        // given
        // when
        managerService.clearRecords();

        // then
        assertEquals(managerService.getObservableRecords(), new ArrayList<>());
    }

    @Test
    public void canUndo_ReturnsTrue_AfterAddingData() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"});
        // when
        managerService.setRecords(data);

        // then
        assertTrue(managerService.canUndo());
    }

    @Test
    public void canUndo_ReturnsFalse_IfNoActionsDone() {
        // given
        // when

        // then
        assertFalse(managerService.canUndo());
    }

    @Test
    public void undoRecords_SetsEmptyListAsRecordings_AfterDataAdded() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"});

        // when
        managerService.setRecords(data);
        // then
        assertEquals(data, managerService.getObservableRecords());
        assertTrue(managerService.canUndo());
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());

        // when
        managerService.undoRecords();
        // then
        assertFalse(managerService.canUndo());
        assertEquals(new ArrayList<>(), managerService.getObservableRecords());
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());
    }

    @Test
    public void undoRecords_SetsPreviousListAsRecordings_AfterDataAddedTwoTimes() {
        // given
        List<String[]> data1 = ImmutableList.of(
                new String[]{"1", "2", "3"},
                new String[]{"4", "5", "6"},
                new String[]{"1", "2", "3"});
        List<String[]> data2 = ImmutableList.of(
                new String[]{"12", "22", "32"},
                new String[]{"42", "52", "62"},
                new String[]{"12", "22", "32"});

        // when
        managerService.setRecords(data1);
        // then
        assertEquals(data1, managerService.getObservableRecords());
        assertTrue(managerService.canUndo());

        // when
        managerService.setRecords(data2);
        // then
        assertEquals(data2, managerService.getObservableRecords());
        assertTrue(managerService.canUndo());

        // when
        managerService.undoRecords();
        // then
        assertTrue(managerService.canUndo());
        assertEquals(data1.size(), managerService.getObservableRecords().size());
        for (int i = 0; i < data1.size(); i++) {
            Arrays.equals(managerService.getObservableRecords().get(i), data1.get(i));
        }
        assertTrue(managerService.canRedo());
    }

    @Test
    public void redoRecords_SetsPreviousListAsRecordings_AfterUndo() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"});

        // when
        managerService.setRecords(data);
        // then
        assertEquals(data, managerService.getObservableRecords());
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());
        assertTrue(managerService.canUndo());

        // when
        managerService.undoRecords();
        // then
        assertFalse(managerService.canUndo());
        assertEquals(new ArrayList<>(), managerService.getObservableRecords());
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());
        assertTrue(managerService.canRedo());

        // when
        managerService.redoRecords();
        // then
        assertTrue(managerService.canUndo());
        assertEquals(data.size(), managerService.getObservableRecords().size());
        for (int i = 0; i < data.size(); i++) {
            Arrays.equals(managerService.getObservableRecords().get(i), data.get(i));
        }
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());
    }

    @Test
    public void redoRecords_SetsPreviousListAsRecordings_AfterRemove() {
        // given
        List<String[]> data = ImmutableList.of(new String[]{"1", "2", "3"}, new String[]{"4", "5", "6"});

        // when
        managerService.setRecords(data);
        // then
        assertEquals(data, managerService.getObservableRecords());
        assertTrue(managerService.canUndo());
        assertEquals(ImmutableList.of(data), managerService.getRecordsHistory());

        // when
        managerService.removeRecords(singletonList(1));
        // then
        assertTrue(managerService.canUndo());
        List<String[]> expectedData = ImmutableList.of(new String[]{"1", "2", "3"});
        assertEquals(expectedData.size(), managerService.getObservableRecords().size());
        for (int i = 0; i < expectedData.size(); i++) {
            Arrays.equals(managerService.getObservableRecords().get(i), expectedData.get(i));
        }

        // when
        managerService.undoRecords();
        // then
        assertEquals(data.size(), managerService.getObservableRecords().size());
        for (int i = 0; i < data.size(); i++) {
            Arrays.equals(managerService.getObservableRecords().get(i), data.get(i));
        }
        assertTrue(managerService.canRedo());

        // when
        managerService.redoRecords();
        // then
        assertTrue(managerService.canUndo());
        List<String[]> expectedData1 = ImmutableList.of(new String[]{"1", "2", "3"});
        assertEquals(expectedData1.size(), managerService.getObservableRecords().size());
        for (int i = 0; i < expectedData1.size(); i++) {
            Arrays.equals(managerService.getObservableRecords().get(i), expectedData1.get(i));
        }
        assertFalse(managerService.canRedo());
    }

}