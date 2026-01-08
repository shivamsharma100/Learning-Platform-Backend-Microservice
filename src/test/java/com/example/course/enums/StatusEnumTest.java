package com.example.course.enums;

import com.example.course.exception.IncorrectStatusForEnrollment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusEnumTest {

    @Test
    void testGetMessageType_Available() {
        assertEquals("available", StatusEnum.AVAILABLE.getMessageType());
    }

    @Test
    void testGetMessageType_NotAvailable() {
        assertEquals("notAvailable", StatusEnum.NOT_AVAILABLE.getMessageType());
    }

    @Test
    void testFindByName_Available_LowerCase() {
        StatusEnum status = StatusEnum.findByName("available");
        assertEquals(StatusEnum.AVAILABLE, status);
    }

    @Test
    void testFindByName_Available_UpperCase() {
        StatusEnum status = StatusEnum.findByName("AVAILABLE");
        assertEquals(StatusEnum.AVAILABLE, status);
    }

    @Test
    void testFindByName_NotAvailable_MixedCase() {
        StatusEnum status = StatusEnum.findByName("notAvailable");
        assertEquals(StatusEnum.NOT_AVAILABLE, status);
    }

    @Test
    void testFindByName_InvalidValue_ThrowsException() {
        IncorrectStatusForEnrollment exception =
                assertThrows(IncorrectStatusForEnrollment.class,
                        () -> StatusEnum.findByName("invalidStatus"));

        assertTrue(exception.getMessage()
                .contains("Unrecognized status value : invalidStatus"));
    }
}
