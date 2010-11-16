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

		public Builder tauxChargesSocialesPatronales(
				double tauxChargesSocialesPatronales) {
			this.tauxChargesSocialesPatronales = tauxChargesSocialesPatronales;
			return this;
		}

		public Builder tauxPartageIngeEntreprise(
				double tauxPartageIngeEntreprise) {
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
			return new CalculateurSalaire(salaireBrutDeBase,
					tauxChargesSocialesPatronales, tauxPartageIngeEntreprise,
					tauxMargeCommerciale, nbJoursTravaillesAnnuels);
		}

	}

	private final double salaireBrutDeBase;
	private final double tauxChargesSocialesPatronales;
	private final double tauxPartageIngeEntreprise;
	private final double tauxMargeCommerciale;
	private final double nbJoursTravaillesAnnuels;

	private CalculateurSalaire(double salaireBrutDeBase,
			double tauxChargesSocialesPatronales,
			double tauxPartageIngeEntreprise, double tauxMargeCommerciale,
			double nbJoursTravaillesAnnuels) {
		this.salaireBrutDeBase = salaireBrutDeBase;
		this.tauxChargesSocialesPatronales = tauxChargesSocialesPatronales;
		this.tauxPartageIngeEntreprise = tauxPartageIngeEntreprise;
		this.tauxMargeCommerciale = tauxMargeCommerciale;
		this.nbJoursTravaillesAnnuels = nbJoursTravaillesAnnuels;

	}

	public long calculerSalaireBrut(long tjm) {

		double salaireAnnuelBrut = calculerSalaireAnnuelBrut(tjm);

		return Math.round(salaireAnnuelBrut);
	}

	private double calculerSalaireAnnuelBrut(long tjm) {
		double salaireMensuelBrut = calculerSalaireMensuelBrut(tjm);

		return 12 * salaireMensuelBrut;
	}

	private double calculerSalaireMensuelBrut(long tjm) {
		double primesBrutBornees = calculerPrimesBrutBornees(tjm);

		return salaireBrutDeBase + primesBrutBornees;
	}

	private double calculerPrimesBrutBornees(long tjm) {
		double primesBrut = calculerPrimesBrut(tjm);
		return Math.max(primesBrut, 0);
	}

	private double calculerPrimesBrut(long tjm) {
		double chiffreAffaireMensuelGenerePartDeLIngenieur = chiffreAffaireMensuelGenerePartDeLIngenieur(tjm);

		double coutSalaire = calculerCoutSalaire();

		double primesHorsCharges = chiffreAffaireMensuelGenerePartDeLIngenieur
				- coutSalaire;

		return primesHorsCharges / tauxChargesSocialesPatronales;
	}

	private double calculerCoutSalaire() {
		return salaireBrutDeBase * tauxChargesSocialesPatronales;
	}

	private double chiffreAffaireMensuelGenerePartDeLIngenieur(long tjm) {
		double nbJoursTravaillesMensuelMoyen = nbJoursTravaillesAnnuels / 12;

		double chiffreAffaireMensuelGenere = tjm
				* nbJoursTravaillesMensuelMoyen;

		double chiffreAffaireMensuelGenereMoinsMargeCommerciale = chiffreAffaireMensuelGenere
				* (1 - tauxMargeCommerciale);

		double chiffreAffaireMensuelGenerePartDeLIngenieur = chiffreAffaireMensuelGenereMoinsMargeCommerciale
				* tauxPartageIngeEntreprise;
		return chiffreAffaireMensuelGenerePartDeLIngenieur;
	}

}
