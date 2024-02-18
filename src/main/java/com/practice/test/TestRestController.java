package com.practice.test;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TestRestController {
    @PostMapping("/test/json")
    public ResponseEntity<String> testJson(@ModelAttribute TestInput testInput) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(createJsonObject(testInput).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createJsonObject(testInput).toString());
        }
    }

    private JSONObject createJsonObject(TestInput testInput) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("draw", testInput.getDraw());
        jsonObject.put("recordsTotal", 1000);
        jsonObject.put("recordsFiltered", 1000);

        JSONArray dataArray = new JSONArray();
        for (int i = 1; i <= 10; i++) {
            JSONObject dataObject = new JSONObject();
            dataObject.put("grpCd", "TEST_GROUP" + i);
            dataObject.put("grpNm", "페이지 그룹" + testInput.getPage());
            dataObject.put("useYn", "N");
            dataObject.put("delYn", "N");
            dataObject.put("crtDt", "2021-03-19 15:36:3" + i);
//            dataObject.put("crtBy", 0);
            dataArray.put(dataObject);
        }

        jsonObject.put("data", dataArray);
        return jsonObject;
    }
}
