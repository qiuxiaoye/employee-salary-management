package govtech.nphc.employeesalarymanagement.controller;

import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;


@SpringBootTest
public class CSVControllerTest {
    File file1;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /* executed before every test: create temporary files */
    @Before
    public void setUp() {
        try {
            file1 = folder.newFile( "files/g1test.csv" );
        }
        catch( IOException ioe ) {
            System.err.println(
                    "error creating temporary test file in " +
                            this.getClass().getSimpleName() );
        }
    }

    @Autowired
    CSVController csvController;

    @Test
    public void testUploadFile_Success () {

//        MultipartFile multipartFile = new MultipartFile()

    }
}
