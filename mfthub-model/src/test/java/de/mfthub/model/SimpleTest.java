package de.mfthub.model;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-simmft-model-emedded-test.xml" }) 
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SimpleTest {

}
