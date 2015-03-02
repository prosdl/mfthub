package de.mfthub.processing.api;

import de.mfthub.model.entities.Delivery;
import de.mfthub.processing.api.exception.ProcessorException;

public interface Processor {
   public void processFiles(Delivery delivery) throws ProcessorException;
}
