import static org.junit.jupiter.api.Assertions.*;

import org.as2.LinkedListImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedListImplTest {

    private LinkedListImpl linkedList;

    @BeforeEach
    void setUp() {
        linkedList = new LinkedListImpl();
    }

    @Test
    void getReturnsCorrectValueAtIndex() {
        linkedList.addAtHead(1);
        linkedList.addAtTail(2);
        linkedList.addAtTail(3);
        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));
        assertEquals(3, linkedList.get(2));
    }

    @Test
    void getReturnsMinusOneForInvalidIndex() {
        linkedList.addAtHead(1);
        assertEquals(-1, linkedList.get(1));
        assertEquals(-1, linkedList.get(-1));
    }

    @Test
    void addAtHeadInsertsValueAtHead() {
        linkedList.addAtHead(1);
        linkedList.addAtHead(2);
        assertEquals(2, linkedList.get(0));
        assertEquals(1, linkedList.get(1));
    }

    @Test
    void addAtTailInsertsValueAtTail() {
        linkedList.addAtHead(1);
        linkedList.addAtTail(2);
        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));
    }

    @Test
    void addAtIndexInsertsValueAtCorrectIndex() {
        linkedList.addAtHead(1);
        linkedList.addAtTail(3);
        linkedList.addAtIndex(1, 2);
        assertEquals(1, linkedList.get(0));
        assertEquals(2, linkedList.get(1));
        assertEquals(3, linkedList.get(2));
    }

    @Test
    void addAtIndexDoesNothingForInvalidIndex() {
        linkedList.addAtHead(1);
        linkedList.addAtIndex(2, 2);
        assertEquals(1, linkedList.get(0));
        assertEquals(-1, linkedList.get(1));
    }

    @Test
    void deleteAtIndexRemovesValueAtCorrectIndex() {
        linkedList.addAtHead(1);
        linkedList.addAtTail(2);
        linkedList.addAtTail(3);
        linkedList.deleteAtIndex(1);
        assertEquals(1, linkedList.get(0));
        assertEquals(3, linkedList.get(1));
    }

    @Test
    void deleteAtIndexDoesNothingForInvalidIndex() {
        linkedList.addAtHead(1);
        linkedList.deleteAtIndex(1);
        assertEquals(1, linkedList.get(0));
    }
}