/*
 * Copyright 2010 Pierre-Yves Ricau (py.ricau+tjmoid@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.piwai.tjmoid;

import static org.junit.Assert.assertEquals;

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
