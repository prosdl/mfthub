# mfthub

Transfer definition:
```java
    MftCoreAPI mftCoreAPI = ctx.getBean(MftCoreAPI.class);
            Transfer transfer = new Transfer.Builder("testtransfer")
                  .withCronSchedule("0/10 * * * * ?")
                  .fromNamedSource("MY_SENDER", "local:///tmp/source")
                  .toTargets("local:///tmp/foo")
                  .files("bar/**/*.pdf")
                  .usingReceivePolicies(
                        TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();
            mftCoreAPI.saveAndScheduleTransfer(transfer);
```

Output:
```
19:05:40.587 [main] INFO  d.m.c.CoreMain - Started CoreMain in 6.14 seconds (JVM running for 6.763)
19:05:40.863 [scheduler_Worker-1] INFO  d.m.c.s.KickoffTransferJob - Starting job: 'mftjobs.06a75c96-aa4e-4294-96fd-010aeb05af95'
19:05:40.932 [DefaultMessageListenerContainer-1] INFO  d.m.c.a.l.InboundListenerDefaultImpl - mft.inbound received message: delivery.uuid='d8d30c05-a782-4e27-addf-2de611df73bb'
19:05:41.146 [DefaultMessageListenerContainer-1] INFO  d.m.c.a.l.InboundListenerDefaultImpl - Delivery found: 
 {
  "uuid" : "d8d30c05-a782-4e27-addf-2de611df73bb",
  "transfer" : {
    "uuid" : "06a75c96-aa4e-4294-96fd-010aeb05af95",
    "name" : "testtransfer",
    "trigger" : {
      "cronExpresion" : "0/10 * * * * ?"
    },
    "source" : {
      "id" : 1,
      "endpointKey" : "MY_SENDER",
      "active" : true,
      "protocol" : "LOCAL",
      "endpointConfiguration" : {
        "id" : 1,
        "directory" : "/tmp/source"
      }
    },
    "targets" : [ {
      "id" : 2,
      "endpointKey" : "__ep__k7kpus",
      "active" : true,
      "protocol" : "LOCAL",
      "endpointConfiguration" : {
        "id" : 2,
        "directory" : "/tmp/foo"
      }
    } ],
    "transferSendPolicies" : [ ],
    "transferReceivePolicies" : [ "LOCKSTRATEGY_PG_LEGACY" ],
    "administrativeApplication" : {
      "id" : 1,
      "name" : "internal",
      "description" : "Interne Anwendung"
    },
    "tenant" : {
      "id" : 1,
      "name" : "internal",
      "description" : "Interner Transfer",
      "externalId" : "000"
    },
    "transformations" : [ ],
    "fileSelector" : {
      "id" : 1,
      "fileSelectorStrategy" : "ANT_STYLE",
      "filenameExpression" : "bar/**/*.pdf",
      "onlyNewest" : false
    }
  },
  "initiated" : 1425146740884,
  "state" : "INITIATED"
}
19:05:41.163 [DefaultMessageListenerContainer-1] INFO  d.m.c.m.TransferExecutorImpl - Starting reception phase of delivery d8d30c05-a782-4e27-addf-2de611df73bb. Details:

19:05:41.182 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'not_me.pdf'.
19:05:41.183 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'bar/baz/bla3.pdf'.
19:05:41.183 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Moving '/tmp/source/bar/baz/bla3.pdf' to '/tmp/MY_SENDER/OUTBOUND/06a75c96-aa4e-4294-96fd-010aeb05af95/d8d30c05-a782-4e27-addf-2de611df73bb/bar/baz/bla3.pdf'.
19:05:41.183 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'bar/baz/bla4.pdf'.
19:05:41.183 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Moving '/tmp/source/bar/baz/bla4.pdf' to '/tmp/MY_SENDER/OUTBOUND/06a75c96-aa4e-4294-96fd-010aeb05af95/d8d30c05-a782-4e27-addf-2de611df73bb/bar/baz/bla4.pdf'.
19:05:41.183 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'bar/baz/bla2.doc'.
19:05:41.185 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'bar/bla1.pdf'.
19:05:41.185 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Moving '/tmp/source/bar/bla1.pdf' to '/tmp/MY_SENDER/OUTBOUND/06a75c96-aa4e-4294-96fd-010aeb05af95/d8d30c05-a782-4e27-addf-2de611df73bb/bar/bla1.pdf'.
19:05:41.186 [DefaultMessageListenerContainer-1] INFO  d.m.t.i.LocalFilecopyTransferClient - Found: 'foo'.
```
