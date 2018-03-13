package ro.uaic.info;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.TestDTO;
import ro.uaic.info.crypto.CryptoUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TestDTO t = new TestDTO();
        List<String> strings = new ArrayList<>();
        strings.add("Hell0");
        strings.add("World");
        t.setStrings(strings);
        System.out.println(objectMapper.writeValueAsString(t));
    }
}
