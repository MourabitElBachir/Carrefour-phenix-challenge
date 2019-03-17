# Carrefour-phenix-challenge

[![Build Status](https://travis-ci.org/MourabitElBachir/Carrefour-phenix-challenge.svg?branch=master)](https://travis-ci.org/MourabitElBachir/Carrefour-phenix-challenge)
[![Coverage Status](https://coveralls.io/repos/github/MourabitElBachir/Carrefour-phenix-challenge/badge.svg?branch=master)](https://coveralls.io/github/MourabitElBachir/Carrefour-phenix-challenge?branch=master)

## Prérequis

 - Java 8 au minimum
 - Sbt version : 1.2.8
 - Scala version : 2.12.8

### Objectif du projet 

Le principe est de collecter les transactions de tous les magasins à partir d'un fichier "transaction_date-d-un-jour.data", ainsi que les prix de produits par magasin à partir des fichiers referentiels collectés par magasin.

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




