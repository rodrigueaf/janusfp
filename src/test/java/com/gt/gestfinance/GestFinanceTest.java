package com.gt.gestfinance;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Classe parent de toutes les classes filles pour les tests Elle configure le
 * test sur le context d'application et l'environnement d'exécution
 *
 * @author <a href="mailto:claude-rodrigue.affodogandji@ace3i.com?">RODRIGUE
 * AFFODOGANDJI</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceApplication.class})
@DirtiesContext
public abstract class GestFinanceTest {

}
