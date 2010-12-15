package info.piwai.tjmoid;

public enum JoursOuvres {
	
	YEAR_2010(2010, 22, 22), //
	YEAR_2011(2011, 22, 22), //
	;
	
	private final int annee;
	private final int[] jourOuvresParMois;
	
	private JoursOuvres(int annee, int... jourOuvresParMois) {
		
		if (jourOuvresParMois.length!=12) {
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
	
	/**
	 * @return null if year is not supported
	 */
	public static JoursOuvres fromYear(int year) {
		for (JoursOuvres joursOuvres : values()) {
			if (joursOuvres.getAnnee()==year) {
				return joursOuvres;
			}
		}
		return null;
	}
	
	public static int joursOuvres(int year, int monthNumber) {
		return fromYear(year).getJourOuvresParMois()[monthNumber];
	}
	

}
