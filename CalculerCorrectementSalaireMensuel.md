# Introduction #

Cette page a pour but de décrire la bonne utilisation de l'écran suivant :

![http://tjmoid.googlecode.com/svn/trunk/TJMoid/snapshot2.png](http://tjmoid.googlecode.com/svn/trunk/TJMoid/snapshot2.png)

# Détails #


TJMoid fonctionne de manière simplifiée par rapport à Maestro. Il ne permet de rentrer qu'un seul TJM pour un mois donné, ce qui pose problème lorsqu'on est entre deux missions, ou que l'on donne une formation quelques jours par mois. Le **CA Manuel** permet de contourner ce problème.

## CA Manuel ##

### Pour les consultants ###
Le champ _CA Manuel_ n'est pas obligatoire et restera la plupart du temps à 0.

Cependant, il permet d'ajouter du CA pour les jours où l'on est facturé différemment.

Par exemple, si j'ai un TJM à 500 euros, et que pour un mois donné j'ai donné 3 jours de formation à 600 euros, il suffit d'entrée un CA Manuel de 3**(600-500)**0.9 = 270. Pourquoi le coefficient 0.9 ? Parce qu'en général le commercial prend aussi la marge de 10% sur les jours de formation.

### Pour les commerciaux ###
TJMoid est conçu pour être utilisé par les consultants, mais peut-être très bien répondre au besoin des commerciaux.

Pour cela, il suffit de rentrer un TJM à 0, le champ congés/intercontrats à 0 (mais **pas le champ CSS**, qui impacte le salaire fixe brut), puis d'ajouter le CA Manuel qui va bien.

Pour calculer le CA Manuel du commercial : prendre le "CA Généré" dans Maestro, et le multiplier par 0.1 (les 10% qui vous reviennent en tant que commercial). Le CA Généré dans Maestro correspond au TJM du consultant multiplié par le nombre de jours facturés.

## Congés, intercontrats ##
Le nombre de jours de congés et le nombre de jours d'intercontrats sont fusionnés en un seul champ pour une raison simple : ils ont strictement le même effet sur votre salaire. Il s'agit, purement et simplement, de **jours non facturés**.

## CSS ##

Le nombre de CSS est différents du nombres de jours de congés / intercontrats. En effet, un CSS est un jour non facturé mais aussi non payé. Il diminue donc le salaire fixe, ce qui implique un coût de salaire plus bas et impacte donc moins le CA généré qu'un congé payé.

## Jours communautaires ##
Les jours communautaires ne sont pas encore pris en compte par TJMoid, mais le seront dans un avenir proche. En attendant, vous pouvez jouer avec le CA Manuel, à condition de connaître la formule.

## Primes brut non lissées ##

Les primes étant lissées sur 6 mois, il n'est pas forcément évident de mesurer l'impact d'un changement de facturation, de la prise de jours de congés, etc. Le champ **primes brut non lissées** est la pour ça : il permet de calculer, concrètement, combien de primes ont été générées pour un mois donné.

Le calcul des primes lissées d'un mois X correspond à la moyenne des primes non lissées des 6 mois précédents.

Attention : les primes brut non lissées peuvent être négatives, si le nombre de jours facturés est trop faible. Cela impactera donc la moyenne des primes lissées, et fera diminuer les primes versées sur les mois suivants.