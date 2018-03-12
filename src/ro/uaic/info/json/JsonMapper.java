package ro.uaic.info.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.ClientInformationDTO;

import java.io.IOException;


public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static String generateJsonFromDTO(Object object){
        String response = null;
        try {
                response = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
    public static Object generateObjectFromJSON(String jsonString, Object targetObject){
        Object object = null;
        try {
            object = objectMapper.readValue(jsonString, targetObject.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }
}
