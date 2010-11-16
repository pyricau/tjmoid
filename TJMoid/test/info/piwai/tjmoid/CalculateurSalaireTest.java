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
				.tauxMargeCommerciale(0.1) //
				.nbJoursTravaillesAnnuels(219);

		CalculateurSalaire calculateurSalaire = builder.build();

		assertEquals(37424, calculateurSalaire.calculerSalaireBrut(500));
		assertEquals(31008, calculateurSalaire.calculerSalaireBrut(350));
		assertEquals(31008, calculateurSalaire.calculerSalaireBrut(400));
		assertEquals(59878, calculateurSalaire.calculerSalaireBrut(800));

	}

}
