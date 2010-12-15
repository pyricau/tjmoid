package info.piwai.tjmoid.domain;

public enum JoursOuvres {
	
	

	YEAR_2009(2009, 22, 20, 22, 21, 18, 21, 22, 21, 22, 22, 20, 22), //
	YEAR_2010(2010, 20, 20, 23, 21, 19, 22, 21, 22, 22, 21, 20, 23), //
	YEAR_2011(2011, 21, 20, 23, 21, 22, 22, 21, 23, 22, 21, 22, 22), //
	YEAR_2012(2012, 22, 21, 22, 21, 23, 21, 22, 23, 20, 23, 22, 21), //
	;
	
	/**
	 * @return null if year is not supported
	 */
	public static JoursOuvres fromYear(int year) {
		for (JoursOuvres joursOuvres : values()) {
			if (joursOuvres.getAnnee() == year) {
				return joursOuvres;
			}
		}
		return null;
	}

	public static int joursOuvres(int year, int monthNumber) {
		JoursOuvres joursOuvres = fromYear(year);
		if (joursOuvres!=null) {
			return joursOuvres.getJourOuvresParMois()[monthNumber];
		} else {
			return 0;
		}
	}

	private final int annee;
	private final int[] jourOuvresParMois;

	private JoursOuvres(int annee, int... jourOuvresParMois) {

		if (jourOuvresParMois.length != 12) {
			throw new IllegalArgumentException("There should be 12 values, since there are twelve months in a year");
		}

		this.annee = annee;
		this.jourOuvresParMois = jourOuvresParMois;
	}

	public int[] getJourOuvresParMois() {
		return jourOuvresParMois.clone();
	}

	public int getAnnee() {
		return annee;
	}
	
	@Override
	public String toString() {
		return ""+annee;
	}

}
