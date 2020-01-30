package nl.siegmann.zoo.frontend.controller;

import nl.siegmann.zoo.dataloader.ZooDataLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Creates demo data.
 * <p>
 * Loads the demo data from the demo-data.yaml
 */
@RestController
public class DemoDataController {

    private final ZooDataLoader zooDataLoader;

    public DemoDataController(ZooDataLoader zooDataLoader) {
        this.zooDataLoader = zooDataLoader;
    }

    @PostMapping("/create-demo-data")
    public void loadTestData() {
        Reader dataReader = new InputStreamReader(DemoDataController
                .class
                .getClassLoader()
                .getResourceAsStream("demo-data.yml"));
        zooDataLoader.loadTestData(dataReader);
    }
}
