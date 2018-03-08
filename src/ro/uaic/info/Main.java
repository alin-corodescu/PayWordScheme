//package ro.uaic.info;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ro.uaic.info.crypto.ClientCertificateRepresentation;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//
//public class Main {
//
//    public static void main(String[] args) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ClientCertificateRepresentation certificateRepresentation = new ClientCertificateRepresentation();
//        try {
//            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//            generator.initialize(2048);
//            KeyPair pair = generator.genKeyPair();
//            certificateRepresentation.setKb(pair.getPublic());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(objectMapper.writeValueAsString(certificateRepresentation));
//
//    }
//}
