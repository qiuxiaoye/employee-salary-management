package govtech.nphc.employeesalarymanagement.controller;



import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@SpringBootTest
public class CSVControllerTest {

    @Autowired
    CSVController csvController;

    File file1;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /* executed before every test: create temporary files */

    @Before
    public void setUp() {
        try {
            file1 = folder.newFile( "g1test.csv" );
        }
        catch( IOException ioe ) {
            System.err.println(
                    "error creating temporary test file in " +
                            this.getClass().getSimpleName() );
        }
    }

    @Test
    public void testUploadFile_Success () throws IOException {


        InputStream targetStream = new FileInputStream(file1);
        MultipartFile multipartFile = new MockMultipartFile("g1test", targetStream);

        csvController.uploadFile(multipartFile);

//        assertThat(csvController).isNotNull();
    }
}
