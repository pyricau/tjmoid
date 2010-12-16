package info.piwai.tjmoid.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class SalaireMensuel implements Comparable<SalaireMensuel> {

	/**
	 * Nécessaire pour calculer l'impact d'un CSS sur le brut
	 */
	private static final double NOMBRE_DHEURES_MOYEN_PAR_MOIS = 151.67;

	/**
	 * Nécessaire pour calculer l'impact d'un CSS sur le brut
	 */
	private static final double NOMBRE_DHEURES_PAR_JOUR = 7;

	/**
	 * Same as month numbers in {@link Calendar} : January =
	 * {@link Calendar#JANUARY}
	 */
	protected int monthNumber;

	protected int year;

	protected int nbJoursOuvres;

	protected int nbConges;

	protected int nbCongesSansSolde;

	protected int nbJoursCommunautaires;

	/**
	 * Attention : dans les calculs, le CA Manuel est aussi partagé entre la
	 * société et le salarié (règle des 60/40)
	 */
	protected double chiffreAffaireManuel;

	protected double salaireBrutDeBase;

	protected double tauxChargesSocialesPatronales;

	protected double tauxPartageSalariéEntreprise;

	protected int tjm;

	protected double tauxMargeCommerciale;

	/**
	 * Not persisted
	 */
	private transient double primesLissées;

	public void validate(List<String> validationErrors) {

		if (monthNumber < Calendar.JANUARY || monthNumber > Calendar.DECEMBER) {
			validationErrors.add("Numéro de mois invalide : " + monthNumber);
		}

		if (JoursOuvres.fromYear(year) == null) {
			validationErrors.add("Année invalide : " + year);
		}

		if (nbJoursOuvres < 0 || nbJoursOuvres > 31) {
			validationErrors.add("Le nombre de jours ouvrés doit être compris entre 0 et 31, il vaut " + nbJoursOuvres);
		}

		if (nbConges < 0 || nbConges > nbJoursOuvres) {
			validationErrors.add("Le nombre de congés doit être compris entre 0 et " + nbJoursOuvres + ", il vaut " + nbConges);
		}

		if (nbCongesSansSolde < 0 || nbCongesSansSolde > nbJoursOuvres) {
			validationErrors.add("Le nombre de congés sans solde doit être compris entre 0 et " + nbJoursOuvres + ", il vaut " + nbCongesSansSolde);
		}

		if (nbJoursCommunautaires < 0 || nbJoursCommunautaires > nbJoursOuvres) {
			validationErrors.add("Le nombre de jours communautaires doit être compris entre 0 et " + nbJoursOuvres + ", il vaut " + nbJoursCommunautaires);
		}

		if (salaireBrutDeBase < 0) {
			String format = "Le salaire de base (%d €) ne doit pas être inférieur à O";
			validationErrors.add(String.format(format, salaireBrutDeBase));
		}

		if (tauxChargesSocialesPatronales < 1) {
			validationErrors.add("Le taux de charges sociales patronales doit être inférieur à 1, il vaut " + tauxChargesSocialesPatronales);
		}

		if (tauxPartageSalariéEntreprise < 0 || tauxPartageSalariéEntreprise > 1) {
			validationErrors.add("Le taux de partage salarié / entreprise doit être compris entre 0 et 1, il vaut " + tauxPartageSalariéEntreprise);
		}

		if (tauxMargeCommerciale < 0 || tauxMargeCommerciale > 1) {
			validationErrors.add("Le taux de marge commerciale doit être compris entre 0 et 1, il vaut " + tauxMargeCommerciale);
		}

		if (tjm < 0) {
			validationErrors.add("Le taux de facturation doit être supérieur ou égal à 0, il vaut " + tjm);
		}

		if (nbJoursOuvres < nbConges + nbCongesSansSolde + nbJoursCommunautaires) {
			String format = "Le nombre de jours ouvrés (%d) est inférieur à la somme des congés (%d), congés sans soldes (%d) et jours communautaires (%d)";
			validationErrors.add(String.format(format, nbJoursOuvres, nbConges, nbCongesSansSolde, nbJoursCommunautaires));
		}
	}

	public int calculerPrimesLissées(List<SalaireMensuel> salairesSixDerniersMois) {
		if (salairesSixDerniersMois.size() != 6) {
			throw new IllegalArgumentException("Il faut 6 mois de salaire !");
		}
		validateOrThrow();

		double sommeChiffresAffairesPourPrimes = 0;
		for (SalaireMensuel salaireMensuel : salairesSixDerniersMois) {
			sommeChiffresAffairesPourPrimes += salaireMensuel.calculerChiffreAffairePourPrimes();
		}

		double chiffreAffairePourPrimeMoyen = sommeChiffresAffairesPourPrimes / 6;

		double primesExactes = chiffreAffairePourPrimeMoyen / tauxChargesSocialesPatronales;

		// TODO la différence entre l'arrondi et l'exact est normalement
		// reportée au mois suivant
		int primesArrondies = (int) Math.round(primesExactes);

		return Math.max(primesArrondies, 0);
	}

	private double calculerChiffreAffairePourPrimes() {
		validateOrThrow();

		double salaireBrutDuMois = calculerSalaireBrutDuMois();

		double chiffreAffairePourSalarié = calculerChiffreAffairePourSalarié(salaireBrutDuMois);

		double coutSalaire = salaireBrutDuMois * tauxChargesSocialesPatronales;

		double chiffreAffairePourPrimes = chiffreAffairePourSalarié - coutSalaire;

		return chiffreAffairePourPrimes;

	}

	public void validateOrThrow() {
		List<String> validationErrors = new ArrayList<String>();
		validate(validationErrors);

		if (validationErrors.size() > 0) {
			throw new IllegalStateException("Should not compute CA without resolving validation errors first: " + validationErrors);
		}
	}

	private double calculerSalaireBrutDuMois() {
		double tauxHoraireMoyen = salaireBrutDeBase / NOMBRE_DHEURES_MOYEN_PAR_MOIS;

		double retraitCongesSansSolde = NOMBRE_DHEURES_PAR_JOUR * tauxHoraireMoyen * nbCongesSansSolde;

		double salaireBrutDuMois = salaireBrutDeBase - retraitCongesSansSolde;
		return salaireBrutDuMois;
	}

	private double calculerChiffreAffairePourSalarié(double salaireBrutDuMois) {
		double chiffreAffaireGenere = calculerChiffreAffaireGénéré(salaireBrutDuMois);

		return tauxPartageSalariéEntreprise * chiffreAffaireGenere;
	}

	private double calculerChiffreAffaireGénéré(double salaireBrutDuMois) {
		int nbJoursFacturés = calculerNbJoursFacturés();
		double chiffreAffaireFacturation = (1 - tauxMargeCommerciale) * (tjm * nbJoursFacturés);

		// TODO vérifier la formule de calcul des jours communautaires
		double tauxJoursCommunautaires = (1.0 * nbJoursCommunautaires) / nbJoursOuvres;
		double chiffreAffaireCommunautaire = (salaireBrutDuMois * tauxJoursCommunautaires) * tauxChargesSocialesPatronales / tauxPartageSalariéEntreprise;

		double chiffreAffaireGenere = chiffreAffaireFacturation + chiffreAffaireCommunautaire + chiffreAffaireManuel;
		return chiffreAffaireGenere;
	}

	private int calculerNbJoursFacturés() {
		return nbJoursOuvres - (nbConges + nbCongesSansSolde + nbJoursCommunautaires);
	}

	public void updatePrimeLissées(List<SalaireMensuel> salairesSixDerniersMois) {
		this.primesLissées = calculerPrimesLissées(salairesSixDerniersMois);
	}

	private double calculerChiffreAffairePourSalarié() {
		validateOrThrow();

		double salaireBrutDuMois = calculerSalaireBrutDuMois();

		return calculerChiffreAffairePourSalarié(salaireBrutDuMois);
	}

	@Override
	public int compareTo(SalaireMensuel another) {

		int compared = Integer.valueOf(year).compareTo(another.year);

		if (compared == 0) {
			compared = Integer.valueOf(monthNumber).compareTo(another.monthNumber);
		}

		return compared;
	}

	public double getPrimesLissées() {
		return primesLissées;
	}

	public double getTotalBrutMensuel() {
		return calculerSalaireBrutDuMois() + primesLissées;
	}

	public String getTjmAsString() {
		return tjm + "";
	}

	public void setTjmAsString(String tjm) {
		this.tjm = intFromString(tjm);
	}

	public boolean tjmChanged(String tjm) {
		return intFromString(tjm) != this.tjm;
	}

	public String getCongesAsString() {
		return nbConges + "";
	}

	public void setCongesAsString(String nbConges) {
		this.nbConges = intFromString(nbConges);
	}

	public boolean congesChanged(String nbConges) {
		return intFromString(nbConges) != this.nbConges;
	}

	private static int intFromString(String intString) {
		int intValue;
		if ("".equals(intString)) {
			intValue = 0;
		} else if ("-".equals(intString)) {
			intValue = 0;
		} else {
			intValue = Integer.parseInt(intString);
		}
		return intValue;
	}

	public double getFixeBrutMensuel() {
		return calculerSalaireBrutDuMois();
	}

	public double getPrimesBrutMensuelles() {
		return primesLissées;
	}

	public double getTotalNetMensuel() {
		return getFixeBrutMensuel() * 0.77;
	}
	
	public double getChiffreAffaireGenere() {
		return calculerChiffreAffaireGénéré(calculerSalaireBrutDuMois());
	}

}