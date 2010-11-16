package info.piwai.tjmoid;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LouarneFactGenerator {

	private final Random random = new Random();

	private final List<String> louarneFacts;

	public LouarneFactGenerator() {
		louarneFacts = Arrays.asList( //
				"Notre sprint backlog, on l'a planifié CST", //
				"Capiiiico, sous ton soleil qui chante iiii", //
				"Avec CV-Forge, un CV de perdu... bah un de perdu !", //
				"Secu Admin est une application démocratique : tout le monde peut être admin", //
				"Maestro-Salaire est ouvert, mais surtout ne le dites pas, des fois que quelqu'un l'utiliserai", //
				"Quelqu'un a les sources du CAS utilisé en prod ???", //
				"SecurityAPI.getCurrentUsername(), bien mieux que SecurityContextHolder.getContext().getAuthentication() !",
				"Ya un louarne, jte dis qu'y a un louarne !", //
				"Je viens de mettre à jour la doc d'install de Maestro, c'est cool il faut plus que 3 jours pour l'installer", //
				"Je démarre Alfresco ? Tu rigoles, c'est trop long, on verra bien si ça passe en prod", //
				"Je peux avoir les logs ? Oui : envoi un ticket aux admins... par recommandé", //
				"Heu... c'est quoi ce dragon dans les logs ???", //
				"Qui a enlevé tous les post-its ?!!", //
				"Quand je pense Excellence, je pisse Ex.. ??", //
				"Tu vois, Capico, ça serait bien si on pouvait faire comme dans Warcraft", //
				"Tu vois, Capico, ça serait bien si on pouvait faire comme dans Starcraft", //
				"Capico, c'est un outil de formation ET un outil de gestion de projet. En même temps.", //
				"Capico forme les stagiaires. Les stagiaires forment Maestro", //
				"Les apparts, c'est une arnaque des patrons pour payer moins de taxes", //
				"Le ski, c'est une arnaque des patrons pour payer moins de taxes", //
				"Les restos, c'est une arnaque des patrons pour payer moins de taxes", //
				"Les jours communautaires, c'est une arnaque des patrons pour payer moins de taxes", //
				"Les formations, c'est une arnaque des patrons pour payer moins de taxes", //
				"Les patrons, c'est une arnaque des patrons pour payer moins de taxes", //
				"Les CSS coûtent plus cher que les CP", //
				"Les CP coûtent plus cher que les CSS", //
				"Capico fait tout. A l'exception, évidemment, de ce dont vous avez besoin.", //
				"Héhé, j'ai commité, c'est toi qui va devoir merger !", //
				"On est agile : pour rattraper la courbe du Sprint, on va bosser 10h par jour", //
				""
		);
	}

	public String getRandomLouarneFact() {
		int louarneFactNb = louarneFacts.size();
		int louarneFactIndex = random.nextInt(louarneFactNb);
		return louarneFacts.get(louarneFactIndex);
	}

}
