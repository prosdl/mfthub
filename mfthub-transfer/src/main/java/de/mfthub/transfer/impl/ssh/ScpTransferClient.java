package de.mfthub.transfer.impl.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfScp;
import de.mfthub.model.entities.enums.ErrorCode;
import de.mfthub.model.entities.enums.TransferClientFeature;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.storage.folder.MftFolder;
import de.mfthub.transfer.api.TransferClientSupport;
import de.mfthub.transfer.api.TransferReceiptInfo;
import de.mfthub.transfer.api.TransferSendInfo;
import de.mfthub.transfer.exception.TransmissionException;

public class ScpTransferClient extends TransferClientSupport<EndpointConfScp> {
   private static Logger LOG = LoggerFactory.getLogger(ScpTransferClient.class);

   public ScpTransferClient(EndpointConfScp conf) {
      super(conf);
      initalizeFeatures(TransferClientFeature.TF_SUPPORTS_RECEIVE_FILES,
            TransferClientFeature.TF_SUPPORTS_SEND_FILES);
   }

   @Override
   public TransferReceiptInfo receive(Endpoint source, Delivery delivery,
         Set<TransferReceivePolicies> set) throws TransmissionException {
      MftFolder inbound = getInbound(delivery);
      TransferReceiptInfo info = new TransferReceiptInfo();

      Session session = null;
      ChannelExec channel = null;
      OutputStream out = null;
      InputStream in = null;

      JSch jsch = new JSch();
      try {
         session = jsch.getSession(getConfiguration().getUserid(),
               getConfiguration().getDnsName(), getConfiguration().getPort());
         session.setUserInfo(new ScpSimplePasswordUserInfo(getConfiguration()
               .getPassword()));
         session.setConfig("StrictHostKeyChecking", "no");
         session.connect();

         // TODO filename, modes, no ant ...
         String cmd = "scp -r -f "
               + getConfiguration().getDirectory()
               + "/"
               + delivery.getTransfer().getFileSelector()
                     .getFilenameExpression();
         channel = (ChannelExec) session.openChannel("exec");
         channel.setCommand(cmd);

         out = channel.getOutputStream();
         in = channel.getInputStream();

         channel.connect();
         ScpTools.writeAck(out);

         ScpFromTransfer scpFrom = new ScpFromTransfer(in, out,
               inbound.getPath());
         scpFrom.transfer();

         info.setNumberOfFilesReceived(scpFrom.getNumberOfFilesReceived());
         info.setTotalBytesReceived(scpFrom.getNumberOfBytesReceived());
         info.setInboundFolder(inbound.getPath().toString());
      } catch (JSchException e) {
         throw new TransmissionException(
               ErrorCode.TRANSMISSION_COULDNT_RECEIVE,
               "SSH library error while sending file with scp: "
                     + e.getMessage(), e);
      } catch (IOException e) {
         throw new TransmissionException(
               ErrorCode.TRANSMISSION_COULDNT_RECEIVE,
               "IO-error while sending file with scp: " + e.getMessage(), e);
      } finally {
         if (out != null) {
            try {
               out.close();
            } catch (IOException e) {
               LOG.error("Error while closing out", e);
            }
         }
         if (channel != null) {
            channel.disconnect();
         }
         if (session != null) {
            session.disconnect();
         }
      }

      return info;
   }

   @Override
   public TransferSendInfo send(Endpoint target, Delivery delivery,
         Set<TransferSendPolicies> transferPolicies)
         throws TransmissionException {

      MftFolder outbound = getOutbound(delivery);
      TransferSendInfo info = new TransferSendInfo();

      Session session = null;
      ChannelExec channel = null;
      OutputStream out = null;
      InputStream in = null;

      try {
         JSch jsch = new JSch();
         session = jsch.getSession(getConfiguration().getUserid(),
               getConfiguration().getDnsName(), getConfiguration().getPort());
         session.setUserInfo(new ScpSimplePasswordUserInfo(getConfiguration()
               .getPassword()));
         session.setConfig("StrictHostKeyChecking", "no");
         session.connect();

         String cmd = "scp -r -d -t " + getConfiguration().getDirectory();
         channel = (ChannelExec) session.openChannel("exec");
         channel.setCommand(cmd);

         out = channel.getOutputStream();
         in = channel.getInputStream();

         channel.connect();
         ScpTools.readAck(in);

         ScpToFilesVisitor visitor = new ScpToFilesVisitor(in, out);
         Files.walkFileTree(outbound.getPath(), visitor);

         info.setNumberOfFilesSend(visitor.getNumberOfFilesSend());
         info.setTotalBytesSend(visitor.getNumberOfBytesSend());
         info.setOutboundFolder(outbound.getPath().toString());

      } catch (JSchException e) {
         throw new TransmissionException(ErrorCode.TRANSMISSION_COULDNT_SEND,
               "SSH library error while sending file with scp: "
                     + e.getMessage(), e);
      } catch (IOException e) {
         throw new TransmissionException(ErrorCode.TRANSMISSION_COULDNT_SEND,
               "IO-error while sending file with scp: " + e.getMessage(), e);
      } finally {
         if (out != null) {
            try {
               out.close();
            } catch (IOException e) {
               LOG.error("Error while closing out", e);
            }
         }
         if (channel != null) {
            channel.disconnect();
         }
         if (session != null) {
            session.disconnect();
         }
      }

      return info;
   }

}
