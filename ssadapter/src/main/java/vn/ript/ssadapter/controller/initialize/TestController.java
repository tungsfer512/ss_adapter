package vn.ript.ssadapter.controller.initialize;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

    @GetMapping("")
    public ResponseEntity<InputStreamResource> getCSRS() {
        try {
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
