# mfthub

Transfer definition:
```java
            MftCoreAPI mftCoreAPI = ctx.getBean(MftCoreAPI.class);
            Transfer transfer = new Transfer.Builder("testtransfer")
                  .withCronSchedule("0/10 * * * * ?")
                  .fromNamedSource("MY_SENDER", "local:///tmp/source")
                  .toTargets("local:///tmp/foo")
                  .files("bar/**/*.pdf")
                  .addProcessor(ProcessingType.COMPRESS, "destination", "foo.tar.gz")
                  .usingReceivePolicies(
                        TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();
            mftCoreAPI.saveAndScheduleTransfer(transfer);
```

Output for delivery:
```
{
  "uuid" : "ac1922d0-fdc3-49e2-a9e4-2f24a94a0f75",
  "transfer" : {
    "uuid" : "b0b57345-f374-407f-89ce-8eeff3da5f07",
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
      "endpointKey" : "__ep__425p0v",
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
    "processings" : [ {
      "id" : 1,
      "type" : "COMPRESS",
      "bindings" : [ {
        "id" : 1,
        "param" : "destination",
        "value" : "foo.tar.gz"
      } ]
    } ],
    "fileSelector" : {
      "id" : 1,
      "fileSelectorStrategy" : "ANT_STYLE",
      "filenameExpression" : "bar/**/*.pdf",
      "onlyNewest" : false
    }
  },
  "initiated" : 1425316760011,
  "state" : "FILES_SEND",
  "stateChanges" : [ {
    "id" : 43,
    "changedTo" : "INITIATED",
    "timeOfChange" : 1425316760012,
    "action" : "Created"
  }, {
    "id" : 44,
    "changedTo" : "FILES_INBOUND",
    "timeOfChange" : 1425316760078,
    "action" : "Copied 5 files (5304118 bytes) to folder: '/tmp/MY_SENDER/inbound/b0b57345-f374-407f-89ce-8eeff3da5f07/ac1922d0-fdc3-49e2-a9e4-2f24a94a0f75'"
  }, {
    "id" : 45,
    "changedTo" : "PROCESSING_READY",
    "timeOfChange" : 1425316760115,
    "action" : "Copied 5 files (5304118 bytes) from '/tmp/MY_SENDER/inbound/b0b57345-f374-407f-89ce-8eeff3da5f07/ac1922d0-fdc3-49e2-a9e4-2f24a94a0f75' to '/tmp/MY_SENDER/processing_out/b0b57345-f374-407f-89ce-8eeff3da5f07/ac1922d0-fdc3-49e2-a9e4-2f24a94a0f75'"
  }, {
    "id" : 46,
    "changedTo" : "PROCESSING_DONE",
    "timeOfChange" : 1425316760334,
    "action" : "Number of processing steps completed: 1"
  }, {
    "id" : 47,
    "changedTo" : "OUTBOUND_READY",
    "timeOfChange" : 1425316760336,
    "action" : "outbound folder successfully created."
  }, {
    "id" : 48,
    "changedTo" : "FILES_SEND",
    "timeOfChange" : 1425316760390,
    "action" : "Copied 1 files (3774931 bytes) to target: '__ep__425p0v'"
  } ]
}
```
