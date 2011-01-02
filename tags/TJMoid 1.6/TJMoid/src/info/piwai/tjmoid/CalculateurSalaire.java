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

public class CalculateurSalaire {

	public static class Builder {
		private double salaireBrutDeBase;
		private double tauxChargesSocialesPatronales;
		private double tauxPartageIngeEntreprise;
		private double tauxMargeCommerciale;
		private double nbJoursTravaillesAnnuels;

		public Builder salaireBrutDeBase(double salaireBrutDeBase) {
			this.salaireBrutDeBase = salaireBrutDeBase;
			return this;
		}

		public Builder tauxChargesSocialesPatronales(double tauxChargesSocialesPatronales) {
			this.tauxChargesSocialesPatronales = tauxChargesSocialesPatronales;
			return this;
		}

		public Builder tauxPartageIngeEntreprise(double tauxPartageIngeEntreprise) {
			this.tauxPartageIngeEntreprise = tauxPartageIngeEntreprise;
			return this;
		}

		public Builder tauxMargeCommerciale(double tauxMargeCommerciale) {
			this.tauxMargeCommerciale = tauxMargeCommerciale;
			return this;
		}

		public Builder nbJoursTravaillesAnnuels(double nbJoursTravaillesAnnuels) {
			this.nbJoursTravaillesAnnuels = nbJoursTravaillesAnnuels;
			return this;
		}

		public CalculateurSalaire build() {
			return new CalculateurSalaire(salaireBrutDeBase, tauxChargesSocialesPatronales, tauxPartageIngeEntreprise,
					tauxMargeCommerciale, nbJoursTravaillesAnnuels);
		}

	}

	private final double salaireBrutDeBase;
	private final double tauxChargesSocialesPatronales;
	private final double tauxPartageIngeEntreprise;
	private final double tauxMargeCommerciale;
	private final double nbJoursTravaillesAnnuels;

	private CalculateurSalaire(double salaireBrutDeBase, double tauxChargesSocialesPatronales,
			double tauxPartageIngeEntreprise, double tauxMargeCommerciale, double nbJoursTravaillesAnnuels) {
		this.salaireBrutDeBase = salaireBrutDeBase;
		this.tauxChargesSocialesPatronales = tauxChargesSocialesPatronales;
		this.tauxPartageIngeEntreprise = tauxPartageIngeEntreprise;
		this.tauxMargeCommerciale = tauxMargeCommerciale;
		this.nbJoursTravaillesAnnuels = nbJoursTravaillesAnnuels;

	}

	public long calculerSalaireBrut(long tjm) {

		double chiffreAffaireMensuelGenerePartDeLIngenieur = calculerChiffreAffaireMensuelGenerePartDeLIngenieur(tjm);

		double primesBrut = calculerPrimesBrut(chiffreAffaireMensuelGenerePartDeLIngenieur);

		double primesBrutBornees = calculerPrimesBrutBornees(primesBrut);

		double salaireMensuelBrut = calculerSalaireMensuelBrut(primesBrutBornees);

		double salaireAnnuelBrut = calculerSalaireAnnuelBrut(salaireMensuelBrut);

		return Math.round(salaireAnnuelBrut);
	}

	private double calculerChiffreAffaireMensuelGenerePartDeLIngenieur(long tjm) {

		double nbJoursTravaillesMensuelMoyen = nbJoursTravaillesAnnuels / 12;

		double chiffreAffaireMensuelGenere = tjm * nbJoursTravaillesMensuelMoyen;

		double chiffreAffaireMensuelGenereMoinsMargeCommerciale = chiffreAffaireMensuelGenere
				* (1 - tauxMargeCommerciale);

		double chiffreAffaireMensuelGenerePartDeLIngenieur = chiffreAffaireMensuelGenereMoinsMargeCommerciale
				* tauxPartageIngeEntreprise;
		return chiffreAffaireMensuelGenerePartDeLIngenieur;
	}

	private double calculerPrimesBrut(double chiffreAffaireMensuelGenerePartDeLIngenieur) {

		double coutSalaire = calculerCoutSalaire();

		double primesHorsCharges = chiffreAffaireMensuelGenerePartDeLIngenieur - coutSalaire;

		return primesHorsCharges / tauxChargesSocialesPatronales;
	}

	private double calculerCoutSalaire() {
		return salaireBrutDeBase * tauxChargesSocialesPatronales;
	}

	private double calculerPrimesBrutBornees(double primesBrut) {
		return Math.max(primesBrut, 0);
	}

	private double calculerSalaireMensuelBrut(double primesBrutBornees) {
		return salaireBrutDeBase + primesBrutBornees;
	}

	private double calculerSalaireAnnuelBrut(double salaireMensuelBrut) {
		return 12 * salaireMensuelBrut;
	}

}
