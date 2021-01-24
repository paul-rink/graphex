package graphex2021.model;

import org.junit.*;
//import org.junit.After;
//import org.junit.Before;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//import graphex2021.model.GraphParser;

public class GraphParserTest {

    private static final File originalFile = new File("GraphData/testGraph.json");
    private static final File TEST_DIR = new File("target/test");
    private static GraphParser parser;

    /**
     * make sure output folder is empty
     */
    @BeforeClass
    public static void beforeClass() {

        /*
        if (TEST_DIR.exists()) {
            for (File f : TEST_DIR.listFiles()) {
                f.delete();
            }
        } else {
            TEST_DIR.mkdirs();
        }
         */
    }
    @Before
    public void setUp() throws Exception {
        //this.parser = GraphParser.getGraphParserInstance();
        //File original = new File(originalFilePath);
        //Path copied = Paths.get("src/test/resources/graphCopy.json");
        //Path originalPath = original.toPath();
        //Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testTest() {
        int i = 1+2;
        assertEquals(i,3);
    }

    @After
    public void tearDown() throws Exception {
    }

}