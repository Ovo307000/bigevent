package com.ovo307000.bigevent.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilTest
{

    private FileUtil fileUtil;

    @BeforeEach
    public void setUp()
    {
        this.fileUtil = new FileUtil();
    }

    @Test
    public void generateUUIDFileName_WithValidInput_ShouldReturnUUIDFileName()
    {
        String originalFilename = "test.txt";
        String uuidFileName     = this.fileUtil.generateUUIDFileName(originalFilename);

        assertNotNull(uuidFileName);
        assertTrue(uuidFileName.contains(".txt"));
    }

    @Test
    public void generateUUIDFileName_WithNullInput_ShouldThrowException()
    {
        assertThrows(NullPointerException.class, () -> FileUtilTest.this.fileUtil.generateUUIDFileName(null));
    }

    @Test
    public void getFileSuffix_WithValidInput_ShouldReturnFileSuffix()
    {
        String originalFilename = "test.txt";
        String suffix           = this.fileUtil.getFileSuffix(originalFilename);

        assertNotNull(suffix);
        assertEquals("txt", suffix);
    }

    @Test
    public void getFileSuffix_WithNoDot_ShouldReturnNull()
    {
        String originalFilename = "test";
        String suffix           = this.fileUtil.getFileSuffix(originalFilename);

        assertNull(suffix);
    }

    @Test
    public void getFileSuffix_WithNullInput_ShouldThrowException()
    {
        assertThrows(NullPointerException.class, () -> FileUtilTest.this.fileUtil.getFileSuffix(null));
    }
}
