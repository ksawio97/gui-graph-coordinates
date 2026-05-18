package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void appCanBeInstantiated() {
        assertNotNull(new App(), "app should be instantiable");
    }
}
