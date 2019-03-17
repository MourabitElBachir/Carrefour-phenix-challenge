# Carrefour-phenix-challenge

[![Build Status](https://travis-ci.org/MourabitElBachir/Carrefour-phenix-challenge.svg?branch=master)](https://travis-ci.org/MourabitElBachir/Carrefour-phenix-challenge)
[![Coverage Status](https://coveralls.io/repos/github/MourabitElBachir/Carrefour-phenix-challenge/badge.svg)](https://coveralls.io/github/MourabitElBachir/Carrefour-phenix-challenge)

### 1- Objectif du projet 

Le principe est de récuperer les transactions de tous les magasins à partir d'un fichier "transaction_date-d-un-jour.data", ainsi que les prix de produits par magasin à partir des fichiers referentiels collectés par magasin.

  - les transactions : `transactions_YYYYMMDD.data`
  - les référentiels : `reference_prod-ID_MAGASIN_YYYYMMDD.data` où ID_MAGASIN est un UUID identifiant le magasin.
  
Le résultat du programme sont les fichiers :

1. `top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data` 
2. `top_100_ventes_GLOBAL_YYYYMMDD.data`
3. `top_100_ca_<ID_MAGASIN>_YYYYMMDD.data`
4. `top_100_ca_GLOBAL_YYYYMMDD.data`
5. `top_100_ventes_<ID_MAGASIN>_YYYYMMDD-J7.data` 
6. `top_100_ventes_GLOBAL_YYYYMMDD-J7.data`
7. `top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data`
8. `top_100_ca_GLOBAL_YYYYMMDD-J7.data`

#### Contraintes: selon [Carrefour Group Phenix Team](https://github.com/Carrefour-Group/phenix-challenge)

- 2 cpu 
- 512M ram 
- efficacité (temps d'exécution % ressources consommées)
- nombre de produits en constante évolution
- nombre de transactions en grande évolution
- pas de backend (base de données, Hadoop, Spark, Kafka, etc.)
- languages: Scala, Java, Bash, Go


## 2- Description de la solution

#### Programme

Le programme permet d'avoir la traçabilité sur les données pendant l'éxecution. Si on a par exemple une transaction datée le 14/05/2017 avec juste des transactions relatives aux deux derniers jours, le programme va calculer la formule J7 sur ces jours en affichant à l'utilisateur que les autres transactions sont manquantes.

#### Modèles de données - Classes principales

- <b>Classe Transaction :</b> Permet de cooncrétiser une transaction (id de la transaction, date, reference magasin, id du produit, quantité)
- <b>Classe Item :</b> Permet de concrétiser un produit (id du produit, prix)
- <b>Classe ItemSale :</b> C'est une classe pour concrétiser les lignes de sortie pour un calcul de vente (id produit, somme quantités)
- <b>Classe Turnover :</b> C'est une classe pour concrétiser les lignes de sortie pour un calcul de chiffre d'affaire (id produit, chiffre d'affaire)
 

## 3-Prérequis

 - Java 8 au minimum
 - Sbt version : 1.2.8
 - Scala version : 2.12.8
 

## 4- Utiliser le projet avec SBT


### Lancer les tests
```
 sbt test
```

### Lancer le programme
```
sbt "run -i data -o output -d 20170514" 
```

<b>-i:</b> option pour le chemin des données d'entrée
<b>-o:</b> option pour le chemin des données de sortie 
<b>-d:</b> date du jour

## 5- Utiliser le projet avec un JAR
```
scala release/Carrefour-phenix-challenge-production.jar -i data -o output -d 20170514
```


