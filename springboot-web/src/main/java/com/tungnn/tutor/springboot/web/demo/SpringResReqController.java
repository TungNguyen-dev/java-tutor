package com.tungnn.tutor.springboot.web.demo;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringResReqController {

  @RequestMapping("/req-params")
  public ResponseEntity<String> requestParameters(@RequestParam String name) {
    return ResponseEntity.ok().body(name);
  }

  @RequestMapping("/req-params/{id}")
  public ResponseEntity<String> requestParametersWithID(
      @PathVariable("id") Long id, @RequestParam String name) {

    return ResponseEntity.ok().body(Map.of("id", id, "name", name).toString());
  }

  @RequestMapping("/req-headers")
  public ResponseEntity<String> requestHeaders(@RequestHeader("User-Agent") String agent) {
    return ResponseEntity.ok().body(agent);
  }
}
