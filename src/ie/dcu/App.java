package ie.dcu;

import ie.dcu.process.MCPolygons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.stream.Collectors.toList;

public class App {

    public static final String DATA_FOLDER = "CTbunny";

    public static void main(String... args) throws IOException {
        File[] images = Files.list(Paths.get(DATA_FOLDER)).map(Path::toFile).collect(toList()).toArray(new File[0]);

        MCPolygons marchingCube = new MCPolygons();
        marchingCube.initiateMCProcess(images, ".", DATA_FOLDER, true);
    }
}
