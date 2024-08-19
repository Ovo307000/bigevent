package com.ovo307000.bigevent.core.utils;

import org.hibernate.annotations.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ThreadLocalUtilTest
{
    @InjectMocks
    private ThreadLocalUtil<String> threadLocalUtil;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this); // Use openMocks instead of initMocks
    }

    @Test
    void getAndRemove_WhenSet_ExpectReturnsValueAndRemovesIt()
    {
        // Arrange
        String value = "testValue";
        this.threadLocalUtil.set(value);

        // Act
        String result = this.threadLocalUtil.getAndRemove();

        // Assert
        assertEquals(value, result, "Expected the getAndRemove method to return the set value.");
        assertNull(this.threadLocalUtil.get(), "Expected the thread local value to be removed after getAndRemove.");
    }

    @Test
    void getAndRemove_WhenNotSet_ExpectReturnsNullAndDoesNothing()
    {
        // Act
        String result = this.threadLocalUtil.getAndRemove();

        // Assert
        assertNull(result, "Expected the getAndRemove method to return null when no value is set.");
        assertNull(this.threadLocalUtil.get(),
                   "Expected the thread local value to remain null after getAndRemove when no value is set.");
    }
}
