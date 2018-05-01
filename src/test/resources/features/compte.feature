# language: fr
# auteur Claude Rodrigue AFFODOGANDJI

Fonctionnalité: Gestion des comptes

  Les informations d'un compte sont :
  - numeroCompte : Le numéro de compte
  - libelle : Le titre donné au compte
  - type : Le type de compte (Exple : compte de charge, produit, trésorerie, ...)
  - utilisable : Un compte est utilisable que si il n'a pas de sous compte. Les opérations sont enregistrées sur les comptes utilisables
  - soldeInitial : Le solde initial du compte
  - soldeCourant : Le dernier solde connu du compte
  - dateCreation : La date de création du compte

  Scénario: Ajouter un compte avec succès
    Etant donné qu'aucun compte n'était enregistré
    Et qu'on dispose d'un compte valide à enregistrer
    Lorsqu' on ajoute le compte
    Alors on obtient une réponse confirmant la réussite de l'opération d'ajout du compte
    Et 1 compte est créé
    Et le compte créé est celui soumis à l'ajout

  Scénario: Ajouter un compte dont le libellé existe déjà
    Etant donné qu'un compte était déjà enregistré
    Et qu'on dispose d'un compte avec le même libellé à enregistrer
    Lorsqu' on ajoute le compte
    Alors on obtient le message sur le compte "Un compte avec le même libellé existe déjà"
    Et 1 compte demeure créé

  Scénario: Ajouter un compte utilisable dont le parent spécifié est aussi utilisable
    Etant donné qu'un compte était déjà enregistré
    Et qu'on dispose d'un compte ayant ce compte pour compte parent
    Et que le compte parent est utilisable
    Lorsqu' on ajoute le nouveau compte
    Alors on obtient le message sur le compte "Le compte parant est déjà utilisable. Veuillez le rendre inutilisable pour continuer"
    Et 1 compte demeure créé

  Scénario: Ajouter un compte dont le libellé n'est pas renseigné
    Etant donné qu'aucun compte n'était enregistré
    Et qu'on dispose d'un compte dont le libellé n'est pas renseigné
    Lorsqu' on ajoute le compte
    Alors on obtient le message sur le compte "Le libellé est obligatoire"

  Scénario: Modifier un compte avec succès
    Etant donné qu'un compte était déjà enregistré
    Et qu'on récupère ce compte pour le modifier
    Lorsqu' on modifie le compte
    Alors on obtient une réponse confirmant la réussite de l'opération de modification du compte

  Scénario: Modifier un compte en ne renseignant pas le libellé
    Etant donné qu'un compte était déjà enregistré
    Et qu'on récupère ce compte pour le modifier en ne renseignant pas le libellé
    Lorsqu' on modifie le compte
    Alors on obtient le message sur le compte "Le libellé est obligatoire"

  Scénario: Modifier un compte en renseignant un libellé déjà existant
    Etant donné que deux comptes étaient déjà enregistrés
    Et qu'on récupère ce compte pour modifier son libellé par une valeur qui existe déjà
    Lorsqu' on modifie le compte
    Alors on obtient le message sur le compte "Un compte avec le même libellé existe déjà"

  Scénario: Rendre utilisable un compte qui possède au moins un compte fils
    Etant donné que deux comptes étaient déjà enregistrés
    Et que le compte A est le parent du compte B
    Et qu'on récupère le compte A pour le rendre utilisable
    Lorsqu' on modifie le compte
    Alors on obtient le message sur le compte "Ce compte a déjà au moins un sous-compte. Il ne peut plus être utilisable"

  Scénario: Consulter les détails d'un compte
    Etant donné qu'un compte était déjà enregistré
    Lorsqu' on récupère ce compte
    Alors on obtient le compte déjà enregistré

  Scénario: Récupérer la liste des comptes
    Etant donné qu'un compte était déjà enregistré
    Lorsqu' on récupère la liste des comptes
    Alors on a une liste contenant le compte déjà enregistré

  Scénario: Supprimer un compte avec succès
    Etant donné qu'un compte était déjà enregistré
    Lorsqu' on supprime le compte
    Alors on obtient une réponse confirmant la réussite de l'opération de suppression du compte
    Et 0 compte est créé

  Scénario: Supprimer un compte qui a des sous comptes
    Etant donné que deux comptes étaient déjà enregistrés
    Et que le compte A est le parent du compte B
    Lorsqu' on supprime le compte
    Alors on obtient le message sur le compte "Ce compte a déjà au moins un sous compte. Il ne peut plus être supprimé"
    Et 2 compte demeure créé

  Scénario: Supprimer un compte déjà utilisé
    Etant donné qu'un compte était déjà enregistré
    Et que ce compte est déjà utilisé dans une opération
    Lorsqu' on supprime le compte
    Alors on obtient le message sur le compte "Ce compte a déjà été utilisé dans une opération. Il ne peut plus être supprimé"
    Et 1 compte demeure créé

  Scénario: Rechercher un compte
    Etant donné que deux comptes étaient déjà enregistrés
    Lorsqu' on recherche des comptes en fonction d'un critère
    Alors les comptes récupérés doivent respecter le critère défini

  Scénario: Activer un compte
    Etant donné qu'un compte était déjà enregistré
    Lorsqu' on active le compte
    Alors le compte récupéré doit être activé

  Scénario: Désactiver un compte
    Etant donné qu'un compte était déjà enregistré
    Lorsqu' on désactive le compte
    Alors le compte récupéré doit être désactivé
