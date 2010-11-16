package info.piwai.tjmoid;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculateurSalaireTest {

	@Test
	public void testCalculerSalaireBrut() {

		CalculateurSalaire.Builder builder = new CalculateurSalaire.Builder();

		builder.salaireBrutDeBase(2584) //
				.tauxChargesSocialesPatronales(1.58) //
				.tauxPartageIngeEntreprise(0.6) //
				.nbJoursTravaillesAnnuels(219);
		
		

		CalculateurSalaire calculateurSalaire10Pourcent = builder.tauxMargeCommerciale(0.1).build();

		assertEquals(31008, calculateurSalaire10Pourcent.calculerSalaireBrut(350));
		assertEquals(31008, calculateurSalaire10Pourcent.calculerSalaireBrut(400));
		assertEquals(37424, calculateurSalaire10Pourcent.calculerSalaireBrut(500));
		assertEquals(59878, calculateurSalaire10Pourcent.calculerSalaireBrut(800));
		
		CalculateurSalaire calculateurSalaire05Pourcent = builder.tauxMargeCommerciale(0.05).build();
		
		assertEquals(31008, calculateurSalaire05Pourcent.calculerSalaireBrut(350));
		assertEquals(31603, calculateurSalaire05Pourcent.calculerSalaireBrut(400));
		assertEquals(39503, calculateurSalaire05Pourcent.calculerSalaireBrut(500));
		assertEquals(63205, calculateurSalaire05Pourcent.calculerSalaireBrut(800));

	}

}
