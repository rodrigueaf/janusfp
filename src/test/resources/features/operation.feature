# language: fr
# auteur Claude Rodrigue AFFODOGANDJI

Fonctionnalité: Gestion des opérations

  Les informations d'une opération sont :
  - La date d'enregistrement
  - La description de l'opération
  - Le type de l'opération
  - Le budget associé
  - Le montant de l'opération
  - L'opération de budget
  - Les détails des comptes de l'opération
    $ Le compte
    $ Le montant
    $ Le sens (débit / crédit) : à gérer comme comptabilité

  Scénario: Ajouter une opération de dépense avec succès
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de dépense valide à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout de l'opération
    Et 1 opération est créée
    Et 2 détail d'opération au "CREDIT" sont créé
    Et 1 détail d'opération au "DEBIT" sont créé

  Scénario: Ajouter une opération de produit avec succès
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de produit valide à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout de l'opération
    Et 1 opération est créée
    Et 2 détail d'opération au "DEBIT" sont créé
    Et 1 détail d'opération au "CREDIT" sont créé

  Scénario: Ajouter une opération de virement entre comptes de trésorerie avec succès
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de virement valide à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout de l'opération
    Et 1 opération est créée
    Et 2 détail d'opération au "DEBIT" sont créé
    Et 2 détail d'opération au "CREDIT" sont créé

  Scénario: Ajouter une opération de dépense en ne renseignant pas les 2 comptes
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de dépense disposant d'un nombre incorrect de compte à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient le message sur l'opération "Le nombre de comptes renseigné est incorrecte"
    Et 0 opération est créée

  Scénario: Ajouter une opération de produit en ne renseignant pas les 2 comptes
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de produit disposant d'un nombre incorrect de compte à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient le message sur l'opération "Le nombre de comptes renseigné est incorrecte"
    Et 0 opération est créée

  Scénario: Ajouter une opération de virement en ne renseignant pas les 2 comptes
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération de virement disposant d'un nombre incorrect de compte à enregistrer
    Lorsqu' on ajoute l'opération
    Alors on obtient le message sur l'opération "Le nombre de comptes renseigné est incorrecte"
    Et 0 opération est créée

  Scénario: Ajouter une opération en ne renseignant pas la description
    Etant donné qu'aucune opération n'était enregistrée
    Et qu'on dispose d'une opération dont la description n'est pas renseignée
    Lorsqu' on ajoute l'opération
    Alors on obtient le message sur l'opération "La description est obligatoire"

  Scénario: Consulter les détails d'une opération
    Etant donné qu'une opération était déjà enregistrée
    Lorsqu' on récupère cette opération
    Alors on obtient l'opération déjà enregistrée

  Scénario: Récupérer la liste des opérations
    Etant donné qu'une opération était déjà enregistrée
    Lorsqu' on récupère la liste des opérations
    Alors on a une liste contenant l'opération déjà enregistrée

  Scénario: Modifier une opération avec succès
    Etant donné qu'une opération était déjà enregistrée
    Et qu'on récupère cette opération pour le modifier
    Lorsqu' on modifie l’opération
    Alors on obtient une réponse confirmant la réussite de l'opération de modification de l'opération

  Scénario: Supprimer une opération avec succès
    Etant donné qu'une opération était déjà enregistrée
    Lorsqu' on supprime l'opération
    Alors on obtient une réponse confirmant la réussite de l'opération de suppression de l'opération
    Et 0 opération est créée

  Scénario: Rechercher une opération
    Etant donné qu'une opération était déjà enregistrée
    Lorsqu' on recherche des opérations en fonction d'un critère
    Alors les opérations récupérées doivent respecter le critère défini
