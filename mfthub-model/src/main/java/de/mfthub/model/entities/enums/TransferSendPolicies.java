package de.mfthub.model.entities.enums;

public enum TransferSendPolicies {
   LOCKSTRATEGY_PG_LEGACY,
   LOCKSTRATEGY_FILELOCK,
   LOCKSTRATEGY_ATOMIC_MOVE,
   OVERWRITE_EXISTING,
   NO_REDELIVERY
}
