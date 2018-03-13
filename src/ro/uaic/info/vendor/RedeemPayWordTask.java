package ro.uaic.info.vendor;

import ro.uaic.info.DTO.ClientCertificateDTO;
import ro.uaic.info.DTO.CommitmentDTO;
import ro.uaic.info.DTO.CurrentPaymentsDTO;
import ro.uaic.info.communication.ClientHandler;
import ro.uaic.info.communication.CommunicationChannel;
import ro.uaic.info.communication.SocketCommunicationChannel;
import ro.uaic.info.crypto.Commitment;
import ro.uaic.info.json.JsonMapper;

import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

class RedeemPayWordTask extends TimerTask {

    private ClientHandler clientHandler;
    private int brokerPort;
    private CommunicationChannel communicationChannel;

    RedeemPayWordTask(ClientHandler clientHandler, int brokerPort) throws IOException {
        if(clientHandler instanceof VendorClientHandler) {
            this.clientHandler = clientHandler;
            this.brokerPort = brokerPort;
        }
        else{
            throw new RuntimeException("Unknown clientHandler for VendorWorkDayProtocol!");
        }
    }

    @Override
    public void run() {
        try {
            this.communicationChannel = new SocketCommunicationChannel(new Socket("localhost", this.brokerPort));
            Commitment commitment = ((VendorClientHandler) clientHandler).getCommitmentMap().get("Client");
            if(commitment.getUsedPaywords() != null) {
                String message = "vendor";
                this.communicationChannel.writeMessage(message);

                CommitmentDTO commitmentDTO = commitment.getRepresentation();
                message = JsonMapper.generateJsonFromDTO(commitmentDTO);
                this.communicationChannel.writeMessage(message);


                CurrentPaymentsDTO currentPaymentsDTO = new CurrentPaymentsDTO(commitment.getLastPaywords(),
                        commitment.getUsedPaywords());

                message = JsonMapper.generateJsonFromDTO(currentPaymentsDTO);
                this.communicationChannel.writeMessage(message);

                message = this.communicationChannel.readMessage();

                if(message.equalsIgnoreCase("True")){
                    System.out.println("Waiting for broker money!");
                    message = communicationChannel.readMessage();
                    System.out.println("Recieved " + message + " cents!");
                }
                else{
                    System.out.println("PayWord Redeem was denied!");
                }
//                System.exit(15);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
