package de.mfthub.transfer.api;

import java.util.UUID;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;

/**
 * Describes a path for transfer in- and outboxes. A path is a String representation of
 * endpoints, their in- and outboxes, transfer identifiers and delivery identifiers.
 * 
 * <p/>
 * BNF style path syntax:
 * 
 * <pre>
 * {@code
 * 
 * <path>          ::= <source-id> [ "/" <box>  ["/" <transer-uuid> ["/" <delivery-uuid>  ]]]  
 * <source-id>     ::= <xalpha-num> <xalpha-nums>
 * <xalpha-num>    ::= "A"| ... |"Z"| "a" | ... | "z" | "0" | ... | "9" | "-" | "_" | "."  
 * <xalpha-nums>   ::= } { &lt;xalpha-num&gt; } 
 * {@code
 * <box>           ::= "inbound" | "outbound"
 * <transfer-uuid> ::= <uuid>
 * <delivery-uuid> ::= <uuid>
 * }
 * </pre>
 * 
 * <p/>
 * 
 * <b>Examples</b>:
 * 
 * <pre>
 * u001.xyz.de
 * some_source_endpoint/inbound
 * fileserver_xy123_sftp/outbound/07a0510-3b13-4b17-98e3-b5d91109d8ce
 * localhost_cp/inbound/07a0510-3b13-4b17-98e3-b5d91109d8ce/668c34bc-a431-415c-8448-215104f0ca43
 * </pre>
 * 
 * @author prosdl
 * 
 */
public class MftPath {
   private String sourceIdSegment;
   private MailBox boxSegment;
   private UUID transferUUIDSegment;
   private UUID deliveryUUIDSegment;

   public static enum MailBox {
      INBOUND, OUTBOUND;
      
      public static MailBox fromString(String s) throws MftPathException {
         return MailBox.valueOf(s.toUpperCase());
      }
   }

   public MftPath(String sourceId) throws MftPathException {
      validateSourceIDSyntax(sourceId);
      this.setSourceIdSegment(sourceId);
   }

   public MftPath(String sourceId, MailBox box) throws MftPathException {
      this(sourceId);
      this.boxSegment = box;
   }

   public MftPath(String sourceId, MailBox box, UUID transferUUID)
         throws MftPathException {
      this(sourceId, box);
      this.transferUUIDSegment = transferUUID;
   }

   public MftPath(String sourceId, MailBox box, String transferUUID)
         throws MftPathException {
      this(sourceId, box);
      setTransferUUIDSegment(transferUUID);
   }

   public MftPath(String sourceId, MailBox box, UUID transferUUID, UUID deliveryUUID)
         throws MftPathException {
      this(sourceId, box, transferUUID);
      this.deliveryUUIDSegment = deliveryUUID;
   }

   public MftPath(String sourceId, MailBox box, String transferUUID,
         String deliveryUUID) throws MftPathException {
      this(sourceId, box, transferUUID);
      setDeliveryUUIDSegment(deliveryUUID);
   }

   public static void validateSourceIDSyntax(String sourceIdSegment)
         throws MftPathException {
      if (sourceIdSegment == null || sourceIdSegment.isEmpty()) {
         throw new MftPathException(
               "Illegal syntax for source-id: can't be empty");
      }
      if (!sourceIdSegment.matches("[a-zA-Z0-9_\\-\\.]+")) {
         throw new MftPathException(
               String.format("Illegal syntax for source-id: '%s", 
               sourceIdSegment));
      }
   }
   
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(sourceIdSegment);
      if (boxSegment == null) return sb.toString();
      sb.append("/").append(boxSegment.toString());
      if (transferUUIDSegment == null) return sb.toString();
      sb.append("/").append(transferUUIDSegment.toString());
      if (deliveryUUIDSegment == null) return sb.toString();
      sb.append("/").append(deliveryUUIDSegment.toString());
      return sb.toString();
   }
   
   public String[] toSegmentArray() {
      if (boxSegment == null) return new String[]{sourceIdSegment};
      if (transferUUIDSegment == null) return new String[]{sourceIdSegment, boxSegment.toString()};
      if (deliveryUUIDSegment == null) return new String[]{sourceIdSegment, boxSegment.toString(),transferUUIDSegment.toString()};
      return new String[]{sourceIdSegment,boxSegment.toString(),transferUUIDSegment.toString(),deliveryUUIDSegment.toString()};
   }

   public static MftPath fromString(String path) throws MftPathException {
      if (path == null || path.isEmpty()) {
         throw new MftPathException("Path can't be empty");
      }
      String[] segments = path.split("/");
      MftPath mftPath = new MftPath(segments[0]);
      if (segments.length == 1) {
         return mftPath;
      }
      mftPath.setBoxSegment(segments[1]);
      if (segments.length == 2) {
         return mftPath;
      }
      mftPath.setTransferUUIDSegment(segments[2]);
      if (segments.length == 3) {
         return mftPath;
      }
      mftPath.setTransferUUIDSegment(segments[3]);
      if (segments.length == 4) {
         return mftPath;
      }

      throw new MftPathException(
            String.format("Illegal syntax: too many segments in '%s'.", path));
   }

   public String getSourceIdSegment() {
      return sourceIdSegment;
   }

   public void setSourceIdSegment(String sourceIdSegment)
         throws MftPathException {
      validateSourceIDSyntax(sourceIdSegment);
      this.sourceIdSegment = sourceIdSegment;
   }

   public MailBox getBoxSegment() {
      return boxSegment;
   }

   public void setBoxSegment(MailBox boxSegment) {
      this.boxSegment = boxSegment;
   }
   
   public void setBoxSegment(String boxSegment) throws MftPathException {
      this.boxSegment = MailBox.fromString(boxSegment);
   }

   public UUID getTransferUUIDSegment() {
      return transferUUIDSegment;
   }

   public void setTransferUUIDSegment(UUID jobUUIDSegment) {
      this.transferUUIDSegment = jobUUIDSegment;
   }

   public void setTransferUUIDSegment(String transferUUIDSegment) throws MftPathException {
      try {
         this.transferUUIDSegment = UUID.fromString(transferUUIDSegment);
      } catch (IllegalArgumentException e) {
         throw new MftPathException("Illegal syntax for jobUUID: "
               + e.getMessage(), e);
      }
   }

   public UUID getDeliveryUUIDSegment() {
      return deliveryUUIDSegment;
   }

   public void setDeliveryUUIDSegment(UUID deliveryUUIDSegment) {
      this.deliveryUUIDSegment = deliveryUUIDSegment;
   }

   public void setDeliveryUUIDSegment(String deliveryUUIDSegment)
         throws MftPathException {
      try {
         this.deliveryUUIDSegment = UUID.fromString(deliveryUUIDSegment);
      } catch (IllegalArgumentException e) {
         throw new MftPathException("Illegal syntax for jobUUID: "
               + e.getMessage(), e);
      }
   }

   public static MftPath outboundFrom(Delivery delivery) throws MftPathException {
      return new MftPath(delivery.getTransfer().getSource()
            .getEndpointKey(), MftPath.MailBox.OUTBOUND, delivery.getTransfer()
            .getUuid(), delivery.getUuid());
   }
   public static MftPath inboundFrom(Delivery delivery, Endpoint target) throws MftPathException {
      return new MftPath(target.getEndpointKey(), MftPath.MailBox.INBOUND, 
            delivery.getTransfer().getUuid(), delivery.getUuid());
   }
}
